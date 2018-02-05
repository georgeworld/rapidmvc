/*
 * Author : George <GeorgeNiceWorld@gmail.com> | <Georgeinfo@163.com>
 * Copyright (C) George (http://www.georgeinfo.com), All Rights Reserved.
 */
package com.georgeinfo.rapidmvc.api;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author George <Georgeinfo@163.com>
 */
public interface LoginInterceptor {

    public boolean checkLogin(String url, HttpServletRequest request, HttpServletResponse response);

}
