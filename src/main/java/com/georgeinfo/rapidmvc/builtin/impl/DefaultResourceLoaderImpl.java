/*
 * Author : George <GeorgeNiceWorld@gmail.com> | <Georgeinfo@163.com>
 * Copyright (C) George (http://www.georgeinfo.com), All Rights Reserved.
 */
package com.georgeinfo.rapidmvc.builtin.impl;

import com.georgeinfo.rapidmvc.RapidClassScanner;
import com.georgeinfo.rapidmvc.api.ResourceLoader;
import java.util.Set;

/**
 * Rapid Mvc内嵌默认资源加载器实现类
 *
 * @author George <Georgeinfo@163.com>
 */
public class DefaultResourceLoaderImpl implements ResourceLoader {

    @Override
    public Set<Class<?>> getClassesByPackage(String packagePath) {
        return RapidClassScanner.getClasses(packagePath);
    }

}
