package com.tqh.demo.exception;

public class DataException extends RuntimeException {
    private String msg;
    private int code;

    public DataException(int code,String msg){
        super(msg);
        this.code=code;
        this.msg=msg;

    }
    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
