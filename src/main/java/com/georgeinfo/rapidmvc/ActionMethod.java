/*
* Copyright (c) George software studio, All Rights Reserved.
* George <GeorgeWorld@qq.com> | QQ:178069108 | www.georgeinfo.com 
*/
package com.georgeinfo.rapidmvc;

import java.lang.reflect.Method;

/**
 * 控制器方法与方法注解包装类
 *
 * @author George <GeorgeWorld@qq.com>
 */
public class ActionMethod {
    private Method method;
    private String annotationValue;

    public ActionMethod() {
    }

    public ActionMethod(Method method) {
        this.method = method;
    }

    public ActionMethod(Method method, String annotationValue) {
        this.method = method;
        this.annotationValue = annotationValue;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public String getAnnotationValue() {
        return annotationValue;
    }

    public void setAnnotationValue(String annotationValue) {
        this.annotationValue = annotationValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ActionMethod that = (ActionMethod) o;

        if (method != null ? !method.equals(that.method) : that.method != null) return false;
        return annotationValue != null ? annotationValue.equals(that.annotationValue) : that.annotationValue == null;
    }

    @Override
    public int hashCode() {
        int result = method != null ? method.hashCode() : 0;
        result = 31 * result + (annotationValue != null ? annotationValue.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ActionMethod{" +
                "method=" + method +
                ", annotationValue='" + annotationValue + '\'' +
                '}';
    }
}
