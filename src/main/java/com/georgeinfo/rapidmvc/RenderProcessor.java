/*
 * Author : George <GeorgeNiceWorld@gmail.com> | <Georgeinfo@163.com>
 * Copyright (C) George (http://www.georgeinfo.com), All Rights Reserved.
 */
package com.georgeinfo.rapidmvc;

import com.georgeinfo.base.beans.generic.KeyValueBean;
import com.georgeinfo.base.util.logger.GeorgeLogger;
import com.georgeinfo.rapidmvc.annotation.Get;
import com.georgeinfo.rapidmvc.annotation.Param;
import com.georgeinfo.rapidmvc.annotation.Post;
import com.georgeinfo.rapidmvc.exception.MissingHttpResponseException;
import com.georgeinfo.rapidmvc.exception.NotSupportedRenderException;
import com.georgeinfo.rapidmvc.render.FreeRender;
import com.georgeinfo.rapidmvc.render.HtmlRender;
import com.georgeinfo.rapidmvc.render.JsonRender;
import com.georgeinfo.rapidmvc.render.RedirectRender;
import com.georgeinfo.rapidmvc.render.ViewRender;
import com.georgeinfo.rapidmvc.render.XmlRender;
import com.georgeinfo.rapidmvc.result.*;
import gbt.config.GeorgeLoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.LinkedList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.text.StyledEditorKit;

/**
 * 渲染执行器
 *
 * @author George <Georgeinfo@163.com>
 */
public class RenderProcessor {

    private static final GeorgeLogger logger = GeorgeLoggerFactory.getLogger(RenderProcessor.class);

    public static boolean execute(
            Controller controllerObj,
            Method method,
            HttpMethodEnum requestMethodType) {
        return execute(controllerObj, method, null, requestMethodType);
    }

    private static boolean getMethodParameter(int paramIndex, Parameter[] parameters, LinkedList<MethodParameter> methodParameters, String[] annotationParamNames) {
        Parameter p = parameters[paramIndex];
        if (p.isNamePresent()) {
            String paramName = p.getName();
            Class<?> paramType = p.getType();
            int indexInUriParams = getIndexInUriParams(annotationParamNames, paramName);
            methodParameters.add(new MethodParameter(paramIndex, indexInUriParams, paramName, paramType));
            return true;
        } else {
            return false;
        }
    }

    private static int getIndexInUriParams(String[] annotationParamNames, String paramName) {
        int indexInUriParams = 0;
        for (String name : annotationParamNames) {
            if (name.trim().equals(paramName)) {
                break;
            }
            indexInUriParams++;
        }
        return indexInUriParams;
    }

