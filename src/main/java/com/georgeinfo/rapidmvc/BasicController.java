/*
 * Author : George <GeorgeNiceWorld@gmail.com> | <Georgeinfo@163.com>
 * Copyright (C) George (http://www.georgeinfo.com), All Rights Reserved.
 */
package com.georgeinfo.rapidmvc;

import com.georgeinfo.rapidmvc.httpcycle.ActionContext;
import com.georgeinfo.rapidmvc.httpcycle.ActionContextThreadLocalImpl;
import com.georgeinfo.rapidmvc.render.FreeRenderProcessor;
import com.georgeinfo.rapidmvc.result.Html;
import com.georgeinfo.rapidmvc.result.JavaScript;
import com.georgeinfo.rapidmvc.result.Json;
import com.georgeinfo.rapidmvc.result.Redirect;
import com.georgeinfo.rapidmvc.result.View;
import com.georgeinfo.rapidmvc.result.Xml;
import com.georgeinfo.rapidmvc.support.AcHelper;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author George <Georgeinfo@163.com>
 */
public class BasicController implements Controller, RenderFactory {
    
    
    protected ActionContext ac = new ActionContextThreadLocalImpl();
    protected RenderFactory rf = new RenderFactoryProxy();

    @Override
    public HttpServletRequest getRequest() {
        return ThreadLocalContainer.getCurrentThreadRequest();
    }

    @Override
    public void setRequest(HttpServletRequest request) {
        ThreadLocalContainer.bindRequestToCurrentThread(request);
    }

    @Override
    public HttpServletResponse getResponse() {
        return ThreadLocalContainer.getCurrentThreadResponse();
    }

    @Override
    public void setResponse(HttpServletResponse response) {
        ThreadLocalContainer.bindResponseToCurrentThread(response);
    }

    @Override
    public AcHelper getAcHelper() {
        return ThreadLocalContainer.getCurrentThreadAcHelper();
    }

    @Override
    public void setAcHelper(AcHelper helper) {
        ThreadLocalContainer.bindAcHelperToCurrentThread(helper);
    }

    @Override
    public View view() {
        return DefaultRenderFactory.view();
    }

    @Override
    public View view(String viewName) {
        return DefaultRenderFactory.view(viewName);
    }

    @Override
    public Json json() {
        return DefaultRenderFactory.json();
    }

    @Override
    public Json json(Object object) {
        return DefaultRenderFactory.json(object);
    }

    @Override
    public JavaScript javascript() {
        return DefaultRenderFactory.javascript();
    }

    @Override
    public JavaScript javascript(String javascript) {
        return DefaultRenderFactory.javascript(javascript);
    }

    @Override
    public Html html() {
        return DefaultRenderFactory.html();
    }

    @Override
    public Html html(String htmlContent) {
        return DefaultRenderFactory.json(htmlContent);
    }

    @Override
    public Xml xml() {
        return DefaultRenderFactory.xml();
    }

    @Override
    public Xml xml(String xmlContent) {
        return DefaultRenderFactory.xml(xmlContent);
    }

    @Override
    public Redirect redirect() {
        return DefaultRenderFactory.redirect();
    }

    @Override
    public Redirect redirect(String targetUrl) {
        return DefaultRenderFactory.redirect(targetUrl);
    }

    @Override
    public com.georgeinfo.rapidmvc.result.Free Free() {
        return DefaultRenderFactory.Free();
    }

    @Override
    public com.georgeinfo.rapidmvc.result.Free Free(FreeRenderProcessor freeRenderProcessor) {
        return DefaultRenderFactory.Free(freeRenderProcessor);
    }

    public String getContextPath() {
        return getRequest().getContextPath();
    }

    public void out(String str) {
        PrintWriter out = null;
        try {
            getResponse().setContentType("text/html;charset=UTF-8");
            out = this.getResponse().getWriter();
            out.flush();
            out.print(str);
            out.close();
        } catch (IOException ex) {

        } finally {
            out.close();
        }

    }

}
