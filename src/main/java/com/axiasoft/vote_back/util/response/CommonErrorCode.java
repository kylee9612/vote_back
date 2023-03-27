package com.axiasoft.vote_back.util.response;

public enum CommonErrorCode implements ErrorCode{
    CODE_0000(0, "success")
    ,CODE_500(500, "internal error");


    private final Integer code;
    private final String msg;

    CommonErrorCode(Integer code, String msg){
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
