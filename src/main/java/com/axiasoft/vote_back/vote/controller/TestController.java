package com.axiasoft.vote_back.vote.controller;

import com.axiasoft.vote_back.util.response.ApiResponse;
import com.axiasoft.vote_back.util.response.CommonErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @ManagedOperation(description = "test")
    @GetMapping("/test1")
    public ResponseEntity<?> test(HttpServletRequest request) {

        try {
            return ResponseEntity.ok(new ApiResponse(CommonErrorCode.CODE_1101));
        }catch (Exception e){
            return ResponseEntity.ok(new ApiResponse(CommonErrorCode.CODE_9999));
        }
    }
}
