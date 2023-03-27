package com.axiasoft.vote_back.admin.dao;

import com.axiasoft.vote_back.util.SHA256Util;
import jakarta.annotation.PostConstruct;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Repository
public class AdminDAO {

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;
    @Autowired
    private SHA256Util sha256Util;
    private static String NAMESPACE = "com.axiasoft.vote_back.admin.dao.";

    private Set<String> sha256Set;

    @PostConstruct
    private void init(){
        setSha256Set();
    }

    private void setSha256Set(){
        sha256Set = new HashSet<>();
        sha256Set.add("ad_pw");
    }

    private Map<String, Object> checkSha256(Map<String, Object> map){
        Map<String, Object> resultMap = new HashMap<>(map);
        for(String key : sha256Set){
            if(resultMap.containsKey(key)) {
                String str = sha256Util.encrypt(resultMap.get(key).toString());
                if(str != null)
                    resultMap.put(key, str);
                else
                    return null;
            }
        }
        return resultMap;
    }

    public Map <String, Object> selectLogin(Map<String, Object> map){
        return sqlSessionTemplate.selectOne(NAMESPACE+"selectLogin",checkSha256(map));
    }

}
