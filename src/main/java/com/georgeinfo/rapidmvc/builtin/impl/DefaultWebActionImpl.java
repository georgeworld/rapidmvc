/*
 * Author : George <GeorgeNiceWorld@gmail.com> | <Georgeinfo@163.com>
 * Copyright (C) George (http://www.georgeinfo.com), All Rights Reserved.
 */
package com.georgeinfo.rapidmvc.builtin.impl;

import com.georgeinfo.rapidmvc.api.WebAction;
import com.georgeinfo.rapidmvc.api.PathTree;
import com.georgeinfo.base.util.logger.GeorgeLogger;
import com.georgeinfo.rapidmvc.Controller;
import com.georgeinfo.rapidmvc.ControllerMethod;
import com.georgeinfo.rapidmvc.HttpMethodEnum;
import com.georgeinfo.rapidmvc.api.LoginInterceptor;
import com.georgeinfo.rapidmvc.RenderProcessor;
import com.georgeinfo.rapidmvc.RequestPath;
import com.georgeinfo.rapidmvc.ResultWrapper;
import com.georgeinfo.rapidmvc.ThreadLocalContainer;
import com.georgeinfo.rapidmvc.exception.WrongPathFormatException;
import com.georgeinfo.rapidmvc.support.AcHelper;
import gbt.config.GeorgeLoggerFactory;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author George <Georgeinfo@163.com>
 */
public class DefaultWebActionImpl implements WebAction {

    private static final GeorgeLogger logger = GeorgeLoggerFactory.getLogger(DefaultWebActionImpl.class);

    private LoginInterceptor li;
    private final PathTree pathTree;

    public DefaultWebActionImpl(PathTree pathTree) {
        this.pathTree = pathTree;
    }

    @Override
    public ResultWrapper doProcess(HttpServletRequest request, HttpServletResponse response) {
        ResultWrapper result = new ResultWrapper();

        RequestPath path = new RequestPath(request);
        result.setRequestPath(path);
        String uri = path.getUri();

        if (li != null) {
            if (!li.checkLogin(uri, request, response)) {
                return result;
            }
        }

        String contextPath = request.getContextPath();
        //过滤掉应用上下文路径
        if (contextPath != null && !contextPath.trim().isEmpty()) {
            uri = uri.substring(contextPath.length());
        }
        //过滤掉MVC路径前缀
//        if (uri.startsWith("/"+Constants.mvc_path_prefix)) {
//            uri = uri.substring(Constants.mvc_path_prefix.length() + 1);
//        }

        //设置本次http请求的全程属性
        response.setCharacterEncoding("UTF-8"); //这句不能去掉，否则页面渲染会出现乱码
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException ex) {
            logger.error("## UnsupportedEncodingException when setting UTF-8 character cncoding to request object.", ex);
        }

        //开始解析MVC Path
        String realUri = uri.substring(1);
        String[] pathSubsection = realUri.split("/");
        if (pathSubsection.length < 2) {
            throw new WrongPathFormatException("The MVC path need at least two path subsection.");
        }

        String controllerPath = realUri.substring(0, realUri.lastIndexOf("/"));// pathSubsection[0];
        String methodPath = pathSubsection[pathSubsection.length - 1];
        String httpMethod = request.getMethod();
        ControllerMethod cm = null;
        if (httpMethod.equals(HttpMethodEnum.POST.name())) {
            cm = pathTree.doMatchingPath(controllerPath, methodPath, HttpMethodEnum.POST);
        } else {
            cm = pathTree.doMatchingPath(controllerPath, methodPath, HttpMethodEnum.GET);
        }
        if (cm != null) {
            Controller controller = cm.getController();
            //注入本次请求所产生的request和response对象（将本次请求的request和response对象，绑定到ThreadLocal上）
            controller.setRequest(request);
            controller.setResponse(response);
            //将AcHelper绑定到ThreadLocal上
            controller.setAcHelper(new AcHelper(request));

            Method controllerMethod = cm.getMethod();
            try {
                //找到了控制器方法，执行方法
                boolean r = RenderProcessor.execute(controller, controllerMethod);
                if (r) {
                    result.success();
                } else {
                    result.error().setMessage("使用MVC路径{" + controllerPath + "/" + methodPath + "}找不到匹配的控制器方法。");
                }
            } //            catch (IllegalAccessException ex) {
            //                result.exception().setMessage(ex.getMessage());
            //                logger.error(ex);
            //            } 
            catch (IllegalArgumentException ex) {
                result.exception().setMessage(ex.getMessage());
                logger.error(ex);
            } //            catch (InvocationTargetException ex) {
            //                result.exception().setMessage(ex.getMessage());
            //                logger.error(ex);
            //            } 
            finally {
                //如果执行结果怎么样，最后都要将本次请求所绑定的request和response对象从ThreadLocal中移除
                ThreadLocalContainer.unindRequestFromCurrentThread();
                ThreadLocalContainer.unindResponseFromCurrentThread();
                //将AcHelper从Thread中移除
                ThreadLocalContainer.unindAcHelperFromCurrentThread();
            }
        } else {
            result.error().setMessage("找不到MVC路径{" + controllerPath + "/" + methodPath + "}所注册的控制器方法.");
        }

        return result;
    }

    @Override
    public void setLi(LoginInterceptor li) {
        this.li = li;
    }

    @Override
    public PathTree getPathTree() {
        return pathTree;
    }

}
