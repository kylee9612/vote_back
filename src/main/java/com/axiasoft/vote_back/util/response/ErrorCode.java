package com.axiasoft.vote_back.util.response;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public interface ErrorCode {
    public Integer getCode();

    public String getMsg();
}