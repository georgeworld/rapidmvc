/*
 * Author : George <GeorgeNiceWorld@gmail.com> | <Georgeinfo@163.com>
 * Copyright (C) George (http://www.georgeinfo.com), All Rights Reserved.
 */
package com.georgeinfo.rapidmvc;

import com.georgeinfo.base.util.logger.GeorgeLogger;
import com.georgeinfo.rapidmvc.dependents.uri.UriTemplate;
import com.georgeinfo.rapidmvc.exception.RapidMvcException;
import gbt.config.GeorgeLoggerFactory;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 控制器对象实例包装类
 *
 * @author George <Georgeinfo@163.com>
 */
public class ControllerWrapper {

    private static final GeorgeLogger logger = GeorgeLoggerFactory.getLogger(ControllerWrapper.class);
    private Controller controllerObject;
    /**
     * 控制器所对应的path uri部分
     */
    private String controllerPath;
    /**
     * 控制器方法全名称，与控制器器方法本身的映射map，key=控制器类全路径#方法名称，value=方法反射对象
     *
     * @Post注解内写的path路径，value=控制器方法对象
     */
    private final Map<String, Method> methodNameMap = new HashMap<String, Method>();
    /**
     * 控制器方法子路径与控制器名称的映射map（@Post注解映射）,key=控制器方法上的
     *
     * @Post注解内写的path路径，value=控制器方法的全名称
     */
    private final Map<String, String> postMethodPathMap = new HashMap<String, String>();
    /**
     * 控制器方法子路径与控制器名称的映射map（@Gost注解映射）,key=控制器方法上的@Get注解内写的path路径，value=控制器方法的全名称
     */
    private final Map<String, String> getMethodPathMap = new HashMap<String, String>();

    public ControllerWrapper(String controllerPath, Controller controllerObject) {
        this.controllerPath = controllerPath;
        this.controllerObject = controllerObject;
    }

    public String getControllerPath() {
        return controllerPath;
    }

    public void setControllerPath(String controllerPath) {
        this.controllerPath = controllerPath;
    }

    public Controller getControllerObject() {
        return controllerObject;
    }

    public void setControllerObject(Controller controllerObject) {
        this.controllerObject = controllerObject;
    }

    public Map<String, Method> getMethodNameMap() {
        return methodNameMap;
    }

    public Map<String, String> getPostMethodPathMap() {
        return postMethodPathMap;
    }

    public Map<String, String> getGetMethodPathMap() {
        return getMethodPathMap;
    }

    public boolean addMethod(HttpMethodEnum restType, String methodPath, String methodNamePath, Method method) {
        if (methodPath != null && !methodPath.trim().isEmpty() && method != null) {
            String methodName = controllerObject.getClass().getName() + "#" + method.getName();

            if (restType == HttpMethodEnum.POST) {//POST请求
                if (postMethodPathMap.get(methodPath) != null) {
                    logger.error("## RESTful-Post Method path[" + methodPath + "] has been bind to method:" + method.getName() + " of controller [" + controllerObject.getClass().getName() + "] when add method path.");
                    return false;
                } else {
                    methodNameMap.put(methodName, method);
                    postMethodPathMap.put(methodPath, methodName);
                    return true;
                }
            } else { //GET请求
                if (getMethodPathMap.get(methodPath) != null) {
                    logger.error("## RESTful-Get Method path[" + methodPath + "] has been bind to method:" + method.getName() + " of controller [" + controllerObject.getClass().getName() + "] when add method path.");
                    return false;
                } else {
                    methodNameMap.put(methodName, method);
                    getMethodPathMap.put(methodPath, methodName);
                    return true;
                }
            }
        } else {
            return false;
        }
    }

    public boolean isPostPathRegistered(String methodPath) {
        if (postMethodPathMap.get(methodPath) != null) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isGetPathRegistered(String methodPath) {
        if (getMethodPathMap.get(methodPath) != null) {
            return true;
        } else {
            return false;
        }
    }

    public ExecutableMethod getMatchedMethod(String methodNameAndParametersUri, HttpMethodEnum httpMethodEnum) {
        ExecutableMethod result = null;

        Map<String, String> parameters = new HashMap<>();
        String fullMethodName = null;
        Map<String, String> methodAnnotationFullMethodNameMap = null;
        if (httpMethodEnum == HttpMethodEnum.POST) {
            methodAnnotationFullMethodNameMap = postMethodPathMap;
        } else if (httpMethodEnum == HttpMethodEnum.GET) {
            methodAnnotationFullMethodNameMap = getMethodPathMap;
        } else {
            throw new RapidMvcException("## Unsupported HTTP request method:" + httpMethodEnum.name());
        }

        methodNameAndParametersUri = methodNameAndParametersUri.trim();
        for (Map.Entry<String, String> e : methodAnnotationFullMethodNameMap.entrySet()) {
            String annotationUriTemplate = e.getKey().trim();
            if (annotationUriTemplate.startsWith("/") && !methodNameAndParametersUri.startsWith("/")) {
                methodNameAndParametersUri = "/" + methodNameAndParametersUri;
            }
            if (methodNameAndParametersUri.startsWith("/") && !annotationUriTemplate.startsWith("/")) {
                methodNameAndParametersUri = StringUtils.removeStart(methodNameAndParametersUri, "/");
            }

            UriTemplate uriTemplate = new UriTemplate(annotationUriTemplate);
            boolean r = uriTemplate.match(methodNameAndParametersUri, parameters);
            if (r == true) {
                fullMethodName = e.getValue();
                break;
            }
        }

        if (fullMethodName != null && !fullMethodName.trim().isEmpty()) {
            result = new ExecutableMethod(methodNameMap.get(fullMethodName), parameters);
            result.setMethodNameAndParametersUri(methodNameAndParametersUri);
            return result;
        } else {
            logger.error("## 找不到合适的控制器方法");
            return null;
        }
    }

    public ExecutableMethod getMatchedPostMethod(String methodNameAndParametersUri) {
        return getMatchedMethod(methodNameAndParametersUri, HttpMethodEnum.POST);
    }

    public ExecutableMethod getMatchedGetMethod(String methodNameAndParametersUri) {
        return getMatchedMethod(methodNameAndParametersUri, HttpMethodEnum.GET);
    }

    public Map<String, String> getControllerMethodAnnotationUri(HttpMethodEnum restfullType) {
        if (restfullType == HttpMethodEnum.GET) {
            return this.getMethodPathMap;
        } else if (restfullType == HttpMethodEnum.POST) {
            return this.postMethodPathMap;
        } else {
            throw new RapidMvcException("## Unsupported HTTP request method.");
        }
    }
}
