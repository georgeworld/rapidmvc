/*
 * Author : George <GeorgeNiceWorld@gmail.com> | <Georgeinfo@163.com>
 * Copyright (C) George (http://www.georgeinfo.com), All Rights Reserved.
 */
package com.georgeinfo.rapidmvc.builtin.impl;

import com.georgeinfo.rapidmvc.api.MainFactory;
import com.georgeinfo.rapidmvc.api.PathTree;
import com.georgeinfo.rapidmvc.api.ResourceLoader;

/**
 *
 * @author George <Georgeinfo@163.com>
 */
public class DefaultMainFactoryImpl implements MainFactory {

    @Override
    public PathTree getPathTree(ResourceLoader resourceLoader) {
        return new DefaultPathTreeImpl(resourceLoader);
    }

    @Override
    public ResourceLoader getResourceLoader() {
        return new DefaultResourceLoaderImpl();
    }

}
