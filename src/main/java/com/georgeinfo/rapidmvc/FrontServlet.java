/*
 * Author: George <GeorgeNiceWorld@gmail.com> | <Georgeinfo@163.com>
 * Copyright (C) George (www.georgeinfo.com), All Rights Reserved.
 */
package com.georgeinfo.rapidmvc;

import com.georgeinfo.base.util.logger.GeorgeLogger;
import com.georgeinfo.base.util.reflect.ReflectionTool;
import com.georgeinfo.rapidmvc.api.LoginInterceptor;
import com.georgeinfo.rapidmvc.api.MainFactory;
import com.georgeinfo.rapidmvc.api.PathTree;
import com.georgeinfo.rapidmvc.api.WebAction;
import com.georgeinfo.rapidmvc.builtin.impl.DefaultMainFactoryImpl;
import com.georgeinfo.rapidmvc.builtin.impl.DefaultWebActionImpl;
import gbt.config.GeorgeLoggerFactory;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author George <Georgeinfo@163.com>
 */
public class FrontServlet extends HttpServlet {

    private static final GeorgeLogger logger = GeorgeLoggerFactory.getLogger(FrontServlet.class);
    protected static final boolean debug = true;

    protected ServletConfig config;
    /**
     * 主工厂类，所有的接口实现类，都在这个工厂实现类中定义，代替使用DI依赖注入
     */
    protected final MainFactory mainFactory = new DefaultMainFactoryImpl();
    /**
     * 路径树，整个Rapid MVC的运行环境中，应该只存在一个PathTree实例
     */
    protected PathTree pathTree;
    /**
     * web响应处理对象
     */
    protected WebAction action;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        this.config = config;
        //扫描控制器类，注册mvc path树
        //pathTree =  new DefaultPathTreeImpl(new DefaultResourceLoaderImpl()).init(); //如果是Spring前置过滤器，则这里应该使用SpringResourceLoaderImpl实例
        pathTree = mainFactory.getPathTree(mainFactory.getResourceLoader()).init();

        //初始化WebAction响应处理器
        String dispatcherClassName = config.getInitParameter("dispatcherClassName");
        if (dispatcherClassName != null && !dispatcherClassName.trim().isEmpty()) {
            action = ReflectionTool.getInstanceFromFullClassName(dispatcherClassName);
        } else {
            action = new DefaultWebActionImpl(pathTree);
        }

        //初始化权限拦截器
        String loginInterceptorClassName = config.getInitParameter("loginInterceptorClassName");
        if (loginInterceptorClassName != null && !loginInterceptorClassName.trim().isEmpty()) {
            LoginInterceptor li = ReflectionTool.getInstanceFromFullClassName(loginInterceptorClassName);
            if (li != null) {
                action.setLi(li);
            }
        }

        if (debug) {
            log("FrontServlet:Initializing filter");
        }

    }

    private boolean doProcessing(ServletRequest request, ServletResponse response)
            throws IOException, ServletException {
        if (debug) {
            log("FrontServlet:DoProcessing");
        }

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        //RequestPath path = new RequestPath(httpRequest);

        //开始解析URI
        ResultWrapper result = action.doProcess(httpRequest, httpResponse);
        if (!result.isSuccess()) {
            logger.error("## The page you requested can't be processed[" + result.getRequestPath().getUriWithQueryString() + "]");
            logger.error("### Error info:"+result.getMessage());
//            httpRequest.setAttribute("requestedUrl", path.getUriWithQueryString());
//            httpRequest.getRequestDispatcher("/WEB-INF/sysview/not-found.jsp").forward(request, response);

            if (result.isException()) {
                httpResponse.setStatus(500);
                PrintWriter writer = httpResponse.getWriter();
                if (result.getMessage() != null) {
                    writer.write(result.getMessage());
                } else {
                    writer.write("Rapid MVC front filter ERROR");
                }
                writer.flush();
                writer.close();
            }

            return false;
        } else {
            return true;
        }
    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (debug) {
            log("FrontServlet:doFilter()");
        }

        doProcessing(request, response);
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
