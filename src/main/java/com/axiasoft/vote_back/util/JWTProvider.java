package com.axiasoft.vote_back.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class JWTProvider {
    private static final Logger log = LogManager.getLogger(JWTProvider.class);

    @Value("${jwt.secret}")
    private String jwtSecret;
    @Value("${jwt.user.accessToken.expirationInMinute}")
    private int userAccessTokenExpiration;
    @Value("${jwt.user.refreshToken.expirationInMinute}")
    private int userRefreshTokenExpiration;

    private static final String JWT_TYPE = "jwtType";
    private static final String REFRESH_TYPE = "refresh";
    private static final String ACCESS_TYPE = "access";

//    public String createUserAccessToken(Map<String, Object> jwtMap) {
//        return generateToken(jwtMap, ACCESS_TYPE, userAccessTokenExpiration);
//    }
//
//    public String createUserLoginAccessToken(Map<String, Object> jwtMap) {
//        return generateToken(jwtMap, ACCESS_TYPE, userAccessTokenExpiration);
//    }
//
//    public String createAmlRefreshToken(Map<String, Object> jwtMap) {
//        return generateToken(jwtMap, REFRESH_TYPE, userRefreshTokenExpiration);
//    }
//
//    public String createUserConfirmdAccessToken(Map<String, Object> jwtMap) {
//        return generateToken(jwtMap, ACCESS_TYPE, userAccessTokenExpiration);
//    }
//
//    public String createUserConfirmdRefreshToken(Map<String, Object> jwtMap) {
//        return generateToken(jwtMap, REFRESH_TYPE, userRefreshTokenExpiration);
//    }
}
