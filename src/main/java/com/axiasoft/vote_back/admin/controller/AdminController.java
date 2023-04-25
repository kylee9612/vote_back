package com.axiasoft.vote_back.admin.controller;

import com.axiasoft.vote_back.admin.dto.AdminDTO;
import com.axiasoft.vote_back.admin.dto.AdminDetailDTO;
import com.axiasoft.vote_back.admin.service.AdminService;
import com.axiasoft.vote_back.util.PageUtil;
import com.axiasoft.vote_back.admin.domain.VoteVO;
import com.axiasoft.vote_back.util.response.ApiResponse;
import com.axiasoft.vote_back.util.response.CommonErrorCode;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
    private static final Logger log = LogManager.getLogger(AdminController.class);

    @Autowired
    private AdminService adminService;

    @PostMapping("/login")
    public ResponseEntity<?> checkLogin(@RequestBody Map<String, Object> map){
        Map<String, Object> rtnMap = adminService.checkLogin(map);
        return ResponseEntity.ok(new ApiResponse<>(CommonErrorCode.CODE_0000, rtnMap));
    }

    @PostMapping("/vote")
    public ResponseEntity<?> addVote(@RequestBody VoteVO voteVO){
        return ResponseEntity.ok(adminService.addVote(voteVO));
//        return ResponseEntity.ok(new ApiResponse<>(CommonErrorCode.CODE_9999));
    }

    @GetMapping("/vote/results")
    public ResponseEntity<?> voteResult(){
        return ResponseEntity.ok(adminService.voteResult());
    }

    @GetMapping("/vote/list")
    public ResponseEntity<?> voteList(){
        return ResponseEntity.ok(adminService.getVoteList());
    }

    @GetMapping("/vote/list/last")
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

    @PostMapping("/addAdmin")
    public ResponseEntity<?> addAdmin(@RequestBody Map<String,Object> map) {
        // adminDTO와 adminDetailDTO 객체를 사용하여 로직을 처리합니다.
        return ResponseEntity.ok(adminService.addAdmin(map));
    }

    @PostMapping("/getAdminList")
    public ResponseEntity<?> getAdminList(@RequestParam Map<String, Object> paramMap, HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> returnMap = new HashMap<>();
        log.info("=============================START========================================== in " + request.getRequestURL());
        log.info("paramMap :::" + paramMap);

        paramMap = PageUtil.curPage(paramMap);

        int adminCount = adminService.getAdminListCount(paramMap);
        int lastPage = PageUtil.getLastPage(adminCount);

        // pageNation
        List<Map<String, Object>> adminList = adminService.getAdminList(paramMap);

        returnMap.put("lastPage", lastPage);
        returnMap.put("adminList", adminList);
        log.info("returnMap :: " + returnMap);
        return ResponseEntity.ok(new ApiResponse(CommonErrorCode.CODE_0000, returnMap));
    }

}
