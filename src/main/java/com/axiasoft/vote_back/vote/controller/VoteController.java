package com.axiasoft.vote_back.vote.controller;

import com.axiasoft.vote_back.util.response.ApiResponse;
import com.axiasoft.vote_back.util.response.CommonErrorCode;
import com.axiasoft.vote_back.vote.service.VoteService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.web.bind.annotation.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/vote")
public class VoteController {

    @Autowired
    VoteService voteService;

    private final static Logger log = LogManager.getLogger(VoteController.class);

    @ManagedOperation(description = "2-1-1 투표리스트")
    @PostMapping ("/voteList")
    public ResponseEntity<?> voteList(HttpServletRequest request) {
       try {
           log.info("=============================START= in " + request.getRequestURL());
           List<Map<String,Object>> voteList = voteService.selectVoteList();

//           rtnMap.put("voteList",voteList);
           return ResponseEntity.ok(new ApiResponse(true,voteList,CommonErrorCode.CODE_0000));
       }catch (Exception e){
           log.debug(e);
           return ResponseEntity.ok(new ApiResponse(CommonErrorCode.CODE_9999));
       }
    }

    @ManagedOperation(description = "2-2-1 투표할 코인리스트")
    @PostMapping ("/coin/list")
    public ResponseEntity<?> coinList(@RequestBody Map<String, Object> paramMap, HttpServletRequest request) {
        try {
            log.info("=============================START= in " + request.getRequestURL());
            List<Map<String,Object>> coinList = voteService.selectCoinList(paramMap);
            return ResponseEntity.ok(new ApiResponse(true,coinList,CommonErrorCode.CODE_0000));
        }catch (Exception e){
            log.info(e);
            return ResponseEntity.ok(new ApiResponse(CommonErrorCode.CODE_9999));
        }
    }

    @ManagedOperation(description = "2-2-2 투표할 코인이미지")
    @PostMapping ("/coin/pics")
    public ResponseEntity<?> coinPics(@RequestBody Map<String, Object> paramMap, HttpServletRequest request) {
        try {
            log.debug("=============================START= in " + request.getRequestURL());

            List<Map<String,Object>> coinList = voteService.getCoinPics(paramMap);
            System.out.println(paramMap);
            System.out.println(coinList);
            return ResponseEntity.ok(new ApiResponse(true,coinList,CommonErrorCode.CODE_0000));
        }catch (Exception e){
            log.debug(e);
            System.out.println(e);
            return ResponseEntity.ok(new ApiResponse(CommonErrorCode.CODE_9999));
        }
    }
}
