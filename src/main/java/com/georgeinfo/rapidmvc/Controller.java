/*
 * Author : George <GeorgeNiceWorld@gmail.com> | <Georgeinfo@163.com>
 * Copyright (C) George (http://www.georgeinfo.com), All Rights Reserved.
 */
package com.georgeinfo.rapidmvc;

import com.georgeinfo.rapidmvc.support.AcHelper;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author George <Georgeinfo@163.com>
 */
public interface Controller {

    public HttpServletRequest getRequest();

    public void setRequest(HttpServletRequest request);

    public HttpServletResponse getResponse();

    public void setResponse(HttpServletResponse request);

    public AcHelper getAcHelper();

    public void setAcHelper(AcHelper helper);

}
