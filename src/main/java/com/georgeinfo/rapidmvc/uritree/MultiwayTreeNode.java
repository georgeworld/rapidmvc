/*
 * Author : George <GeorgeNiceWorld@gmail.com> | <Georgeinfo@163.com>
 * Copyright (C) George (http://www.georgeinfo.com), All Rights Reserved.
 */
package com.georgeinfo.rapidmvc.uritree;

import java.util.List;

/**
 * 多叉树树节点定义接口
 *
 * @author George <Georgeinfo@163.com>
 */
public interface MultiwayTreeNode<T extends MultiwayTreeNode> {

    public String getName();

    public void setName(String name);

    public NodeBean getData();

    public List<T> getChildList();
}
