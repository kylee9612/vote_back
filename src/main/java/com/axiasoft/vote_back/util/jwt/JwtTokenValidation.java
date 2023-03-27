package com.axiasoft.vote_back.util.jwt;

import com.axiasoft.vote_back.util.response.ErrorCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtTokenValidation {
    private boolean success;
    private ErrorCode code;
    private boolean accessToken = false;
    private boolean refreshToken = false;
}
