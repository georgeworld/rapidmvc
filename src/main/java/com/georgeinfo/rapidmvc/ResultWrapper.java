/*
 * Author : George <GeorgeNiceWorld@gmail.com> | <Georgeinfo@163.com>
 * Copyright (C) George (http://www.georgeinfo.com), All Rights Reserved.
 */
package com.georgeinfo.rapidmvc;

/**
 * 执行结果包装器
 *
 * @author George <Georgeinfo@163.com>
 * @param <T> 返回值类型
 */
public class ResultWrapper<T> { 

    public static enum ResultEnum {

        success, error, exception, empty
    };
    private ResultEnum result;
    private String message;
    private T returnValue;
    private RequestPath requestPath;

    public ResultWrapper() {
    }

    public ResultWrapper(ResultEnum result, String message) {
        this.result = result;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public ResultWrapper<T> setMessage(String message) {
        this.message = message;
        return this;
    }

    public boolean isSuccess() {
        return this.result == ResultEnum.success;
    }

    public ResultWrapper<T> success() {
        this.result = ResultEnum.success;
        return this;
    }

    public ResultWrapper<T> error() {
        this.result = ResultEnum.error;
        return this;
    }

    public boolean isError() {
        return this.result == ResultEnum.error;
    }

    public ResultWrapper<T> exception() {
        this.result = ResultEnum.exception;
        return this;
    }

    public boolean isException() {
        return this.result == ResultEnum.exception;
    }

    public ResultWrapper<T> empty() {
        this.result = ResultEnum.empty;
        return this;
    }

    public boolean isEmpty() {
        return this.result == ResultEnum.empty;
    }

    public T getReturnValue() {
        return returnValue;
    }

    public ResultWrapper<T> setReturnValue(T returnValue) {
        this.returnValue = returnValue;
        return this;
    }

    public RequestPath getRequestPath() {
        return requestPath;
    }

    public void setRequestPath(RequestPath requestPath) {
        this.requestPath = requestPath;
    }

}
