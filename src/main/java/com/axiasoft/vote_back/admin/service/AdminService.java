package com.axiasoft.vote_back.admin.service;

import com.axiasoft.vote_back.admin.dao.AdminDAO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdminService {

    private static final Logger log = LogManager.getLogger(AdminService.class);

    @Autowired
    private AdminDAO adminDAO;

    public Map<String, Object> checkLogin(Map<String, Object> map) {

        Map<String, Object> resultMap = adminDAO.selectLogin(map);
        return resultMap;
    }

    public Map<String, List<Map<String, Object>>> voteResult(){
        int latestRound = adminDAO.selectLatestRound();
        Map<String, List<Map<String, Object>>> map = new HashMap();
        for(int i = 1; i <= latestRound; i++){
            map.put(String.valueOf(i),adminDAO.selectVoteResult(i));
        }
        log.info(map);
        return map;
    }
}
