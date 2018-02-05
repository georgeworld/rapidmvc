/*
 * Author : George <GeorgeNiceWorld@gmail.com> | <Georgeinfo@163.com>
 * Copyright (C) George (http://www.georgeinfo.com), All Rights Reserved.
 */
package com.georgeinfo.rapidmvc.httpcycle;

import com.georgeinfo.rapidmvc.ThreadLocalContainer;
import com.georgeinfo.rapidmvc.support.AcHelper;
import java.util.Enumeration;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author George <GeorgeNiceWorld@gmail.com>
 *
 */
public class ActionContextThreadLocalImpl implements ActionContext {

    @Override
    public HttpServletRequest getRequest() {
        return ThreadLocalContainer.getCurrentThreadRequest();
    }

    @Override
    public HttpServletResponse getResponse() {
        return ThreadLocalContainer.getCurrentThreadResponse();
    }
    @Override
    public AcHelper getHelper() {
        return ThreadLocalContainer.getCurrentThreadAcHelper();
    }

    @Override
    public HttpSession getSession() {
        return getRequest().getSession();
    }

    @Override
    public ServletContext getServletContext() {
        return getRequest().getServletContext();
    }

    @Override
    public String getParameter(String name) {
        return getRequest().getParameter(name);
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        return getRequest().getParameterMap();
    }

    @Override
    public Enumeration<String> getParameterNames() {
        return getRequest().getParameterNames();
    }

    @Override
    public void setAttribute(String name, Object value) {
        getRequest().setAttribute(name, value);
    }

    @Override
    public Object getAttribute(String name) {
        return getRequest().getAttribute(name);
    }

    @Override
    public void removeAttribute(String name) {
        getRequest().removeAttribute(name);
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        return getRequest().getAttributeNames();
    }
}
