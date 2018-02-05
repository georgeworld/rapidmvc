/*
 * Author : George <GeorgeNiceWorld@gmail.com> | <Georgeinfo@163.com>
 * Copyright (C) George (http://www.georgeinfo.com), All Rights Reserved.
 */
package com.georgeinfo.rapidmvc.httpcycle;

import com.georgeinfo.rapidmvc.support.AcHelper;
import java.util.Enumeration;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.servlet.http.HttpSession;

/**
 * @author George <GeorgeNiceWorld@gmail.com>
 */
public interface ActionContext {

    /**
     * 返回本次调用的 {@link HttpServletRequest}对象
     *
     * @return
     */
    public HttpServletRequest getRequest();

    /**
     * 返回本次调用的 {@link HttpServletResponse}对象
     *
     * @return
     */
    public HttpServletResponse getResponse();

    /**
     * 得到AcHelper对象，可处理表单批量接受、文件上传等任务
     *
     * @return 请求上下文助手对象
     */
    public AcHelper getHelper();
    
    /**
     * 得到HttpSession对象
     *
     * @return
     */
    public HttpSession getSession();

    /**
     * 得到ServletContext对象
     *
     * @return
     */
    public ServletContext getServletContext();

    /**
     * 获取在请求查询串(即问号后的xx=yyy)、POST中Body中、URI所带的参数值。
     * <p>
     * URI中的参数需要通过在控制器方法中通过类似@Path("user_{name}")进行声明，才可以获取name的参数<br>
     * 同时因为 myginkgo 对 {@link HttpServletRequest}
     * 进行了封装，使得其request的getParameter和{@link ActionContext}的getParameter的语义相同。
     *
     * @param name 参数名字
     * @return 参数值
     */
    public String getParameter(String name);

    public Map<String, String[]> getParameterMap();

    public Enumeration<String> getParameterNames();

    /**
     * 设置一个和本次调用关联的属性。这个属性可以在多个拦截器中共享。
     * <p>
     * 因为所设置的属性值和本次调用有关，所以他与 {@link #getRequest()#setAttribute(String,
     * Object)}是不相同的。
     *
     * @param name
     * @param value
     * @return
     */
    public void setAttribute(String name, Object value);

    /**
     * 获取前面拦截器或代码设置的，和本次调用相关的属性
     *
     * @param name
     * @return
     */
    public Object getAttribute(String name);

    /**
     * 删除inv的某一个属性
     *
     * @param name
     */
    public void removeAttribute(String name);

    /**
     * 返回本次调用相关的所有属性名字
     *
     * @return
     */
    public Enumeration<String> getAttributeNames();
}
