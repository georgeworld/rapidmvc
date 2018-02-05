/*
 * Author : George <GeorgeNiceWorld@gmail.com> | <Georgeinfo@163.com>
 * Copyright (C) George (http://www.georgeinfo.com), All Rights Reserved.
 */
package com.georgeinfo.rapidmvc;

import com.georgeinfo.base.util.logger.GeorgeLogger;
import com.georgeinfo.rapidmvc.exception.MissingHttpResponseException;
import com.georgeinfo.rapidmvc.exception.NotSupportedRenderException;
import com.georgeinfo.rapidmvc.render.FreeRender;
import com.georgeinfo.rapidmvc.render.HtmlRender;
import com.georgeinfo.rapidmvc.render.JsonRender;
import com.georgeinfo.rapidmvc.render.RedirectRender;
import com.georgeinfo.rapidmvc.render.ViewRender;
import com.georgeinfo.rapidmvc.render.XmlRender;
import com.georgeinfo.rapidmvc.result.Free;
import com.georgeinfo.rapidmvc.result.Html;
import com.georgeinfo.rapidmvc.result.Json;
import com.georgeinfo.rapidmvc.result.Redirect;
import com.georgeinfo.rapidmvc.result.View;
import com.georgeinfo.rapidmvc.result.Xml;
import gbt.config.GeorgeLoggerFactory;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 渲染执行器
 *
 * @author George <Georgeinfo@163.com>
 */
public class RenderProcessor {

    private static final GeorgeLogger logger = GeorgeLoggerFactory.getLogger(RenderProcessor.class);

    public static boolean execute(Controller controllerObj, Method method) {
        Object runResult = null;
        boolean hasException = false;
        Exception exObj = null;
        try {
            runResult = method.invoke(controllerObj);//执行方法
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
