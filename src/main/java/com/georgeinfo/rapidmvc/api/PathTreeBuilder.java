/*
 * Author : George <GeorgeNiceWorld@gmail.com> | <Georgeinfo@163.com>
 * Copyright (C) George (http://www.georgeinfo.com), All Rights Reserved.
 */
package com.georgeinfo.rapidmvc.api;

import java.util.Set;

/**
 *
 * @author George <Georgeinfo@163.com>
 */
public interface PathTreeBuilder {

    public void doBuildTree(Set<Class<?>> classSet);
}
