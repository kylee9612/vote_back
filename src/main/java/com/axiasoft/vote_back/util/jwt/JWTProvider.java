package com.axiasoft.vote_back.util.jwt;

import com.axiasoft.vote_back.util.cipher.SEED_KISA;
import com.axiasoft.vote_back.util.response.UserErrorCode;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
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
    private static final String SECOND_LOGIN_TYPE = "second_login";

    private static final String USER_NAME = "user_name";
    private static final String USER_PHONE = "user_phone";
    private static final String USER_BIRTHDAY = "user_birthday";

    public String createUserAccessToken(Map<String, Object> jwtMap) {
        return generateToken(jwtMap, ACCESS_TYPE, userAccessTokenExpiration);
    }

    public String createUserLoginAccessToken(Map<String, Object> jwtMap) {
        return generateToken(jwtMap, ACCESS_TYPE, userAccessTokenExpiration);
    }

    public String createAmlRefreshToken(Map<String, Object> jwtMap) {
        return generateToken(jwtMap, REFRESH_TYPE, userRefreshTokenExpiration);
    }

    public String createUserConfirmedAccessToken(Map<String, Object> jwtMap) {
        return generateToken(jwtMap, ACCESS_TYPE, userAccessTokenExpiration);
    }

    public String createUserConfirmedRefreshToken(Map<String, Object> jwtMap) {
        return generateToken(jwtMap, REFRESH_TYPE, userRefreshTokenExpiration);
    }

    private String generateToken(Map<String, Object> jwtMap, String jwtType, int tokenExpirationInMinute) {

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + (tokenExpirationInMinute * 1000 * 60));
        HashMap claims = new HashMap<>();
        claims.put(JWT_TYPE,jwtType);
        claims.put(USER_NAME,jwtMap.get(USER_NAME));
        claims.put(USER_PHONE,jwtMap.get(USER_PHONE));
        claims.put(USER_BIRTHDAY, jwtMap.get(USER_BIRTHDAY));
        claims.put(SECOND_LOGIN_TYPE,jwtMap.get(SECOND_LOGIN_TYPE));

        // 암호화 위치
        String jwtToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
        String token = new String(SEED_KISA.ENCRYPT(jwtToken.getBytes()));
        token = token.replaceAll(System.getProperty("line.separator"), "");

        return token;
    }

    public JwtTokenValidation validateToken(String authToken) {
        JwtTokenValidation jwtTokenValidation = new JwtTokenValidation();
        jwtTokenValidation.setSuccess(false);
        jwtTokenValidation.setCode(UserErrorCode.CODE_1103);

        if(authToken == null){
            jwtTokenValidation.setCode(UserErrorCode.CODE_1104);
            return jwtTokenValidation;
        }

        try {

            Jws<Claims> claims= Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            jwtTokenValidation.setSuccess(true);
            jwtTokenValidation.setCode(null);

            if(claims.getBody().get(JWT_TYPE).toString().equalsIgnoreCase(ACCESS_TYPE)){
                jwtTokenValidation.setAccessToken(true);
            }else if(claims.getBody().get(JWT_TYPE).toString().equalsIgnoreCase(REFRESH_TYPE)){
                jwtTokenValidation.setRefreshToken(true);
            }

            if(claims.getBody().get(SECOND_LOGIN_TYPE).toString().equalsIgnoreCase("N")) {
                jwtTokenValidation.setCode(UserErrorCode.CODE_9994);
                jwtTokenValidation.setSuccess(false);
                return jwtTokenValidation;
            }

            return jwtTokenValidation;

        } catch (SignatureException ex) {
            log.error("Invalid JWT signature\r\n");
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token\r\n");
        } catch (ExpiredJwtException ex) {
            jwtTokenValidation.setCode(UserErrorCode.CODE_1101);
            log.error("Expired JWT token\r\n");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token\r\n");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty.\r\n");
        }

        return jwtTokenValidation;
    }

    public Map<String, Object> getJwtInfoRefresh(String token) {
        try {
            Jws<Claims> claims= Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            Map<String, Object> expectedMap = new HashMap<>(claims.getBody());
            return expectedMap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Map<String, Object> getJwtInfo(String token) {
        try {
            token = new String(SEED_KISA.DECRYPT(token));
            token = token.trim();
            Jws<Claims> claims= Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            Map<String, Object> expectedMap = new HashMap<>(claims.getBody());
            return expectedMap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Map<String, Object> getJwtInfo(HttpServletRequest request) {
        String bearerToken = request.getHeader("authorization");
        String token = "";
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            token = bearerToken.substring(7);
        } else {
            return null;
        }
        token = new String(SEED_KISA.DECRYPT(token));
        token = token.trim();
        try {
            Jws<Claims> claims= Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            Map<String, Object> expectedMap = new HashMap<>(claims.getBody());
            return expectedMap;
        } catch (Exception e) {
//            e.printStackTrace();
            return null;
        }
    }
}
