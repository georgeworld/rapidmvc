/*
 * Author : George <GeorgeNiceWorld@gmail.com> | <Georgeinfo@163.com>
 * Copyright (C) George (http://www.georgeinfo.com), All Rights Reserved.
 */
package com.georgeinfo.rapidmvc.uritree;

/**
 * URI树节点类型定义枚举类
 *
 * @author George <Georgeinfo@163.com>
 */
public enum UriNodeType {
    /**
     * 控制器包路径节点
     */
    PACKAGE,
    /**
     * 控制器类节点
     */
    CONTROLLER,
    /**
     * http_post节点
     */
    HTTP_POST,
    /**
     * http_get节点
     */
    HTTP_GET,
    /**
     * http_put节点
     */
    HTTP_PUT,
    /**
     * http_delete节点
     */
    HTTP_DELETE,
    /**
     * 控制器方法固定格式URI节点
     */
    ACTION_NORMAL,
    /**
     * 控制器方法参数格式URI节点（如/${type}这种格式）
     */
    ACTION_PARAM;
}
