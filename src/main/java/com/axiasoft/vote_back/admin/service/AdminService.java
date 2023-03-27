package com.axiasoft.vote_back.admin.service;

import com.axiasoft.vote_back.admin.dao.AdminDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

@Service
public class AdminService {

    @Autowired
    private AdminDAO adminDAO;

    public Map<String, Object> checkLogin(Map<String, Object> map) {

        Map<String, Object> resultMap = adminDAO.selectLogin(map);
        return resultMap;
    }
}
