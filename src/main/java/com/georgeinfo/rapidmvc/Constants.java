/*
 * Author : George <GeorgeNiceWorld@gmail.com> | <Georgeinfo@163.com>
 * Copyright (C) George (http://www.georgeinfo.com), All Rights Reserved.
 */
package com.georgeinfo.rapidmvc;

import java.util.ResourceBundle;

/**
 * RapidMVC常量定义（业务常量不要在这里定义）
 *
 * @author George <Georgeinfo@163.com>
 */
public class Constants {

    /**
     * 业务视图文件路径
     */
    public static final String viewsDir = "/WEB-INF/views";
    /**
     * 系统内置视图文件路径
     */
    public static final String sysViewsDir = "/WEB-INF/sysview";
    /**
     * MVC过滤URI前缀
     */
    public static final String mvc_path_prefix = "home";

    /**
     * 控制器类名后缀
     */
    public static final String suffix_of_controller = "Controller";

    /**
     * 控制器所在包路径
     */
    private String controllerPackagePath; // = "com.georgeinfo.sms.controllers";

    private Constants() {
        init();
    }

    // 线程安全的惰性加载单例模式 开始
    private static class ConstantsHolder {

        private static final Constants instance = new Constants();
    }

    public static Constants getInstance() {
        return ConstantsHolder.instance;
    }
    // 线程安全的惰性加载单例模式 结束

    private void init() {
        ResourceBundle bundle = null;
        try {
            bundle = ResourceBundle.getBundle("resources/rapidmvc");
        } catch (java.util.MissingResourceException ex) {
            System.out.println("############# can't find resources/rapidmvc.properties file.\n" + ex.getMessage());
        }
        if (bundle != null) {
            controllerPackagePath = bundle.getString("controller_package_path");
        }
    }

    public String getControllerPackagePath() {
        return controllerPackagePath;
    }

}
