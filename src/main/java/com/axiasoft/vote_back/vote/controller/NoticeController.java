package com.axiasoft.vote_back.vote.controller;

import com.axiasoft.vote_back.util.PageUtil;
import com.axiasoft.vote_back.util.response.ApiResponse;
import com.axiasoft.vote_back.util.response.CommonErrorCode;
import com.axiasoft.vote_back.util.response.ErrorCode;
import com.axiasoft.vote_back.vote.service.NoticeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@RestController
@RequestMapping("/api/notice")
public class NoticeController {
    private final static Logger log = LogManager.getLogger(UserController.class);

    @Autowired
    private NoticeService noticeService;

    /**
     * -----------SELECT-----------------------------------------------------------
     **/

    @ManagedOperation(description = "Notice List 불러오기")
    @GetMapping("/getNoticeList")
    public ResponseEntity<?> getNoticeList(@RequestParam Map<String, Object> paramMap, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> returnMap = new HashMap<>();
        log.info("=============================START========================================== in " + request.getRequestURL());
        log.info("paramMap :::" + paramMap);

        paramMap = PageUtil.curPage(paramMap);

        int notiCount = noticeService.getNoticeListCount(paramMap);
        int lastPage = PageUtil.getLastPage(notiCount);

        // pageNation
        List<Map<String, Object>> noticeList = noticeService.getNoticeList(paramMap);

        returnMap.put("lastPage", lastPage);
        returnMap.put("noticeList", noticeList);
        log.info("returnMap :: " + returnMap);
        return ResponseEntity.ok(new ApiResponse(CommonErrorCode.CODE_0000, returnMap));
    }

    @ManagedOperation(description = "Notice 불러오기")
    @PostMapping("/getNotice")
    public ResponseEntity getNotice(@RequestBody Map<String, Object> paramMap, HttpServletRequest request, HttpServletResponse response) {
        log.info("=============================START========================================== in " + request.getRequestURL());
        Map<String, Object> returnMap = new HashMap<>();
        CommonErrorCode code = null;
        log.info("paramMap :::" + paramMap);
        try {
            Map<String, Object> notice = noticeService.getNotice(paramMap);
            returnMap.put("notice", notice);
            code = CommonErrorCode.CODE_0000;
        } catch (Exception e) {
            log.info(e);
            code = CommonErrorCode.CODE_9999;
        }

        return ResponseEntity.ok(new ApiResponse<>(code, returnMap));

    }
    @ManagedOperation(description = "Notice 저장")
    @PostMapping("/editNotice")
    public ResponseEntity<?> editNotice(@RequestPart(value = "notice_pic_list", required = false) List<MultipartFile> file,
                                        @RequestParam Map<String, Object> paramMap) {
        return ResponseEntity.ok(noticeService.editNotice(paramMap,file));
    }
    @PostMapping("/increaseViews")
    public void increaseViews(@RequestBody Map<String, Object> paramMap, HttpServletRequest request, HttpServletResponse response) {
        log.info("=============================START========================================== in " + request.getRequestURL());
        log.info("paramMap :::" + paramMap);
        noticeService.increaseViews(paramMap);
    }
}
