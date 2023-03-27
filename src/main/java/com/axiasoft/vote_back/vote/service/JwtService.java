package com.axiasoft.vote_back.vote.service;

import com.axiasoft.vote_back.util.StringUtil;
import com.axiasoft.vote_back.util.cipher.SEED_KISA;
import com.axiasoft.vote_back.util.jwt.JWTProvider;
import com.axiasoft.vote_back.util.jwt.JwtAuthenticationResponse;
import com.axiasoft.vote_back.util.jwt.JwtTokenValidation;
import com.axiasoft.vote_back.util.response.ApiResponse;
import com.axiasoft.vote_back.util.response.CommonErrorCode;
import com.axiasoft.vote_back.vote.dao.JwtDAO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {
    @Value("${jwt.user.refreshToken.expirationInMinute}")
    private int userRefreshTokenExpiration;
    @Autowired
    private JwtDAO jwtDAO;
    @Autowired
    private JWTProvider jwtTokenProvider;

    public ApiResponse getNewAccessToken(HttpServletRequest request) throws Exception {
        String jwt = getJwtFromRequest(request);
        String jwt2 = getJwtFromRequest(request);

        jwt = new String(SEED_KISA.DECRYPT(jwt));
        jwt = jwt.trim();

        JwtTokenValidation jwtTokenValidation = jwtTokenProvider.validateToken(jwt);
        if (jwtTokenValidation.isSuccess()) {
            if (jwtTokenValidation.isRefreshToken()) {
                Map<String, Object> tmpJwtMap = jwtTokenProvider.getJwtInfo(request);

                Map<String, Object> map = new HashMap<>();
                String agent = request.getHeader("User-Agent");
                map.put("agent_type", StringUtil.getAgentType(agent));

                map.put("mb_idx", tmpJwtMap.get("mb_idx"));
                map.put("token_type", "T_REFRESH");
                map.put("expiration_in_minute", userRefreshTokenExpiration);
                map.put("token", jwt2);
                Map<String, Object> dbToken = jwtDAO.dbValidateToken(map);
                if(dbToken == null) {
                    return new ApiResponse(CommonErrorCode.CODE_1101);
                }
                Map<String, Object> jwtMap = jwtTokenProvider.getJwtInfoRefresh(jwt);

                String generatedJwt = jwtTokenProvider.createUserAccessToken(jwtMap);
                return new ApiResponse(CommonErrorCode.CODE_0000, new JwtAuthenticationResponse(generatedJwt));
            } else {
                return new ApiResponse(CommonErrorCode.CODE_1105);
            }
        } else {
            return new ApiResponse(jwtTokenValidation.getCode());
        }
    }

    @Transactional
    public int insertAmlJwtToken(Map<String, Object> map, HttpServletRequest request) throws Exception {
        int result = 0;
        Map<String, Object> insertMap = new HashMap<>();
        insertMap.put("mb_idx", map.get("mb_idx"));

        String agent = request.getHeader("User-Agent");
        insertMap.put("agent_type", StringUtil.getAgentType(agent));

        // AML Refresh Token 신규 발행
        if (map.get("refreshToken") != null) {
            // 기존 AML Refresh Token 'N' 처리
            jwtDAO.updateAmlJwtToken(insertMap);

            // 새로운 AML Refresh Token DB insert
            insertMap.put("token_type", "T_REFRESH");
            insertMap.put("token_value", map.get("refreshToken"));
            result = jwtDAO.insertJwtToken(insertMap);
        }
        return result;
    }

    public ApiResponse createAmlRefreshToken(HttpServletRequest request, Map<String, Object> jwtMap) {
        String AmlRefreshToken = jwtTokenProvider.createAmlRefreshToken(jwtMap); // AML 회원가입 aml_refresh_token
        return new ApiResponse(CommonErrorCode.CODE_0000, new JwtAuthenticationResponse(null, AmlRefreshToken));
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }
}
