/*
 * Author : George <GeorgeNiceWorld@gmail.com> | <Georgeinfo@163.com>
 * Copyright (C) George (http://www.georgeinfo.com), All Rights Reserved.
 */
package com.georgeinfo.rapidmvc.api;

import java.util.Set;

/**
 * 资源加载器
 * @author George <Georgeinfo@163.com>
 */
public interface ResourceLoader {

    /**
     * 根据指定的包路径，得到控制器类集合
     *
     * @param packagePath 指定的包路径，以classpath为起点，如：com.georgeinfo.dms.controllers
     * 通常，指定的包路径，在应用层项目的classpath:resources/rapidmvc.properties文件中配置
     * @return
     */
    public Set<Class<?>> getClassesByPackage(String packagePath);
}
