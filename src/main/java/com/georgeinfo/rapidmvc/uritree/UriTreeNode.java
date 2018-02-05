/*
 * Author : George <GeorgeNiceWorld@gmail.com> | <Georgeinfo@163.com>
 * Copyright (C) George (http://www.georgeinfo.com), All Rights Reserved.
 */
package com.georgeinfo.rapidmvc.uritree;

import java.util.ArrayList;
import java.util.List;

/**
 * URI多叉树树节点定义类
 *
 * @author George <Georgeinfo@163.com>
 */
public class UriTreeNode implements MultiwayTreeNode<UriTreeNode> {

    /**
     * 树节点名称
     */
    private String name;
    /**
     * 数节点搭载的节点数据
     */
    private UriNodeBean data;
    /**
     * 子节点列表
     */
    private List<UriTreeNode> childList = new ArrayList<UriTreeNode>();
    /**
     * URI树节点类型
     */
    private UriNodeType uriNodeType;

    //构造函数
    public UriTreeNode() {
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public UriNodeBean getData() {
        return data;
    }

    public void setData(UriNodeBean data) {
        this.data = data;
    }

    @Override
    public List<UriTreeNode> getChildList() {
        return childList;
    }

    public void setChildList(List<UriTreeNode> childList) {
        this.childList = childList;
    }

    public UriTreeNode addChildNode(UriTreeNode childNode) {
        this.childList.add(childNode);
        return this;
    }

    public UriNodeType getUriNodeType() {
        return uriNodeType;
    }

    public void setUriNodeType(UriNodeType uriNodeType) {
        this.uriNodeType = uriNodeType;
    }

}
