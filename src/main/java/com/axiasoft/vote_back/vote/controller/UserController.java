package com.axiasoft.vote_back.vote.controller;

import com.axiasoft.vote_back.util.ModelUtil;
import com.axiasoft.vote_back.util.jwt.JWTProvider;
import com.axiasoft.vote_back.util.response.ApiResponse;
import com.axiasoft.vote_back.util.response.CommonErrorCode;
import com.axiasoft.vote_back.vote.service.JwtService;
import com.axiasoft.vote_back.vote.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private Environment env;
    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private JWTProvider jwtTokenProvider;

    private final static Logger log = LogManager.getLogger(UserController.class);

    @GetMapping("/visitToken")
    public ResponseEntity<?> createVisitToken(HttpServletRequest request){
        ApiResponse apiResponse = jwtService.createVisitToken();
        return ResponseEntity.ok(new ApiResponse(CommonErrorCode.CODE_0000));
    }

    @ManagedOperation(description = "JWT Access Token 재발행")
    @GetMapping("/getAmlAccessToken")
    public ResponseEntity<?> refreshAmlAccessToken(HttpServletRequest request) {
        log.info("=============================START========================================== in " + request.getRequestURL());

        try {
            ApiResponse apiResponse = jwtService.getNewAccessToken(request);
            if (!apiResponse.getSuccess()) {
                return ResponseEntity.ok(apiResponse);
            }
            Map<String, Object> resultMap = ModelUtil.toMap(apiResponse.getData());

            Map<String, Object> accessJwt = jwtTokenProvider.getJwtInfo(resultMap.get("accessToken") + "");

            resultMap.put("user_name", accessJwt.get("user_name"));
            resultMap.put("user_phone", accessJwt.get("user_phone"));
            resultMap.put("user_birthday", accessJwt.get("user_birthday"));

            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            log.info("RESTORE AML ACCESS TOKEN ERROR ::: " + e.toString());
            return ResponseEntity.ok(new ApiResponse(CommonErrorCode.CODE_9999));
        }
    }

    @ManagedOperation(description = "JWT Refresh Token 발행")
    @GetMapping("/createAmlRefreshToken")
    public ResponseEntity<?> createAmlRefreshToken(HttpServletRequest request) {
        log.info("=============================START========================================== in " + request.getRequestURL());

        try {
            log.debug("================ START CREATE AML REFRESH TOKEN ===================");

            // 1. Token 검증
            Map<String, Object> jwtMap = jwtTokenProvider.getJwtInfo(request);
            if (null == jwtMap) {
                return ResponseEntity.ok(new ApiResponse(CommonErrorCode.CODE_9997));
            }

            // 2. Refresh Token 발행
            Map<String, Object> map = new HashMap<>();
            map.put("user_name", jwtMap.get("user_name"));
            map.put("user_phone", jwtMap.get("user_phone"));
            map.put("user_birthday", jwtMap.get("user_birthday"));
            ApiResponse apiResponse = jwtService.createAmlRefreshToken(request, map);

            // 3. Refresh Token DB insert & update
            Map<String, Object> apiResponseMap = ModelUtil.toMap(apiResponse.getData());
//            apiResponseMap.put("mb_idx", jwtMap.get("mb_idx"));
            jwtService.insertAmlJwtToken(apiResponseMap, request);

            log.debug("================ END CREATE AML REFRESH TOKEN ===================");
            return ResponseEntity.ok(new ApiResponse( CommonErrorCode.CODE_0000,apiResponse.getData()));
        } catch (Exception e) {
            log.error(e);
            return ResponseEntity.ok(new ApiResponse(CommonErrorCode.CODE_9999));
        }
    }
    @ManagedOperation(description = "SCI 본인 인증 호출.")
    @PostMapping("/securityInfo")
    public ResponseEntity<?> securityInfo(@RequestParam Map<String, Object> paramMap, HttpServletRequest request, HttpServletResponse response) throws Throwable {
        log.debug("=============================START= in " + request.getRequestURL());
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        try {
            Map<String, Object> jwtMap = jwtTokenProvider.getJwtInfo(request);
            if (null == jwtMap) {
                return ResponseEntity.ok(new ApiResponse(CommonErrorCode.CODE_9997));
            }

            paramMap.put("user_name", jwtMap.get("user_name"));
            paramMap.put("user_phone", jwtMap.get("user_phone"));
            paramMap.put("user_birthday", jwtMap.get("user_birthday"));

            String setParam = "?mb_idx=" + jwtMap.get("mb_idx") + "&mb_id=" + jwtMap.get("mb_id");
            Map<String, Object> rtnParamMap = userService.identifiCationSCI(paramMap, response, request
                    , (String) env.getProperty("siren.id"), (String) env.getProperty("siren.phone.srv_no_aml")
                    , (String) env.getProperty("siren.phone.ret_aml_url") + setParam, "/api/v1/aml_member/open/securityProc");

            rtnMap.put("reqInfo", rtnParamMap.get("reqInfo"));
            rtnMap.put("retUrl", rtnParamMap.get("retUrl"));
            return ResponseEntity.ok(new ApiResponse(CommonErrorCode.CODE_0000,rtnMap));
        } catch (Exception e) {
            log.error(e);
            return ResponseEntity.ok(new ApiResponse(CommonErrorCode.CODE_9999));
        }
    }

    @ManagedOperation(description = "SCI 본인 인증 호출 모바일.")
    @PostMapping("/securityInfoMobile")
    public ResponseEntity<?> securityInfoMobile(HttpServletRequest request, HttpServletResponse response) throws Throwable {
        log.debug("=============================START= in " + request.getRequestURL());
        Map<String, Object> rtnMap = new HashMap<String, Object>();
        Map<String, Object> paramMap = new HashMap<String, Object>();
        try {
            Map<String, Object> jwtMap = jwtTokenProvider.getJwtInfo(request);
            if (null == jwtMap) {
                return ResponseEntity.ok(new ApiResponse(CommonErrorCode.CODE_9997));
            }
            paramMap.put("user_name", jwtMap.get("user_name"));
            paramMap.put("user_phone", jwtMap.get("user_phone"));
            paramMap.put("user_birthday", jwtMap.get("user_birthday"));

            String setParam = "?mb_idx=" + jwtMap.get("mb_idx") + "&mb_id=" + jwtMap.get("mb_id");
            Map<String, Object> rtnParamMap = userService.identifiCationSCI(paramMap, response, request
                    , (String) env.getProperty("siren.id"), (String) env.getProperty("siren.phone.srv_no_aml")
                    , (String) env.getProperty("siren.phone.ret_aml_url") + setParam, "/api/v1/aml_member/open/securityProc");

//            log.info(rtnParamMap.get("reqInfo"));
//            log.info(rtnParamMap.toString());
            rtnMap.put("reqInfo", rtnParamMap.get("reqInfo"));
            rtnMap.put("retUrl", rtnParamMap.get("retUrl"));
            log.info("(모바일) 본인인증호출성공 :::: " + setParam);
            return ResponseEntity.ok(new ApiResponse(CommonErrorCode.CODE_0000,rtnMap));
        } catch (Exception e) {
            log.error(e);
            return ResponseEntity.ok(new ApiResponse(CommonErrorCode.CODE_9999));
        }
    }

    @PostMapping("/login")
    public ResponseEntity loginUser(@RequestBody Map<String, Object> map){


        return ResponseEntity.ok(new ApiResponse(CommonErrorCode.CODE_0000,""));
    }
}
