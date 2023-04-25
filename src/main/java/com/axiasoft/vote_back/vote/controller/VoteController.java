package com.axiasoft.vote_back.vote.controller;

import com.axiasoft.vote_back.util.response.ApiResponse;
import com.axiasoft.vote_back.util.response.CommonErrorCode;
import com.axiasoft.vote_back.vote.service.VoteService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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

    private final static Logger log = LogManager.getLogger(UserController.class);

    @ManagedOperation(description = "2-1-1 투표리스트")
    @PostMapping ("/voteList")
    public ResponseEntity<?> voteList(HttpServletRequest request) {
        Map<String, Object> rtnMap = new HashMap<String, Object>();
       try {
           List<Map<String,Object>> voteList = voteService.selectVoteList();

//           rtnMap.put("voteList",voteList);
           return ResponseEntity.ok(new ApiResponse(true,voteList,CommonErrorCode.CODE_0000));
       }catch (Exception e){
           return ResponseEntity.ok(new ApiResponse(CommonErrorCode.CODE_9999));
       }
    }
}
