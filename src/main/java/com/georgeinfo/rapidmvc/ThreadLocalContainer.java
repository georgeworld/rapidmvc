/*
 * Author : George <GeorgeNiceWorld@gmail.com> | <Georgeinfo@163.com>
 * Copyright (C) George (http://www.georgeinfo.com), All Rights Reserved.
 */
package com.georgeinfo.rapidmvc;

import com.georgeinfo.rapidmvc.support.AcHelper;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 仅限myginkgo框架本身内部调用!
 *
 * @author George <GeorgeNiceWorld@gmail.com>
 *
 */
public class ThreadLocalContainer {

    /**
     * 存放当前线程所处理的Request请求对象
     */
    private final static ThreadLocal<HttpServletRequest> currentRequests = new ThreadLocal<HttpServletRequest>();
    /**
     * 存放当前线程所处理的Response请求对象
     */
    private final static ThreadLocal<HttpServletResponse> currentResponses = new ThreadLocal<HttpServletResponse>();
    /**
     * 存放当前线程所处理的AcHelper请求对象
     */
    private final static ThreadLocal<AcHelper> currentAcHelper = new ThreadLocal<AcHelper>();

    public static void unindRequestFromCurrentThread() {
        currentRequests.remove();
    }

    public static void bindRequestToCurrentThread(HttpServletRequest request) {
        if (request != null) {
            currentRequests.set(request);
        } else {
            unindRequestFromCurrentThread();
        }
    }

    public static HttpServletRequest getCurrentThreadRequest() {
        return currentRequests.get();
    }

    public static void unindResponseFromCurrentThread() {
        currentResponses.remove();
    }

    public static void bindResponseToCurrentThread(HttpServletResponse response) {
        if (response != null) {
            currentResponses.set(response);
        } else {
            unindResponseFromCurrentThread();
        }
    }

    public static HttpServletResponse getCurrentThreadResponse() {
        return currentResponses.get();
    }

    public static void unindAcHelperFromCurrentThread() {
        currentAcHelper.remove();
    }

    public static void bindAcHelperToCurrentThread(AcHelper acHelper) {
        if (acHelper != null) {
            currentAcHelper.set(acHelper);
        } else {
            unindAcHelperFromCurrentThread();
        }
    }

    public static AcHelper getCurrentThreadAcHelper() {
        return currentAcHelper.get();
    }
}
