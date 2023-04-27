package com.axiasoft.vote_back.admin.controller;

import com.axiasoft.vote_back.admin.service.AdminService;
import com.axiasoft.vote_back.admin.domain.RoundVO;
import com.axiasoft.vote_back.util.response.ApiResponse;
import com.axiasoft.vote_back.util.response.CommonErrorCode;
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

    @PostMapping("/vote")
    public ResponseEntity<?> addVote(@RequestBody RoundVO roundVO){
        return ResponseEntity.ok(adminService.addVote(roundVO));
//        return ResponseEntity.ok(new ApiResponse<>(CommonErrorCode.CODE_9999));
    }

    @GetMapping("/vote/results")
    public ResponseEntity<?> voteResult(){
        return ResponseEntity.ok(adminService.voteResult());
    }

    @GetMapping("/round/list")
    public ResponseEntity<?> roundList(){
        return ResponseEntity.ok(adminService.getRoundList());
    }

    @GetMapping("/round/list/{num}")
    public ResponseEntity<?> getRound(@PathVariable("num") int num){
        return ResponseEntity.ok(adminService.getRoundInfo(num));
    }

    @GetMapping("/round/list/last")
    public ResponseEntity<?> latestVoteRound(){
        return ResponseEntity.ok(adminService.voteLatestRound());
    }

    @GetMapping("/index")
    public ResponseEntity<?> indexData(){
        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("doughnut",adminService.latestVoteResult());
        return ResponseEntity.ok(new ApiResponse<>(CommonErrorCode.CODE_0000,returnMap));
    }

    @GetMapping("/coins")
    public ResponseEntity<?> getCoinPage(){
        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("roundList",adminService.unfinishedRound());
        return ResponseEntity.ok(new ApiResponse<>(CommonErrorCode.CODE_0000,returnMap));
    }

    @PostMapping("/coins")
    public ResponseEntity<?> addCoin(@RequestParam(value = "coin_pic_list", required = false) List<MultipartFile> file,
                                     @RequestParam Map<String,Object> map){
        return ResponseEntity.ok(adminService.addCoin(map,file));
    }
}
