/*
 * Author : George <GeorgeNiceWorld@gmail.com> | <Georgeinfo@163.com>
 * Copyright (C) George (http://www.georgeinfo.com), All Rights Reserved.
 */
package com.georgeinfo.rapidmvc.uritree;

/**
 *
 * @author George <Georgeinfo@163.com>
 */
public class UriTreeImpl implements MultiwayTree {

    private UriTreeNode root;

    /**
     * 构造多叉树
     */
    public UriTreeImpl buildTree(UriTreeCreator treeCreator) {
        boolean r = treeCreator.doCreateTree(this);
        boolean cr = treeCreator.doCheckTree(this);
        if (r && cr) {
            return this;
        } else {
            return null;
        }
    }

    @Override
    public UriTreeNode getRoot() {
        return root;
    }

    public void setRoot(UriTreeNode root) {
        this.root = root;
    }

    /**
     * 遍历整棵树
     */
    @Override
    public void traverseTree() {
        StringBuilder sb = new StringBuilder();
        traverseTree(this.root, sb);
    }

    /**
     * 从指定节点开始，遍历树（广度优先）
     */
    public void traverseTree(MultiwayTreeNode<UriTreeNode> treeNode, StringBuilder sb) {
        if (treeNode != null) {
            if ("root".equals(treeNode.getData().getNodeName())) {
                sb.append(treeNode.getData().getNodeName() + ",");
            }

            for (MultiwayTreeNode index : treeNode.getChildList()) {

                sb.append(index.getData().getNodeName() + ",");

                if (index.getChildList() != null && index.getChildList().size() > 0) {

                    traverseTree(index, sb);

                }
            }
        }

        // return sb.toString();
    }

}
