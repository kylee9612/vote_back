package com.axiasoft.vote_back.admin.controller;

import com.axiasoft.vote_back.admin.service.AdminService;
import com.axiasoft.vote_back.util.response.ApiResponse;
import com.axiasoft.vote_back.util.response.CommonErrorCode;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PostMapping("/login")
    public ResponseEntity<?> checkLogin(@RequestBody Map<String, Object> map){
        Map<String, Object> rtnMap = adminService.checkLogin(map);
        return ResponseEntity.ok(new ApiResponse<>(CommonErrorCode.CODE_0000, rtnMap));
    }

    @GetMapping("/voteResult")
    public ResponseEntity<?> voteResult(){
        Map<String, List<Map<String, Object>>> rtnMap = adminService.voteResult();
        return ResponseEntity.ok(new ApiResponse<>(CommonErrorCode.CODE_0000,rtnMap));
    }

    @GetMapping("/index")
    public ResponseEntity<?> indexData(){
        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("doughnut",adminService.latestVoteResult());
        return ResponseEntity.ok(new ApiResponse<>(CommonErrorCode.CODE_0000,returnMap));
    }

    @GetMapping("/addCoin")
    public ResponseEntity<?> addCoinPage(){
        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("roundList",adminService.currentRound());
        return ResponseEntity.ok(new ApiResponse<>(CommonErrorCode.CODE_0000,returnMap));
    }

    @PostMapping("/addCoin")
    public ResponseEntity<?> addCoin(@RequestParam(value = "coin_pic_list", required = false) List<MultipartFile> file,
                                     @RequestParam Map<String,Object> map){
        return ResponseEntity.ok(adminService.addCoin(map,file));
    }
}
