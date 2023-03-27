package com.axiasoft.vote_back.vote.service;

import com.axiasoft.vote_back.common.vo.IdentificationVo;
import com.axiasoft.vote_back.util.GsonUtil;
import com.axiasoft.vote_back.util.StringUtil;
import com.axiasoft.vote_back.util.cipher.SEED_KISA;
import com.axiasoft.vote_back.util.jwt.JWTProvider;
import com.axiasoft.vote_back.util.jwt.JwtAuthenticationResponse;
import com.axiasoft.vote_back.util.jwt.JwtTokenValidation;
import com.axiasoft.vote_back.util.response.ApiResponse;
import com.axiasoft.vote_back.util.response.CommonErrorCode;
import com.axiasoft.vote_back.vote.dao.UserDAO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.util.WebUtils;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class UserService {
    private final static Logger log = LogManager.getLogger(UserService.class);

    @Autowired
    private UserDAO userDAO;
    /**
     * 휴대폰 본인인증(SCI)
     *
     * @param response
     * @return
     */
    public HashMap<String, Object> identifiCationSCI(Map<String, Object> paramMap, HttpServletResponse response, HttpServletRequest request, String siren_id
            , String siren_srv_no, String siren_ret_url, String returnViewName) throws Exception {
        log.debug("---------- identifiCationSCI");

        HashMap<String, Object> map = new HashMap<>();
        HttpSession session = request.getSession();

        //휴대폰 인증
        //날짜 생성
        Calendar today = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String day = sdf.format(today.getTime());

        Random ran = new Random();
        int numLength = 6;
        String randomStr = "";

        for (int i = 0; i < numLength; i++) {
            randomStr += ran.nextInt(10);
        }

        //reqNum은 최대 40byte 까지 사용 가능
        String id = siren_id;
        String srvNo = siren_srv_no;
//        String reqNum = day + randomStr;
        String reqNum = "85729610359798";
        String exVar = "0000000000000000";                                       // 복호화용 임시필드
        String retUrl = siren_ret_url;
        String certDate = day;
        String certGb = "H";                           // 본인실명확인 본인확인 인증수단
        String addVar = "";                           // 본인실명확인 추가 파라메터

        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setHeader("Pragma", "no-store");
        response.setHeader("Cache-Control", "no-cache");

        log.debug("returnViewName : " + returnViewName);

        IdentificationVo identifiCationVo = new IdentificationVo();

        identifiCationVo.setReqNum(reqNum);
        identifiCationVo.setNotiViewName(returnViewName);

        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        String json = GsonUtil.toJsonString(identifiCationVo);

        Cookie identifiCationNotiCookie = new Cookie("identifiCationNoti", URLEncoder.encode(json, "UTF-8"));
        identifiCationNotiCookie.setPath("/");
        identifiCationNotiCookie.setMaxAge(60 * 60);
        response.addCookie(identifiCationNotiCookie);

        Cookie cookie = WebUtils.getCookie(request, "identifiCationNoti");

        // 리턴페이지에서 사용할 session parameter 사용
        session.setAttribute("notiViewName", returnViewName);
        session.setAttribute("reqNum", reqNum);
        session.setAttribute("mb_idx", paramMap.get("mb_idx"));
        session.setAttribute("mb_id", paramMap.get("mb_id"));

        log.debug("session notiViewName :" + session.getAttribute("notiViewName"));
        log.debug("identifiCationNotiCookie : " + cookie);

        //01. 암호화 모듈 선언
//        SciSecuManager seed  = new SciSecuManager();
        com.sci.v2.pccv2.secu.SciSecuManager seed = new com.sci.v2.pccv2.secu.SciSecuManager();

        //02. 1차 암호화
        String encStr = "";
        String reqInfo = id + "^" + srvNo + "^" + reqNum + "^" + certDate + "^" + certGb + "^" + addVar + "^" + exVar;  // 데이터 암호화
        seed.setInfoPublic(id, "509F446C62C30A447DC3FC710A200225"); // 회원사 ID 및 PWD 셋팅 패스워드는 16자리 필수 영문 무관
        encStr = seed.getEncPublic(reqInfo);

        //03. 위변조 검증 값 생성
//        com.sci.v2.pccv2.secu.hmac.SciHmac hmac = new com.sci.v2.pccv2.secu.hmac.SciHmac();
////        com.sci.v2.pcc2.secu.hmac.SciHmac hmac = new com.sci.v2.pcc.secu.hmac.SciHmac();
//        String hmacMsg = hmac.HMacEncriptPublic(encStr);
        com.sci.v2.pccv2.secu.hmac.SciHmac hmac = new com.sci.v2.pccv2.secu.hmac.SciHmac();
        String hmacMsg = seed.getEncReq(encStr, "HMAC");

        //03. 2차 암호화
        reqInfo = seed.getEncPublic(encStr + "^" + hmacMsg + "^" + "0000000000000000");  //2차암호화
        reqInfo = seed.EncPublic(reqInfo + "^" + id + "^" + "00000000");

        map.put("reqInfo", reqInfo);
        map.put("retUrl", retUrl);

        return map;
    }

    /**
     * @param
     * @param response
     * @param request
     * @return Map<String, Object>
     * //success			성공일경우 true, 실패일 경우 false 리턴
     * //full_name			성명
     * //birthday			생일
     * //gender			성멸
     * //phone_company		휴대폰회사
     * //phone_no			휴대전화번호
     * //msg
     */
    public Map<String, Object> identifiCationReturnSCI(HttpServletResponse response, HttpServletRequest request) {
        log.info("----------------identifiCationReturnSCI");
        log.info("----------------request");
        Map<String, Object> rtnParamMap = new HashMap<>();

        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
        HttpSession session = request.getSession();
        String viewName = String.valueOf(session.getAttribute("notiViewName"));
        String sessionReqNum = String.valueOf(session.getAttribute("reqNum"));
//        String mb_idx = String.valueOf(session.getAttribute("mb_idx"));
//        String mb_id = String.valueOf(session.getAttribute("mb_id"));
        String mb_idx = request.getParameter("mb_idx").trim();
        String mb_id = request.getParameter("mb_id").trim();
        Cookie identifiCationNotiCookie = WebUtils.getCookie(request, "identifiCationNoti");
        String param = "";

//        log.info(mb_idx  + " :: " + mb_id);
        // 변수 --------------------------------------------------------------------------------
        String retInfo = "";                                                                // 결과정보
        String id = "";                                                               //회원사 비즈사이렌아이디
        String name = "";                                                               //성명
        String sex = "";                                                                //성별
        String birYMD = "";                                                                //생년월일
        String fgnGbn = "";                                                                //내외국인 구분값
        String scCode = "";                                                                //가상식별번호
        String di = "";                                                                //DI
        String ci1 = "";                                                                //CI
        String ci2 = "";                                                                //CI
        String civersion = "";                                                               //CI Version

        String reqNum = "";                                                               // 본인확인 요청번호
        String result = "";                                                               // 본인확인결과 (Y/N)
        String certDate = "";                                                               // 검증시간
        String certGb = "";                                                               // 인증수단
        String cellNo = "";                                                                // 핸드폰 번호
        String cellCorp = "";                                                                // 이동통신사
        String addVar = "";

        //예약 필드
        String ext1 = "";
        String ext2 = "";
        String ext3 = "";
        String ext4 = "";
        String ext5 = "";

        //복화화용 변수
        String encPara = "";
        String encMsg = "";
        String msgChk = "N";

        //-----------------------------------------------------------------------------------------------------------------
        try {
//            Set<String> keySet = request.getParameterMap().keySet();
//            for(String key: keySet) {
//                log.info(key + ": " + request.getParameter(key));
//            }
            // Parameter 수신 --------------------------------------------------------------------
            log.info(retInfo + ":: retInfo1");
            retInfo = request.getParameter("retInfo").trim();
            log.info(retInfo + ":: retInfo2");
            log.info(sessionReqNum + ":: sessionReqNum");
            log.info(identifiCationNotiCookie + ":: identifiCationNotiCookie");

            if ("null".equals(sessionReqNum) && identifiCationNotiCookie != null) {

                String cookieValue = URLDecoder.decode(identifiCationNotiCookie.getValue(), "UTF-8");
                log.info(cookieValue + ":: cookieValue");
                Gson gson = new Gson();
                IdentificationVo identifiCationVo = new IdentificationVo();
                log.info(identifiCationVo + ":: identifiCationVo");

                identifiCationVo = gson.fromJson(cookieValue, IdentificationVo.class);
                log.info(identifiCationVo + ":: identifiCationVo");
                sessionReqNum = identifiCationVo.getReqNum();
                log.info(sessionReqNum + ":: sessionReqNum");
//                viewName = identifiCationVo.getNotiViewName();
            }

//            mv.setViewName(viewName);

            // 1. 암호화 모듈 (jar) Loading
            log.info("::  1. 암호화 모듈 (jar) Loading");
            com.sci.v2.pccv2.secu.SciSecuManager sciSecuMg = new com.sci.v2.pccv2.secu.SciSecuManager();
            sciSecuMg.setInfoPublic(id, "509F446C62C30A447DC3FC710A200225"); //패스워드는 16자리 필수 영문 무관

            log.info(sciSecuMg + ":: sciSecuMg");
            reqNum = "85729610359798";
            sessionReqNum = "85729610359798";
            //쿠키에서 생성한 값을 Key로 생성 한다.
            retInfo = sciSecuMg.getDec(retInfo, sessionReqNum);
            log.info(retInfo + ":: retInfo");
            // 2.1차 파싱---------------------------------------------------------------
            String[] aRetInfo1 = retInfo.split("\\^");

            encPara = aRetInfo1[0];         //암호화된 통합 파라미터
            encMsg = aRetInfo1[1];    //암호화된 통합 파라미터의 Hash값

            String encMsg2 = sciSecuMg.getMsg(encPara);
            log.info(encMsg2 + ":: encMsg2");

            // 3.위/변조 검증 ---------------------------------------------------------------
            if (encMsg2.equals(encMsg)) {
                msgChk = "Y";
            }

            //비정상적인접근입니다.
            if (msgChk.equals("N")) {
                rtnParamMap.put("msg", "비정상적인접근입니다.");
                rtnParamMap.put("msgCode", 6924);
                rtnParamMap.put("success", false);
                log.info(encMsg2 + ":: 비정상 입금");
                return rtnParamMap;
            }


            // 복호화 및 위/변조 검증 ---------------------------------------------------------------
            log.info(retInfo + ":: 복호화 및 위/변조 검증");
            retInfo = sciSecuMg.getDec(encPara, reqNum);

            String[] aRetInfo = retInfo.split("\\^");

            name = aRetInfo[0];
            birYMD = aRetInfo[1];
            sex = aRetInfo[2];
            fgnGbn = aRetInfo[3];
            di = aRetInfo[4];
            ci1 = aRetInfo[5];
            ci2 = aRetInfo[6];
            civersion = aRetInfo[7];
            reqNum = aRetInfo[8];
            result = aRetInfo[9];
            certGb = aRetInfo[10];
            cellNo = aRetInfo[11];
            cellCorp = aRetInfo[12];
            certDate = aRetInfo[13];
            addVar = aRetInfo[14];

            //예약 필드
            ext1 = aRetInfo[15];
            ext2 = aRetInfo[16];
            ext3 = aRetInfo[17];
            ext4 = aRetInfo[18];
            ext5 = aRetInfo[19];

            //인증실패
            log.info(result + ":: result");
            if (!"Y".equalsIgnoreCase(result)) {
                log.info(rtnParamMap + ":: 인증실패");
                rtnParamMap.put("success", false);
                rtnParamMap.put("msg", "본인인증에 실패하였습니다.");
                rtnParamMap.put("msgCode", 6925);
                return rtnParamMap;
            }

            //mb_security 테이블에 넣기
            Map<String, Object> SecurityMap = new HashMap<String, Object>();
            log.info(cellNo + ":: cellNo");
            // 이름, 생년월일, 휴대폰번호 null 처리
            if (cellNo.trim().isEmpty()) {
                cellNo = "00000000000";
            }
            log.info(name + ":: name");
            if (name.trim().isEmpty()) {
                name = "이름";
            }
            log.info(birYMD + ":: birYMD");
            if (birYMD.trim().isEmpty()) {
                birYMD = "0000000";
            }
            SecurityMap.put("name", name);
            SecurityMap.put("birYMD", birYMD);
            SecurityMap.put("sex", sex);
            SecurityMap.put("fgnGbn", fgnGbn);
            SecurityMap.put("di", di);
            SecurityMap.put("ci1", ci1);
            SecurityMap.put("ci2", ci2);
            SecurityMap.put("civersion", civersion);
            SecurityMap.put("reqNum", reqNum);
            SecurityMap.put("result", result);
            SecurityMap.put("cellNo", cellNo);
            SecurityMap.put("cellCorp", cellCorp);
            SecurityMap.put("certDate", certDate);
            log.info("SecurityMap :::" + SecurityMap);
            log.info("insertSecurityProc" + ":: SecurityMap");
            userDAO.insertSecurityProc(SecurityMap);
            log.info(SecurityMap + ":: SecurityMap");

            rtnParamMap.put("full_name", name);
            rtnParamMap.put("birthday", birYMD);
            rtnParamMap.put("gender", sex);
            rtnParamMap.put("fgnGbn", fgnGbn);
            rtnParamMap.put("phone_company", cellCorp);
            rtnParamMap.put("phone_no", cellNo);
            rtnParamMap.put("msg", "본인인증이 완료되었습니다.");
            rtnParamMap.put("msgCode", 0000);
            rtnParamMap.put("mb_idx", mb_idx);
            rtnParamMap.put("mb_id", mb_id);

            session.removeAttribute("mb_idx");
            session.removeAttribute("mb_id");
            session.removeAttribute("reqNum");
            session.removeAttribute("notiViewName");

            if (identifiCationNotiCookie != null) {
                identifiCationNotiCookie.setValue("");
                identifiCationNotiCookie.setPath("/");
                identifiCationNotiCookie.setMaxAge(0);
                response.addCookie(identifiCationNotiCookie);
            }
            log.info("birYMD:: " + birYMD);
            int age = StringUtil.getAge(birYMD);
            if (age < 19) {
                rtnParamMap.put("success", false);
                rtnParamMap.put("msg", "만 19세 미만은 가입이 불가능합니다.");
                rtnParamMap.put("msgCode", 6926);
                return rtnParamMap;
            }

            log.info("rtnParamMap:: " + rtnParamMap);
            log.info("본인인증이 완료 rtnParamMap :::" + rtnParamMap);
            rtnParamMap.put("success", true);
            rtnParamMap.put("msg", "본인인증이 완료되었습니다.");
            rtnParamMap.put("msgCode", 0000);
            return rtnParamMap;
        } catch (Exception ex) {
            ex.printStackTrace();
            log.info("last:: " + "last");
            log.error(ex);
            System.out.println("[pcc] Receive Error -" + ex.getMessage());
            rtnParamMap.put("success", false);
            return rtnParamMap;
        }
    }
}
