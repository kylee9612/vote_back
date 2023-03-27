package com.axiasoft.vote_back.vote.controller;

import com.axiasoft.vote_back.util.response.ApiResponse;
import com.axiasoft.vote_back.util.response.CommonErrorCode;
import com.axiasoft.vote_back.vote.service.NoticeService;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notice")
public class NoticeController {
    private final static Logger log = LogManager.getLogger(UserController.class);

    @Autowired
    private NoticeService noticeService;

    @ManagedOperation(description = "Notice List 불러오기")
    @GetMapping("/getNoticeList")
    public ResponseEntity<?> refreshToken(HttpServletRequest request) {
        log.info("=============================START========================================== in " + request.getRequestURL());
        // 1. 그냥 들어왔을때
        // 2. 페이지 이동
        // 3-1 검색어 이동
        // 3-2 검색어 이동 후 페이지 이동

        return ResponseEntity.ok(new ApiResponse(CommonErrorCode.CODE_9999));
    }
}
