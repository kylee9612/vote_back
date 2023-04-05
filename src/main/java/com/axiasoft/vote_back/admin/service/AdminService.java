package com.axiasoft.vote_back.admin.service;

import com.axiasoft.vote_back.admin.dao.AdminDAO;
import com.axiasoft.vote_back.util.response.AdminErrorCode;
import com.axiasoft.vote_back.util.response.ApiResponse;
import com.axiasoft.vote_back.util.response.CommonErrorCode;
import com.axiasoft.vote_back.util.response.ErrorCode;
import jakarta.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.security.NoSuchAlgorithmException;
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

    /** TODO
     * reg_date 2023-03-30
     * 프론트 최종 완성 후
     * earliestRound 및 latest Round 를 for 문 조건으로 변경 해 주어야함
     */

    public Map<String, List<Map<String, Object>>> voteResult(){
        int latestRound = adminDAO.selectLatestRound();
        int earliestRound = adminDAO.selectStartRound();
        Map<String, List<Map<String, Object>>> map = new HashMap<>();
        for(int i = 1; i <= 3; i++){
            map.put(String.valueOf(i),adminDAO.selectVoteResult(i));
        }
        log.info(map);
        return map;
    }

    public ApiResponse<?> addCoin(Map<String, Object> map, @Nullable List<MultipartFile> fileList){
        Set<String> checkList = new HashSet<>();
        checkList.add("round");
        checkList.add("coin_name");
        checkList.add("coin_symbol");
        checkList.add("coin_info");

        for(String key : checkList) {
            if(!map.containsKey(key))
                return new ApiResponse<>(AdminErrorCode.ADMIN_5001, map);
        }
        for(MultipartFile file : fileList){
            log.info(file.getOriginalFilename());
        }

//        int check = adminDAO.insertCoin(map);
        return new ApiResponse<>(1 == 1 ? CommonErrorCode.CODE_0000 : CommonErrorCode.CODE_9999,map);
    }

    public List<Map<String, Object>> latestVoteResult(){
        return adminDAO.selectLatestVoteResult();
    }

    public List<String> currentRound(){
        List<Map<String, Object>> rtnMap = adminDAO.selectCurrentRound();
        List<String> list = new ArrayList<>();
        for(Map<String, Object> map : rtnMap){
            list.add(map.get("round").toString());
        }
        return list;
    }
}
