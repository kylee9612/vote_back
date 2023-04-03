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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notice")
public class NoticeController {
    private final static Logger log = LogManager.getLogger(UserController.class);

    @Autowired
    private NoticeService noticeService;

    @ManagedOperation(description = "Notice List 불러오기")
    @GetMapping("/getNoticeList")
    public ResponseEntity<?> getNoticeList(@RequestParam Map<String, Object> paramMap ,HttpServletRequest request, HttpServletResponse response) {
        Map <String,Object> returnMap = new HashMap<>();
        log.info("=============================START========================================== in " + request.getRequestURL());
        log.info("paramMap :::" + paramMap);

        Map<String , Integer > pageMap = PageUtil.curPage(paramMap);
        paramMap.put("startNum", pageMap.get("startNum"));
        paramMap.put("lastNum", pageMap.get("lastNum"));


        int notiCount = noticeService.getNoticeListCount(paramMap);
        int lastPage = PageUtil.getLastPage(notiCount);

        // pageNation
        List<Map<String,Object>> noticeList = noticeService.getNoticeList(paramMap);

        returnMap.put("lastPage" , lastPage);
        returnMap.put("noticeList" , noticeList);
        log.info("returnMap :: " + returnMap);
        return ResponseEntity.ok(new ApiResponse(CommonErrorCode.CODE_0000,returnMap));
    }
}