    public static boolean execute(
            Controller controllerObj,
            Method method,
            Object[] uriParameters,
            HttpMethodEnum requestMethodType) {
        Object runResult = null;
        boolean hasException = false;
        Exception exObj = null;
        try {
            if (uriParameters != null && uriParameters.length > 0) {
                boolean invokeMethod = true;
                String uriParamValue = null;
                if (requestMethodType == HttpMethodEnum.POST) {
                    //从控制器方法上提取@Post注解信息
                    Post p = method.getAnnotation(Post.class);
                    if (p != null) {
                        uriParamValue = p.value();
                    } else {
                        String[] msg = new String[]{ResultStatus.SYSTEM_ERROR.name(), "错误：控制器方法没有@Post注解"};
                        runResult = DefaultRenderFactory.json(msg);
                        invokeMethod = false;
                    }
                } else if (requestMethodType == HttpMethodEnum.GET) {
                    //从控制器方法上提取@Get注解信息
                    Get g = method.getAnnotation(Get.class);
                    if (g != null) {
                        uriParamValue = g.value();
                    } else {
                        String[] msg = new String[]{ResultStatus.SYSTEM_ERROR.name(), "错误：控制器方法没有@Get注解"};
                        runResult = DefaultRenderFactory.json(msg);
                        invokeMethod = false;
                    }
                } else {
                    //目前，仅支持POST和GET请求
                    String[] msg = new String[]{ResultStatus.SYSTEM_ERROR.name(), "错误：目前不支持" + requestMethodType + "类请求"};
                    runResult = DefaultRenderFactory.json(msg);
                    invokeMethod = false;
                }

                //控制器方法参数列表
                LinkedList<MethodParameter> methodParameters = null;
                if (uriParamValue != null && !uriParamValue.trim().isEmpty()) {
                    String[] annotationParamNames = uriParamValue.split("/");
                    if (annotationParamNames.length == uriParameters.length) {
                        //控制器方法参数列表
                        methodParameters = new LinkedList<MethodParameter>();
                        Class<?>[] parameterTypes = method.getParameterTypes();

                        //获取控制器方法的入参列表
                        //第一个维度是参数索引，第二个维度是一个参数上的不同注解的索引
                        Annotation[][] pas = method.getParameterAnnotations();
                        if (pas != null && pas.length >= annotationParamNames.length) {
                            Parameter[] parameters = method.getParameters();

                            int paramIndex = 0;
                            for (Annotation[] as : pas) {
                                if (as != null && as.length >= 1) {
                                    Param paramAnnotation = null;
                                    for (Annotation a : as) {
                                        if (a instanceof Param) {
                                            paramAnnotation = (Param) a;
                                            break;
                                        }
                                    }

                                    if (paramAnnotation != null) {
                                        //获得参数注解中的参数名字
                                        String paramName = paramAnnotation.value();
                                        //获得参数类型
                                        Class<?> paramType = parameterTypes[paramIndex];
                                        int indexInUriParams = getIndexInUriParams(annotationParamNames, paramName);
                                        methodParameters.add(new MethodParameter(paramIndex, indexInUriParams, paramName, paramType));
                                    } else {
                                        //方法入参上，没有参数注解，尝试使用JDK1.8的Parameter机制，获取入参信息
                                        boolean r = getMethodParameter(paramIndex, parameters, methodParameters, annotationParamNames);
                                        if (r == false) {
                                            //靠JDK 1.8 的parameter机制，也获取不到参数名字，只能报错
                                            String[] msg = new String[]{ResultStatus.SYSTEM_ERROR.name(), "错误：无法获取控制器方法参数名字[1]"};
                                            runResult = DefaultRenderFactory.json(msg);
                                            invokeMethod = false;
                                        }
                                    }
                                } else {
                                    //方法入参上，没有参数注解，尝试使用JDK1.8的Parameter机制，获取入参信息
                                    boolean r = getMethodParameter(paramIndex, parameters, methodParameters, annotationParamNames);
                                    if (r == false) {
                                        //靠JDK 1.8 的parameter机制，也获取不到参数名字，只能报错
                                        String[] msg = new String[]{ResultStatus.SYSTEM_ERROR.name(), "错误：无法获取控制器方法参数名字[2]"};
                                        runResult = DefaultRenderFactory.json(msg);
                                        invokeMethod = false;
                                    }
                                }
                                paramIndex++;
                            }

                        } else {
                            //控制器方法实际的入参数量，比方法Post/Get注解中规定的参数数量，不匹配，提示错误
                            String[] msg = new String[]{ResultStatus.SYSTEM_ERROR.name(), "错误：控制器方法参数不匹配"};
                            runResult = DefaultRenderFactory.json(msg);
                            invokeMethod = false;
                        }

                    } else {
                        //如果控制器方法注解中，定义的URI参数数量，与请求方URL中传递过来的URL参数数量不一致，则报错误的请求
                        String[] msg = new String[]{ResultStatus.CUSTOM_ERROR.name(), "错误：请求参数不匹配，错误的请求"};
                        runResult = DefaultRenderFactory.json(msg);
                        invokeMethod = false;
                    }
                } else {
                    //请求URL传递过来了URI参数，但是控制器方法的注解中，没有定义URI参数，此时应该报错误的请求
                    String[] msg = new String[]{ResultStatus.CUSTOM_ERROR.name(), "错误：错误的请求路径"};
                    runResult = DefaultRenderFactory.json(msg);
                    invokeMethod = false;
                }

                if (invokeMethod == true) {
                    if (methodParameters != null && !methodParameters.isEmpty()) {
                        Object[] values = new Object[methodParameters.size()];
                        int i = 0;
                        for (MethodParameter mp : methodParameters) {
                            Object paramValue = uriParameters[mp.getIndex()];
                            values[i] = paramValue;
                            i++;
                        }
                        runResult = method.invoke(controllerObj, values);//执行方法
                    } else {
                        //无法获得控制器方法入参
                        String[] msg = new String[]{ResultStatus.SYSTEM_ERROR.name(), "错误：无法获得控制器方法参数列表"};
                        runResult = DefaultRenderFactory.json(msg);
                    }
                }
            }else{
                runResult = method.invoke(controllerObj);//执行方法
            }
        } catch (IllegalAccessException ex) {
            hasException = true;
            exObj = ex;
            logger.error(ex.getMessage(), ex);
        } catch (IllegalArgumentException ex) {
            hasException = true;
            exObj = ex;
            logger.error(ex.getMessage(), ex);
        } catch (InvocationTargetException ex) {
            hasException = true;
            exObj = ex;
            logger.error(ex.getMessage(), ex);
        } catch (Exception ex) {
            hasException = true;
            exObj = ex;
            logger.error(ex.getMessage(), ex);
        }

        if (hasException == false) { //如果反射执行控制器方法，没有出现异常
            if (runResult != null) {
                if (runResult instanceof View) {
                    View result = (View) runResult;
                    ViewRender render = new ViewRender(result);

                    HttpServletRequest request = controllerObj.getRequest();
                    HttpServletResponse response = controllerObj.getResponse();
                    if (request != null && response != null) {
                        return render.doRender(request, response);
                    } else {
                        if (request == null) {
                            throw new MissingHttpResponseException("## HttpRequest instance is not set to controller object.");
                        }
                        if (response == null) {
                            throw new MissingHttpResponseException("## HttpResponse instance is not set to controller object.");
                        }
                    }
                } else if (runResult instanceof Json) {
                    Json result = (Json) runResult;
                    JsonRender render = new JsonRender(result);

                    HttpServletRequest request = controllerObj.getRequest();
                    HttpServletResponse response = controllerObj.getResponse();
                    if (request != null && response != null) {
                        return render.doRender(request, response);
                    } else {
                        if (request == null) {
                            throw new MissingHttpResponseException("## HttpRequest instance is not set to controller object.");
                        }
                        if (response == null) {
                            throw new MissingHttpResponseException("## HttpResponse instance is not set to controller object.");
                        }
                    }
                } else if (runResult instanceof Html) {
                    Html result = (Html) runResult;
                    HtmlRender render = new HtmlRender(result);

                    HttpServletRequest request = controllerObj.getRequest();
                    HttpServletResponse response = controllerObj.getResponse();
                    if (request != null && response != null) {
                        return render.doRender(request, response);
                    } else {
                        if (request == null) {
                            throw new MissingHttpResponseException("## HttpRequest instance is not set to controller object.");
                        }
                        if (response == null) {
                            throw new MissingHttpResponseException("## HttpResponse instance is not set to controller object.");
                        }
                    }
                } else if (runResult instanceof Xml) {
                    Xml result = (Xml) runResult;
                    XmlRender render = new XmlRender(result);

                    HttpServletRequest request = controllerObj.getRequest();
                    HttpServletResponse response = controllerObj.getResponse();
                    if (request != null && response != null) {
                        return render.doRender(request, response);
                    } else {
                        if (request == null) {
                            throw new MissingHttpResponseException("## HttpRequest instance is not set to controller object.");
                        }
                        if (response == null) {
                            throw new MissingHttpResponseException("## HttpResponse instance is not set to controller object.");
                        }
                    }
                } else if (runResult instanceof Free) {
                    Free result = (Free) runResult;
                    FreeRender render = new FreeRender(result);

                    HttpServletRequest request = controllerObj.getRequest();
                    HttpServletResponse response = controllerObj.getResponse();
                    if (request != null && response != null) {
                        return render.doRender(request, response);
                    } else {
                        if (request == null) {
                            throw new MissingHttpResponseException("## HttpRequest instance is not set to controller object.");
                        }
                        if (response == null) {
                            throw new MissingHttpResponseException("## HttpResponse instance is not set to controller object.");
                        }
                    }
                } else if (runResult instanceof Redirect) {
                    Redirect result = (Redirect) runResult;
                    RedirectRender render = new RedirectRender(result);

                    HttpServletRequest request = controllerObj.getRequest();
                    HttpServletResponse response = controllerObj.getResponse();
                    if (request != null && response != null) {
                        return render.doRender(request, response);
                    } else {
                        if (request == null) {
                            throw new MissingHttpResponseException("## HttpRequest instance is not set to controller object.");
                        }
                        if (response == null) {
                            throw new MissingHttpResponseException("## HttpResponse instance is not set to controller object.");
                        }
                    }
                } else if (runResult instanceof String) {
                    String render = (String) runResult;
                    String contentType = "text/plain";

                    HttpServletResponse response = controllerObj.getResponse();
                    if (response != null) {
                        response.setContentType(contentType);
                        try {
                            PrintWriter writer = response.getWriter();
                            writer.flush();
                            writer.write(render);
                            writer.flush();
                            writer.close();
                            return true;
                        } catch (IOException ex) {
                            logger.error("## IOException when print text context.", ex);
                            return false;
                        }
                    } else {
                        throw new MissingHttpResponseException("## HttpResponse instance is not set to controller object.");
                    }
                } else {
                    throw new NotSupportedRenderException("## The render [" + runResult.getClass().getName() + "] is not supported.");
                }
            } else {
                //如果控制器方法无返回值(void)，就认为控制器方法内，自己处理了http的响应渲染，所以这里不做任何渲染处理了
            }
        } else { //如果反射执行控制器方法，出现了异常，则渲染错误页面。
            String errorMsg = exObj.getMessage();
            if (errorMsg == null) {
                errorMsg = "Rapid MVC render processor EXCEPTION";
            }
            logger.info("ERROR CONTENT:" + errorMsg);
            Html result = new Html(errorMsg);
            HtmlRender render = new HtmlRender(result);

            HttpServletRequest request = controllerObj.getRequest();
            HttpServletResponse response = controllerObj.getResponse();
            if (request != null && response != null) {
                return render.doRender(request, response);
            } else {
                if (request == null) {
                    throw new MissingHttpResponseException("## HttpRequest instance is not set to controller object.");
                }
                if (response == null) {
                    throw new MissingHttpResponseException("## HttpResponse instance is not set to controller object.");
                }
            }
        }

        return true;
    }
}
