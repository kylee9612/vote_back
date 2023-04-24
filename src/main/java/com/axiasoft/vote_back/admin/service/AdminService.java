package com.axiasoft.vote_back.admin.service;

import com.axiasoft.vote_back.admin.dao.AdminDAO;
import com.axiasoft.vote_back.admin.dto.AdminDTO;
import com.axiasoft.vote_back.admin.dto.AdminDetailDTO;
import com.axiasoft.vote_back.util.response.AdminErrorCode;
import com.axiasoft.vote_back.util.response.ApiResponse;
import com.axiasoft.vote_back.util.response.CommonErrorCode;
import jakarta.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;

@Service
public class AdminService {

    private static final Logger log = LogManager.getLogger(AdminService.class);

    @Autowired
    private AdminDAO adminDAO;

    public Map<String, Object> checkLogin(Map<String, Object> map) {

        Map<String, Object> resultMap = adminDAO.selectLogin(map);
        return resultMap;
    }

    /**
     * TODO
     * reg_date 2023-03-30
     * 프론트 최종 완성 후
     * earliestRound 및 latest Round 를 for 문 조건으로 변경 해 주어야함
     */

    public Map<String, List<Map<String, Object>>> voteResult() {
        int latestRound = adminDAO.selectLatestRound();
        int earliestRound = adminDAO.selectStartRound();
        Map<String, List<Map<String, Object>>> map = new HashMap<>();
        for (int i = 1; i <= 3; i++) {
            List<Map<String, Object>> voteResult = adminDAO.selectVoteResult(i);
            if(voteResult.size() != 0)
                map.put(String.valueOf(i), voteResult);
        }
        log.info(map);
        return map;
    }

    @Transactional
    public ApiResponse<?> addCoin(Map<String, Object> map, @Nullable List<MultipartFile> fileList) {
        Set<String> checkList = new HashSet<>();
        checkList.add("round");
        checkList.add("coin_name");
        checkList.add("coin_symbol");
        checkList.add("coin_info");
        log.info(map);
        log.info(fileList);

        for (String key : checkList) {
            if (!map.containsKey(key))
                return new ApiResponse<>(AdminErrorCode.ADMIN_5001, map);
        }
        for (MultipartFile file : Objects.requireNonNull(fileList)) {
            log.info(file.getOriginalFilename());
        }
        adminDAO.insertCoin(map);
        int idx = Integer.parseInt(map.get("id").toString());
        return new ApiResponse<>(addCoinPicture(fileList, idx) ? CommonErrorCode.CODE_0000 : CommonErrorCode.CODE_9999, map);
    }

    public List<Map<String, Object>> latestVoteResult() {
        return adminDAO.selectLatestVoteResult();
    }

    public List<String> currentRound() {
        List<Map<String, Object>> rtnMap = adminDAO.selectCurrentRound();
        List<String> list = new ArrayList<>();
        for (Map<String, Object> map : rtnMap) {
            list.add(map.get("round").toString());
        }
        return list;
    }

    public List<String> unfinishedRound() {
        List<Map<String, Object>> rtnMap = adminDAO.selectUnfinishedRound();
        List<String> list = new ArrayList<>();
        for (Map<String, Object> map : rtnMap) {
            list.add(map.get("round").toString());
        }
        return list;
    }

    private boolean addCoinPicture(List<MultipartFile> fileList, int coinIdx) {
        Map<String, Object> map = new HashMap<>();
        map.put("coin_idx", coinIdx);
        try {
            for (int i = 0; i < fileList.size(); i++) {
                map.put("order_idx", i);
                map.put("picture", fileList.get(i).getBytes());
                adminDAO.insertCoinPicture(map);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
            return false;
        }
        return true;
    }

    public ApiResponse<?> addAdmin(AdminDTO adminDTO , AdminDetailDTO adminDetailDTO){


        return null;
    }
}
