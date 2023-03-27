package com.axiasoft.vote_back.util.response;

public enum UserErrorCode implements ErrorCode{
    //  JWT Token
    CODE_1101(1101, "[1101] 토큰이 만료되었습니다.")
    ,CODE_1103(1103, "[1103] 잘못된 토큰입니다.")
    ,CODE_1104(1104, "[1104] 로그인이 필요합니다.")
    ,CODE_1105(1105, "[1105] 리프레시 토큰이 아닙니다.")
    ,CODE_1106(1106, "[1106] 토큰값이 맞지 않습니다.")
    ,CODE_1107(1107, "[1107] 억세스 토큰이 아닙니다.")
    ,CODE_1108(1108, "[1108] 토큰값을 찾을 수 없습니다.")

    //  Login
    ,CODE_9994(9994, "[9994] 2차 로그인이 필요합니다.")
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
