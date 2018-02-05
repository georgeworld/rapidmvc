/*
 * Author : George <GeorgeNiceWorld@gmail.com> | <Georgeinfo@163.com>
 * Copyright (C) George (http://www.georgeinfo.com), All Rights Reserved.
 */
package com.georgeinfo.rapidmvc.api;

/**
 * 主工厂类接口
 *
 * @author George <Georgeinfo@163.com>
 */
public interface MainFactory {

    public PathTree getPathTree(ResourceLoader resourceLoader);

    public ResourceLoader getResourceLoader();
}
