package com.axiasoft.vote_back.util.response;

public enum CommonErrorCode implements ErrorCode{
    CODE_0000(0, "success")
    ,CODE_500(500, "internal error")

    /** 토큰, login*/
    ,CODE_1101(1101, "[1101] 토큰이 만료되었습니다.")
    ,CODE_1103(1103, "[1103] 잘못된 토큰입니다.")
    ,CODE_1104(1104, "[1104] 로그인이 필요합니다.")
    ,CODE_1105(1105, "[1105] 리프레시 토큰이 아닙니다.")
    ,CODE_1106(1106, "[1106] 토큰값이 맞지 않습니다.")
    ,CODE_1107(1107, "[1107] 억세스 토큰이 아닙니다.")
    ,CODE_1108(1108, "[1108] 토큰값을 찾을 수 없습니다.")
    // notice 등록 수정 삭제 관련 1200 번대
    ,CODE_1201(1201, "[1201] 공지 사항 등록이 완료되었습니다.")
    ,CODE_1202(1202, "[1202] 공지 사항 수정이 완료되었습니다.")
    ,CODE_1203(1203, "[1203] 공지 사항 삭제가 완료되었습니다.")

    ,CODE_9997(9997, "[9997] 로그인이 필요합니다.")
    ,CODE_9999(9999, "[9999] 오류. 고객센터에 문의 바랍니다.");


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
