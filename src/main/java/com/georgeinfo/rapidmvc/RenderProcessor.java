/*
 * Author : George <GeorgeNiceWorld@gmail.com> | <Georgeinfo@163.com>
 * Copyright (C) George (http://www.georgeinfo.com), All Rights Reserved.
 */
package com.georgeinfo.rapidmvc;

import com.georgeinfo.base.beans.generic.KeyValueBean;
import com.georgeinfo.base.util.logger.GeorgeLogger;
import com.georgeinfo.base.util.network.HttpRequest;
import com.georgeinfo.base.util.network.HttpResponse;
import com.georgeinfo.rapidmvc.annotation.Param;
import com.georgeinfo.rapidmvc.exception.MissingHttpResponseException;
import com.georgeinfo.rapidmvc.exception.NotSupportedRenderException;
import com.georgeinfo.rapidmvc.exception.RapidMvcException;
import com.georgeinfo.rapidmvc.render.FreeRender;
import com.georgeinfo.rapidmvc.render.HtmlRender;
import com.georgeinfo.rapidmvc.render.JsonRender;
import com.georgeinfo.rapidmvc.render.RedirectRender;
import com.georgeinfo.rapidmvc.render.ViewRender;
import com.georgeinfo.rapidmvc.render.XmlRender;
import com.georgeinfo.rapidmvc.result.*;
import com.georgeinfo.rapidmvc.support.AcHelper;
import gbt.config.GeorgeLoggerFactory;
import org.apache.commons.lang3.ArrayUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 渲染执行器
 *
 * @author George <Georgeinfo@163.com>
 */
public class RenderProcessor {

    private static final GeorgeLogger logger = GeorgeLoggerFactory.getLogger(RenderProcessor.class);

    //内置参数赋值
    private static boolean builtInParametersSetting(
            Class<?>[] parameterTypes,
            int paramIndex,
            Object[] parameterValues,
            ExecutableMethod em
    ) {
        //判断入参是否是内置支持的类型
        Class<?> paramType = parameterTypes[paramIndex];
        if (paramType == HttpRequest.class || paramType == HttpServletRequest.class) {
            parameterValues[paramIndex] = em.getController().getRequest();
        } else if (paramType == HttpResponse.class || paramType == HttpServletResponse.class) {
            parameterValues[paramIndex] = em.getController().getResponse();
        } else if (paramType == AcHelper.class) {
            parameterValues[paramIndex] = em.getController().getAcHelper();
        } else {
            return false;
        }
        return true;
    }

