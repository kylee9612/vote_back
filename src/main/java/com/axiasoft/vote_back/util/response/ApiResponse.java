package com.axiasoft.vote_back.util.response;

import lombok.Data;

import java.io.Serializable;

@Data
public class ApiResponse<T> implements Serializable {
    private Boolean success;
    private String message;
    private Integer code;
    private T data;


    public ApiResponse(ErrorCode errorCode, T data){
        this.success = false;
        this.message = errorCode.getMsg();
        this.code = errorCode.getCode();
        this.data = data;
    }
    @Override
    public String toString() {
        if(this.data != null){
            return "{ \"success\":" + this.success + "," +
                    "\"message\":\"" + this.message + "\"," +
                    "\"code\":" + this.code + "," +
                    "\"data\":" + this.data + "}";
        }
        else {
            return "{ \"success\":" + this.success + "," +
                    "\"message\":\"" + this.message + "\"," +
                    "\"code\":" + this.code + "}";
        }
    }
}
