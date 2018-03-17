/*
* Copyright (c) George software studio, All Rights Reserved.
* George <GeorgeWorld@qq.com> | QQ:178069108 | www.georgeinfo.com 
*/
package com.georgeinfo.rapidmvc.builtin.impl;

import com.georgeinfo.rapidmvc.ControllerWrapper;

/**
 * 根据访问的URI，找到的合适控制器的主要信息容器类
 *
 * @author George <GeorgeWorld@qq.com>
 */
public class MatchedController {
    private ControllerWrapper controllerWrapper;
    private String methodPath;
    private boolean containUriParameters = false;
    private String controllerPath;
    /**
     * URI参数路径，格式如：edit/123/990
     */
    private String uriParametersPath;

    public MatchedController() {
    }

    public MatchedController(boolean containUriParameters, String controllerPath,String uriParametersPath) {
        this.containUriParameters = containUriParameters;
        this.controllerPath = controllerPath;
        this.uriParametersPath = uriParametersPath;
    }

    public MatchedController(ControllerWrapper controllerWrapper, String methodPath, boolean containUriParameters) {
        this.controllerWrapper = controllerWrapper;
        this.methodPath = methodPath;
        this.containUriParameters = containUriParameters;
    }


    public ControllerWrapper getControllerWrapper() {
        return controllerWrapper;
    }

    public void setControllerWrapper(ControllerWrapper controllerWrapper) {
        this.controllerWrapper = controllerWrapper;
    }

    public String getMethodPath() {
        return methodPath;
    }

    public void setMethodPath(String methodPath) {
        this.methodPath = methodPath;
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

    public String getUriParametersPath() {
        return uriParametersPath;
    }

    public void setUriParametersPath(String uriParametersPath) {
        this.uriParametersPath = uriParametersPath;
    }
}
