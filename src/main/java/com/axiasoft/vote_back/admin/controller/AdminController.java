package com.axiasoft.vote_back.admin.controller;

import com.axiasoft.vote_back.admin.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PostMapping("/login")
    public Map<String, Object> checkLogin(@RequestBody Map<String, Object> map){
        return adminService.checkLogin(map);
    }

    @GetMapping("voteResult")
    public Map<String, List<Map<String, Object>>> voteResult(){
        return adminService.voteResult();
    }
}
