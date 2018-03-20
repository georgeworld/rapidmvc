/*
* Copyright (c) George software studio, All Rights Reserved.
* George <GeorgeWorld@qq.com> | QQ:178069108 | www.georgeinfo.com 
*/
package com.georgeinfo.rapidmvc;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * 可以执行的控制器方法及其相关参数信息
 *
 * @author George <GeorgeWorld@qq.com>
 */
public class ExecutableMethod {
    private Controller controller;
    private Method method;
    private Map<String, String> parameters;

    /**
     * 控制器方法名字及URI参数组成的URI路径，格式如：edit/123/990
     */
    private String methodNameAndParametersUri;

    public ExecutableMethod() {
    }

    public ExecutableMethod(Method method, Map<String, String> parameters) {
        this.method = method;
        this.parameters = parameters;
    }

    public ExecutableMethod(Controller controller, Method method, Map<String, String> parameters, String methodNameAndParametersUri) {
        this.controller = controller;
        this.method = method;
        this.parameters = parameters;
        this.methodNameAndParametersUri = methodNameAndParametersUri;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    public Controller getController() {
        return controller;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public String getMethodNameAndParametersUri() {
        return methodNameAndParametersUri;
    }

    public void setMethodNameAndParametersUri(String methodNameAndParametersUri) {
        this.methodNameAndParametersUri = methodNameAndParametersUri;
    }
}
