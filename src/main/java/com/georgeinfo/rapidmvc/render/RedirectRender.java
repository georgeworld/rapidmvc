/*
 * Author : George <GeorgeNiceWorld@gmail.com> | <Georgeinfo@163.com>
 * Copyright (C) George (http://www.georgeinfo.com), All Rights Reserved.
 */
package com.georgeinfo.rapidmvc.render;

import com.georgeinfo.base.util.logger.GeorgeLogger;
import com.georgeinfo.rapidmvc.result.Redirect;
import gbt.config.GeorgeLoggerFactory;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 跳转渲染器
 *
 * @author George <Georgeinfo@163.com>
 */
public class RedirectRender implements Render {

    private static final GeorgeLogger logger = GeorgeLoggerFactory.getLogger(RedirectRender.class);
    private Redirect redirect;

    public RedirectRender() {
    }

    public RedirectRender(Redirect redirect) {
        this.redirect = redirect;
    }

    public Redirect getRedirect() {
        return redirect;
    }

    public void setRedirect(Redirect redirect) {
        this.redirect = redirect;
    }

    @Override
    public boolean doRender(HttpServletRequest reqeust, HttpServletResponse response) {
        try {
            response.sendRedirect(redirect.getTargetUrl());
            return true;
        } catch (IOException ex) {
            logger.error("## IOException when send redirect to url:" + redirect.getTargetUrl(), ex);
            return false;
        }
    }

}
