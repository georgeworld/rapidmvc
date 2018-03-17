/*
* Copyright (c) George software studio, All Rights Reserved.
* George <GeorgeWorld@qq.com> | QQ:178069108 | www.georgeinfo.com 
*/
package com.georgeinfo.rapidmvc;

/**
 * 控制器方法的参数包装类
 *
 * @author George <GeorgeWorld@qq.com>
 */
public class MethodParameter {
    private int index;
    private int indexInUriParams;
    private String name;
    private Class<?> type;
    private Object value;

    public MethodParameter() {
    }

    public MethodParameter(int index, int indexInUriParams, String name, Class<?> type) {
        this.index = index;
        this.indexInUriParams = indexInUriParams;
        this.name = name;
        this.type = type;
    }

    public MethodParameter(int index, int indexInUriParams, String name, Class<?> type, Object value) {
        this.index = index;
        this.indexInUriParams = indexInUriParams;
        this.name = name;
        this.type = type;
        this.value = value;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getIndexInUriParams() {
        return indexInUriParams;
    }

    public void setIndexInUriParams(int indexInUriParams) {
        this.indexInUriParams = indexInUriParams;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "MethodParameter{" +
                "index=" + index +
                ", indexInUriParams=" + indexInUriParams +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", value=" + value +
                '}';
    }
}