    private static KeyValueBean<Boolean, Object> getMethodParameter(
            int paramIndex,
            Parameter[] inParametersOfMethod,
            Map<String, String> parametersInAnnotation,
            Object[] parameterValues,
            Class<?>[] parameterTypes,
            ExecutableMethod em) throws ParseException {
        Parameter p = inParametersOfMethod[paramIndex];
        if (p.isNamePresent()) {//如果参数支持JDK1.8的参数机制
            String paramName = p.getName();
            String paramValue = parametersInAnnotation.get(paramName);

            boolean invokeMethod = true;
            Object msg = null;
            if (paramValue != null) {
                Object pv = paramTypeConvert(parameterTypes, paramValue, paramIndex, "yyyy-MM-dd HH:mm:ss");
                parameterValues[paramIndex] = pv;
            } else {
                boolean r = builtInParametersSetting(
                        parameterTypes,
                        paramIndex,
                        parameterValues,
                        em
                );
                if (r == false) {
                    //找不到合适的注解参数，也不是内置方法参数
                    String[] msgContent = new String[]{ResultStatus.SYSTEM_ERROR.name(), "错误：不是合法的注解参数，也不是内置参数"};
                    msg = DefaultRenderFactory.json(msgContent);
                    invokeMethod = false;
                }
            }

            return new KeyValueBean<Boolean, Object>(invokeMethod, msg);
        } else {
            //靠JDK 1.8 的parameter机制，也获取不到参数名字，只能报错
            String[] msg = new String[]{ResultStatus.SYSTEM_ERROR.name(), "错误：无法获取控制器方法参数名字"};
            Object runResult = DefaultRenderFactory.json(msg);
            boolean invokeMethod = false;

            return new KeyValueBean<Boolean, Object>(invokeMethod, runResult);
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
            ExecutableMethod em,
            HttpMethodEnum requestMethodType) {
        Object runResult = null;
        boolean hasException = false;
        Exception exObj = null;
        Controller controllerObj = em.getController();
        Method method = em.getMethod();
        try {
            Map<String, String> parametersInAnnotation = em.getParameters();
            if (parametersInAnnotation != null && !parametersInAnnotation.isEmpty()) {//如果方法注解上有URI参数
                boolean invokeMethod = true;

                //控制器方法参数列表
                Set<String> annotationParamNames = parametersInAnnotation.keySet();

                //获取控制器方法的入参列表
                Class<?>[] parameterTypes = method.getParameterTypes();

                //获得控制器方法的入参参数注解，第一个维度是参数索引，
                //第二个维度是一个参数上的不同注解的索引
                Annotation[][] pas = method.getParameterAnnotations();
                //控制器方法的入参列表
                Parameter[] inParametersOfMethod = method.getParameters();
                Object[] parameterValues = new Object[parameterTypes.length];

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
                            String paramValue = parametersInAnnotation.get(paramName);
                            if (paramValue != null) {
                                //对方法参数进行类型判断
                                Object pv = paramTypeConvert(parameterTypes, paramValue, paramIndex, paramAnnotation.format());
                                parameterValues[paramIndex] = pv;
                            } else {//入参上添加了@Param注解，但是并不是控制器方法上，@Get/@Post注解中的URI参数
                                //判断入参是否是内置支持的类型
                                boolean r = builtInParametersSetting(
                                        parameterTypes,
                                        paramIndex,
                                        parameterValues,
                                        em
                                );
                                if (r == false) {
                                    //找不到合适的注解参数，也不是内置方法参数
                                    String[] msgContent = new String[]{ResultStatus.SYSTEM_ERROR.name(), "错误：不是合法的注解参数，也不是内置参数[2]"};
                                    runResult = DefaultRenderFactory.json(msgContent);
                                    logger.error("### Unable to find a matched controller method[1]");
                                }
                            }
                        } else {
                            //方法入参上，没有参数注解，尝试使用JDK1.8的Parameter机制，获取入参信息
                            KeyValueBean<Boolean, Object> gmpResult = getMethodParameter(
                                    paramIndex,
                                    inParametersOfMethod,
                                    parametersInAnnotation,
                                    parameterValues,
                                    parameterTypes,
                                    em);
                            if (gmpResult.getKey() == false) {//只用JDK1.8 参数机制，也获取不到参数名，只能报错
                                invokeMethod = false;
                                runResult = gmpResult.getValue();
                                logger.error("### Unable to find a matched controller method[2]");
                            }
                        }
                    } else {
                        KeyValueBean<Boolean, Object> gmpResult = getMethodParameter(
                                paramIndex,
                                inParametersOfMethod,
                                parametersInAnnotation,
                                parameterValues,
                                parameterTypes,
                                em);
                        if (gmpResult.getKey() == false) {
                            invokeMethod = false;
                            runResult = gmpResult.getValue();
                            logger.error("### Unable to find a matched controller method[2]");
                        }
                    }
                    paramIndex++;
                }


                if (invokeMethod == true) {
                    if (!ArrayUtils.isEmpty(parameterValues)) {
                        runResult = method.invoke(controllerObj, parameterValues);//执行方法
                    } else {
                        //无法获得控制器方法入参
                        String[] msg = new String[]{ResultStatus.SYSTEM_ERROR.name(), "错误：无法获得控制器方法参数列表"};
                        runResult = DefaultRenderFactory.json(msg);
                    }
                }
            } else {
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

    private static Object paramTypeConvert(Class<?>[] parameterTypes, String paramValue, int paramIndex, String format) throws ParseException {
        Class<?> paramType = parameterTypes[paramIndex];
        Object pv = null;
        if (paramType == String.class) {
            pv = paramValue;
        } else if (paramType == int.class || paramType == Integer.class) {
            pv = Integer.parseInt(paramValue);
        } else if (paramType == long.class || paramType == Long.class) {
            pv = Long.parseLong(paramValue);
        } else if (paramType == Date.class) {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            pv = sdf.parse(paramValue);
        } else if (paramType == short.class || paramType == Short.class) {
            pv = Short.parseShort(paramValue);
        } else if (paramType == boolean.class || paramType == Boolean.class) {
            pv = Boolean.parseBoolean(paramValue);
        } else {
            throw new RapidMvcException("## Parameter type [" + paramType + "]  are not supported.");
        }
        return pv;
    }
}
