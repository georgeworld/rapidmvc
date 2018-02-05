/*
 * Author : George <GeorgeNiceWorld@gmail.com> | <Georgeinfo@163.com>
 * Copyright (C) George (http://www.georgeinfo.com), All Rights Reserved.
 */
package com.georgeinfo.rapidmvc;

import com.georgeinfo.base.util.logger.GeorgeLogger;
import gbt.config.GeorgeLoggerFactory;
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

    public ControllerWrapper(Controller controllerObject) {
        this.controllerObject = controllerObject;
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

    public boolean addMethod(HttpMethodEnum restType, String methodPath, Method method) {
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

    public Method getMatchedPostMethod(String methodPath) {
        String methodName = postMethodPathMap.get(methodPath);
        if (methodName != null) {
            return methodNameMap.get(methodName);
        } else {
            return null;
        }
    }

    public Method getMatchedGetMethod(String methodPath) {
        String methodName = getMethodPathMap.get(methodPath);
        if (methodName != null) {
            return methodNameMap.get(methodName);
        } else {
            return null;
        }
    }
}
