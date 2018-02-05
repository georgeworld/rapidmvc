/*
 * Author : George <GeorgeNiceWorld@gmail.com> | <Georgeinfo@163.com>
 * Copyright (C) George (http://www.georgeinfo.com), All Rights Reserved.
 */
package com.georgeinfo.rapidmvc;

import javax.servlet.http.HttpServletRequest;

/**
 * request请求封装对象
 *
 * @author George <Georgeinfo@163.com>
 */
public class RequestPath {

    private String uri;
    private String queryString;

    public RequestPath() {
    }

    public RequestPath(HttpServletRequest request) {
        uri = request.getRequestURI();
        queryString = request.getQueryString();
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    public String getUriWithQueryString() {
        return uri + (queryString != null && !queryString.trim().isEmpty() ? "?" + queryString : "");
    }
}
