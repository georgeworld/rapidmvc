/*
 * Author : George <GeorgeNiceWorld@gmail.com> | <Georgeinfo@163.com>
 * Copyright (C) George (http://www.georgeinfo.com), All Rights Reserved.
 */
package com.georgeinfo.rapidmvc;

import com.georgeinfo.rapidmvc.api.LoginInterceptor;
import com.georgeinfo.rapidmvc.builtin.impl.DefaultWebActionImpl;
import com.georgeinfo.rapidmvc.api.WebAction;
import com.georgeinfo.rapidmvc.api.PathTree;
import com.georgeinfo.base.util.logger.GeorgeLogger;
import com.georgeinfo.base.util.reflect.ReflectionTool;
import com.georgeinfo.rapidmvc.api.MainFactory;
import com.georgeinfo.rapidmvc.builtin.impl.DefaultMainFactoryImpl;
import gbt.config.GeorgeLoggerFactory;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author George <Georgeinfo@163.com>
 */
public class FrontFilter implements Filter {

    private static final GeorgeLogger logger = GeorgeLoggerFactory.getLogger(FrontFilter.class);
    private static final boolean debug = true;

    // The filter configuration object we are associated with.  If
    // this value is null, this filter instance is not currently
    // configured. 
    protected FilterConfig filterConfig = null;
    /**
     * 主工厂类，所有的接口实现类，都在这个工厂实现类中定义，代替使用DI依赖注入
     */
    protected final MainFactory mainFactory = new DefaultMainFactoryImpl();
    /**
     * 路径树，整个Rapid MVC的运行环境中，应该只存在一个PathTree实例
     */
    protected PathTree pathTree;
    protected WebAction action;

    public FrontFilter() {
    }

    /**
     * Init method for this filter
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
        //扫描控制器类，注册mvc path树
        //pathTree =  new DefaultPathTreeImpl(new DefaultResourceLoaderImpl()).init(); //如果是Spring前置过滤器，则这里应该使用SpringResourceLoaderImpl实例
        pathTree = mainFactory.getPathTree(mainFactory.getResourceLoader()).init();

        //初始化WebAction响应处理器
        String dispatcherClassName = filterConfig.getInitParameter("dispatcherClassName");
        if (dispatcherClassName != null && !dispatcherClassName.trim().isEmpty()) {
            action = ReflectionTool.getInstanceFromFullClassName(dispatcherClassName);
        } else {
            action = new DefaultWebActionImpl(pathTree);
        }

        //初始化权限拦截器
        String loginInterceptorClassName = filterConfig.getInitParameter("loginInterceptorClassName");
        if (loginInterceptorClassName != null && !loginInterceptorClassName.trim().isEmpty()) {
            LoginInterceptor li = ReflectionTool.getInstanceFromFullClassName(loginInterceptorClassName);
            if (li != null) {
                action.setLi(li);
            }
        }

        if (debug) {
            log("FrontFilter:Initializing filter");
        }
    }

    private boolean doProcessing(ServletRequest request, ServletResponse response)
            throws IOException, ServletException {
        if (debug) {
            log("FrontFilter:DoProcessing");
        }

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        //RequestPath path = new RequestPath(httpRequest);

        //开始解析URI
        ResultWrapper result = action.doProcess(httpRequest, httpResponse);
        if (!result.isSuccess()) {
            logger.error("## The page you requested can't be processed[" + result.getRequestPath().getUriWithQueryString() + "]");
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
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     * @param chain The filter chain we are processing
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet error occurs
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {
        if (debug) {
            log("FrontFilter:doFilter()");
        }

        doProcessing(request, response);
    }

    /**
     * Return the filter configuration object for this filter.
     *
     * @return
     */
    public FilterConfig getFilterConfig() {
        return (this.filterConfig);
    }

    /**
     * Set the filter configuration object for this filter.
     *
     * @param filterConfig The filter configuration object
     */
    public void setFilterConfig(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

    /**
     * Destroy method for this filter
     */
    public void destroy() {

    }

    /**
     * Return a String representation of this object.
     */
    @Override
    public String toString() {
        if (filterConfig == null) {
            return ("FrontFilter()");
        }
        StringBuffer sb = new StringBuffer("FrontFilter(");
        sb.append(filterConfig);
        sb.append(")");
        return (sb.toString());
    }

    private void sendProcessingError(Throwable t, ServletResponse response) {
        String stackTrace = getStackTrace(t);

        if (stackTrace != null && !stackTrace.equals("")) {
            try {
                response.setContentType("text/html");
                PrintStream ps = new PrintStream(response.getOutputStream());
                PrintWriter pw = new PrintWriter(ps);
                pw.print("<html>\n<head>\n<title>Error</title>\n</head>\n<body>\n"); //NOI18N

                // PENDING! Localize this for next official release
                pw.print("<h1>The resource did not process correctly</h1>\n<pre>\n");
                pw.print(stackTrace);
                pw.print("</pre></body>\n</html>"); //NOI18N
                pw.close();
                ps.close();
                response.getOutputStream().close();
            } catch (Exception ex) {
            }
        } else {
            try {
                PrintStream ps = new PrintStream(response.getOutputStream());
                t.printStackTrace(ps);
                ps.close();
                response.getOutputStream().close();
            } catch (Exception ex) {
            }
        }
    }

    public static String getStackTrace(Throwable t) {
        String stackTrace = null;
        try {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            t.printStackTrace(pw);
            pw.close();
            sw.close();
            stackTrace = sw.getBuffer().toString();
        } catch (Exception ex) {
        }
        return stackTrace;
    }

    public void log(String msg) {
        filterConfig.getServletContext().log(msg);
    }

}
