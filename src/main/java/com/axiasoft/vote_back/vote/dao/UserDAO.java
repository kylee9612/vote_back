package com.axiasoft.vote_back.vote.dao;

import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class UserDAO {
    @Autowired
    private Environment env;
    private static final String NAMESPACE="com.axiasoft.vote_back.user.dao.";
    private final SqlSession sqlSession;
    private final static Logger log = LogManager.getLogger(UserDAO.class);

    public UserDAO(@Qualifier("sqlSessionTemplate") SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    public Object insertSecurityProc(Map<String, Object> map) throws Exception {
        log.debug("1. domain.url  ::::" + env.getProperty("domain.url"));
//        log.debug("2. 암호화  ::::" + SecurityUtil.encryptUptsCrypto("mb_security", "name", (String) map.get("name")));
        log.debug("3. domain.url  ::::" + env.getProperty("domain.url"));

        log.debug("1. name  ::::" + map.get("name"));

//        map.put("name", SecurityUtil.encryptUptsCrypto("mb_security", "name", (String) map.get("name")));
//        map.put("cellNo", SecurityUtil.encryptUptsCrypto("mb_security", "cellno", (String) map.get("cellNo")));
        log.debug("2. name  ::::" + map.get("name"));

        log.debug("3. name  ::::" + map.get("name"));
        return sqlSession.insert(NAMESPACE + "insertSecurityProc", map);
    }
}
