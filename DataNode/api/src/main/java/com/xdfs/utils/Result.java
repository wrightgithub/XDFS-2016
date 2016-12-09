package com.xdfs.utils;

import java.io.Serializable;

/**
 * Created by xyy on 16-12-5.
 */
public class Result<T> implements Serializable {

    // 调用是否成功执行
    private boolean isSuccess = true;
    // 返回值
    private T       value;

    private String  errorMsg;

    public Result() {

    }


    public Result(boolean flag,String errorMsg) {
        this.isSuccess = flag;
        this.errorMsg = errorMsg;
    }

    public Result(T value) {
        this.value = value;
    }



    public String getErrorMsg() {
        return errorMsg;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public T getValue() {
        return value;
    }

    public void setErrorMsg(String errorMsg) {

        this.errorMsg = errorMsg;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
