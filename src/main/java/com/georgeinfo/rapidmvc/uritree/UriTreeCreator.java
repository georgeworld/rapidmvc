/*
 * Author : George <GeorgeNiceWorld@gmail.com> | <Georgeinfo@163.com>
 * Copyright (C) George (http://www.georgeinfo.com), All Rights Reserved.
 */
package com.georgeinfo.rapidmvc.uritree;

/**
 *
 * @author George <Georgeinfo@163.com>
 */
public interface UriTreeCreator {

    public boolean doCreateTree(UriTreeImpl tree);

    public boolean doCheckTree(UriTreeImpl tree);
}
