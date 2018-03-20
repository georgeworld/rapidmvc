/*
 * Author : George <GeorgeNiceWorld@gmail.com> | <Georgeinfo@163.com>
 * Copyright (C) George (http://www.georgeinfo.com), All Rights Reserved.
 */
package com.georgeinfo.rapidmvc.api;

import com.georgeinfo.rapidmvc.Controller;
import com.georgeinfo.rapidmvc.ControllerWrapper;
import com.georgeinfo.rapidmvc.ExecutableMethod;
import com.georgeinfo.rapidmvc.HttpMethodEnum;
import com.georgeinfo.rapidmvc.builtin.impl.MayBeMatchedController;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * 控制器类URI路径树定义及管理类接口
 *
 * @author George <GeorgeNiceWorld@gmail.com>
 */
public interface PathTree {

    public PathTree init();

    public Map<String, ControllerWrapper> getPathTreeMap();

    public boolean addPath(String controllerPath, String methodPath, String methodNamePath, Controller controllerObject, Method method, HttpMethodEnum restfullType);

    public ExecutableMethod doMatchingPath(MayBeMatchedController mayBeMatchedController, HttpMethodEnum restfullType);

    public ExecutableMethod doMatchingPath(String realUri, HttpMethodEnum requestMethodType);

    public ResourceLoader getResourceLoader();


}
