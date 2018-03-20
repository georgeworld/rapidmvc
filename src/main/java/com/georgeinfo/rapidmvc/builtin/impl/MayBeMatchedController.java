/*
* Copyright (c) George software studio, All Rights Reserved.
* George <GeorgeWorld@qq.com> | QQ:178069108 | www.georgeinfo.com 
*/
package com.georgeinfo.rapidmvc.builtin.impl;

import com.georgeinfo.rapidmvc.ControllerWrapper;

/**
 * 根据访问的URI，找到的可能合适的控制器的主要信息容器类
 *
 * @author George <GeorgeWorld@qq.com>
 */
public class MayBeMatchedController {
    private ControllerWrapper controllerWrapper;
    private boolean containUriParameters = false;
    private String controllerPath;
    /**
     * 控制器方法及URI参数部分的路径，格式如：edit/123/990
     */
    private String methodNameAndParametersUri;

    public MayBeMatchedController() {
    }

    public MayBeMatchedController(String controllerPath, String methodNameAndParametersUri) {
        this.controllerPath = controllerPath;
        this.methodNameAndParametersUri = methodNameAndParametersUri;
    }


    public MayBeMatchedController(ControllerWrapper controllerWrapper, boolean containUriParameters) {
        this.controllerWrapper = controllerWrapper;
        this.containUriParameters = containUriParameters;
    }


    public ControllerWrapper getControllerWrapper() {
        return controllerWrapper;
    }

    public void setControllerWrapper(ControllerWrapper controllerWrapper) {
        this.controllerWrapper = controllerWrapper;
    }

    public boolean isContainUriParameters() {
        return containUriParameters;
    }

    public void setContainUriParameters(boolean containUriParameters) {
        this.containUriParameters = containUriParameters;
    }

    public String getControllerPath() {
        return controllerPath;
    }

    public void setControllerPath(String controllerPath) {
        this.controllerPath = controllerPath;
    }

    public String getMethodNameAndParametersUri() {
        return methodNameAndParametersUri;
    }

    public void setMethodNameAndParametersUri(String methodNameAndParametersUri) {
        this.methodNameAndParametersUri = methodNameAndParametersUri;
    }
}
