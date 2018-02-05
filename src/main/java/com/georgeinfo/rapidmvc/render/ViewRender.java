/*
 * Author : George <GeorgeNiceWorld@gmail.com> | <Georgeinfo@163.com>
 * Copyright (C) George (http://www.georgeinfo.com), All Rights Reserved.
 */
package com.georgeinfo.rapidmvc.render;

import com.georgeinfo.base.util.logger.GeorgeLogger;
import com.georgeinfo.rapidmvc.Constants;
import com.georgeinfo.rapidmvc.exception.MissingViewNameException;
import com.georgeinfo.rapidmvc.result.View;
import gbt.config.GeorgeLoggerFactory;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 视图渲染器
 *
 * @author George <Georgeinfo@163.com>
 */
public class ViewRender implements Render {

    private static final GeorgeLogger logger = GeorgeLoggerFactory.getLogger(ViewRender.class);
    private View view;

    public ViewRender() {
    }

    public ViewRender(View view) {
        this.view = view;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    @Override
    public boolean doRender(HttpServletRequest reqeust, HttpServletResponse response) {
        String viewName = view.getViewName();
        if (viewName != null && !viewName.isEmpty()) {
            try {
                reqeust.getRequestDispatcher(Constants.viewsDir + "/" + viewName).forward(reqeust, response);
                return true;
            } catch (ServletException ex) {
                logger.error(ex);
            } catch (IOException ex) {
                logger.error(ex);
            }

            return false;
        } else {
            throw new MissingViewNameException("## View name can't be null or empty.");
        }
    }

}
