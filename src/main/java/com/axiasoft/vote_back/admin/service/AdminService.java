package com.axiasoft.vote_back.admin.service;

import com.axiasoft.vote_back.admin.dao.AdminDAO;
import com.axiasoft.vote_back.admin.domain.RoundVO;
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

    public ApiResponse<?> voteResult() {
        int latestRound = adminDAO.selectLatestRound();
        Map<String, List<Map<String, Object>>> map = new HashMap<>();
        for (int i = 1; i <= latestRound; i++) {
            List<Map<String, Object>> voteResult = adminDAO.selectVoteResult(i);
            if (voteResult.size() != 0)
                map.put(String.valueOf(i), voteResult);
        }
        log.info(map);
        return new ApiResponse<>(CommonErrorCode.CODE_0000, map);
    }

    public ApiResponse<?> voteLatestRound() {
        int lastRound = adminDAO.selectLatestRound();
        return new ApiResponse<>(CommonErrorCode.CODE_0000, lastRound);
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

    @Transactional
    public ApiResponse<?> addVote(RoundVO roundVO){
        try{
            log.info(roundVO);
            return new ApiResponse<>(adminDAO.insertVote(roundVO) == 1 ? CommonErrorCode.CODE_0000 : CommonErrorCode.CODE_9999);
        }catch (Exception e){
            e.printStackTrace();
            return new ApiResponse<>(CommonErrorCode.CODE_9999);
        }
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

    public ApiResponse<?> getRoundList() {
        try {
            List<RoundVO> rtnList = adminDAO.selectRoundList();
//            int latestRound = adminDAO.selectLatestRound();
//            List<Map<String, Object>> rtnList = new ArrayList<>();
//            for (int i = 1; i <= latestRound; i++) {

//                List<Map<String, Object>> listMap = adminDAO.selectVoteList(i);
//                if (listMap.size() != 0) {
//                    Map<String, Object> map = listMap.get(0);
//                    List<byte[]> blobs = new ArrayList<>();
//                    for (Map<String, Object> list : listMap) {
//                        blobs.add((byte[]) list.get("picture"));
//                    }
//                    map.put("picture", blobs);
//                    rtnList.add(map);
//                }
//            }
            return new ApiResponse<>(CommonErrorCode.CODE_0000, rtnList);
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiResponse<>(CommonErrorCode.CODE_9999);
        }
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
}
