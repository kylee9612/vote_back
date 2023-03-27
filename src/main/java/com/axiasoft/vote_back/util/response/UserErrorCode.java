package com.axiasoft.vote_back.util.response;

public enum UserErrorCode implements ErrorCode{

    ;
    private final Integer code;
    private final String msg;

    UserErrorCode(Integer code, String msg){
        this.code = code;
        this.msg = msg;
    }
    @Override
    public Integer getCode(){
        return code;
    }
    @Override
    public String getMsg(){
        return msg;
    }
}
