/*
 * Author : George <GeorgeNiceWorld@gmail.com> | <Georgeinfo@163.com>
 * Copyright (C) George (http://www.georgeinfo.com), All Rights Reserved.
 */
package com.georgeinfo.rapidmvc.uritree;

/**
 * 多叉树定义接口（通用）
 *
 * @author George <Georgeinfo@163.com>
 */
public interface MultiwayTree {

    /**
     * 得到根节点
     */
    public MultiwayTreeNode getRoot();

    /**
     * 遍历树
     */
    public void traverseTree();
}
