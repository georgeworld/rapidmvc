/*
 * Author : George <GeorgeNiceWorld@gmail.com> | <Georgeinfo@163.com>
 * Copyright (C) George (http://www.georgeinfo.com), All Rights Reserved.
 */
package com.georgeinfo.rapidmvc;

import com.georgeinfo.base.util.logger.GeorgeLogger;
import gbt.config.GeorgeLoggerFactory;
import java.lang.reflect.Method;

/**
 * 控制器对象与控制器方法但对包装器
 *
 * @author George <Georgeinfo@163.com>
 */
public class ControllerMethod {

    private static final GeorgeLogger logger = GeorgeLoggerFactory.getLogger(ControllerMethod.class);
    private Controller controller;
    private Method method;

    public ControllerMethod() {
    }

    public ControllerMethod(Controller controller, Method method) {
        this.controller = controller;
        this.method = method;
    }

    public Controller getController() {
        return controller;
    }

    public Controller getNewController() {
        try {
            return controller.getClass().newInstance();
        } catch (InstantiationException ex) {
            logger.error(ex.getMessage(), ex);
        } catch (IllegalAccessException ex) {
            logger.error(ex.getMessage(), ex);
        }
        return null;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }
}
