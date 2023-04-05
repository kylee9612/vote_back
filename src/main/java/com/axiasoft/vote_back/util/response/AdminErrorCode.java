package com.axiasoft.vote_back.util.response;

public enum AdminErrorCode implements ErrorCode{
    ADMIN_5001(5001,"필수값이 입력되지 않았습니다.<br>빈칸이 없는지 확인해주세요.")
    ;
    private final Integer code;
    private final String msg;

    AdminErrorCode(Integer code, String msg){
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
