package com.axiasoft.vote_back.vote.controller;

import com.axiasoft.vote_back.util.response.ApiResponse;
import com.axiasoft.vote_back.util.response.CommonErrorCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @PostMapping("/login")
    public ResponseEntity loginUser(@RequestBody Map<String, Object> map){


        return ResponseEntity.ok(new ApiResponse(CommonErrorCode.CODE_0000,""));
    }
}
