/*
 * Author : George <GeorgeNiceWorld@gmail.com> | <Georgeinfo@163.com>
 * Copyright (C) George (http://www.georgeinfo.com), All Rights Reserved.
 */
package com.georgeinfo.rapidmvc.api;

import com.georgeinfo.rapidmvc.ResultWrapper;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author George <Georgeinfo@163.com>
 */
public interface WebAction {

    /**
     * 受理一次Http请求
     *
     * @param request 本次请求的HttpServletRequest对象
     * @param response 本次请求的HttpServletResponse对象
     * @return Http请求处理结果
     */
    public ResultWrapper doProcess(HttpServletRequest request, HttpServletResponse response);

    /**
     * 设置权限拦截器对象
     *
     * @param li 拦截器对象
     */
    public void setLi(LoginInterceptor li);

    /**
     * 得到路径树处理器
     *
     * @return 路径树，整个Rapid MVC的运行环境中，应该只存在一个PathTree实例
     */
    public PathTree getPathTree();

}
