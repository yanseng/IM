package com.example.yang.testbyyang.models;

/**
 * Created by yang on 2016/2/26.
 */
import java.io.Serializable;

public class Status implements Serializable {

    /**
     * 返回码
     */
    private int code;

    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
