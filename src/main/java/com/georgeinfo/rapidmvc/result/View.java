/*
 * Author : George <GeorgeNiceWorld@gmail.com> | <Georgeinfo@163.com>
 * Copyright (C) George (http://www.georgeinfo.com), All Rights Reserved.
 */
package com.georgeinfo.rapidmvc.result;

/**
 * 视图渲染器结果
 *
 * @author George <Georgeinfo@163.com>
 */
public class View implements Result {

    private String viewName;

    public View() {
    }

    public View(String viewName) {
        this.viewName = viewName;
    }

    public String getViewName() {
        if (viewName != null) {
            if (!viewName.contains(".")) {
                return viewName + ".jsp";
            }
        }
        return viewName;
    }

    public View setViewName(String viewName) {
        this.viewName = viewName;
        return this;
    }

}
