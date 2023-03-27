package com.axiasoft.vote_back.vote.dao;

import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class JwtDAO {
    private static final Logger log = LogManager.getLogger(JwtDAO.class);
    private static final String NAMESPACE="com.axiasoft.vote_back.jwt.dao.";
    private final SqlSession sqlSession;

    public JwtDAO(@Qualifier("sqlSessionTemplate") SqlSession sqlSession){
        this.sqlSession = sqlSession;
    }
    public Map<String, Object> dbValidateToken(Map<String, Object> map) throws Exception{
        return sqlSession.selectOne(NAMESPACE + " ", map);
    }

    public int updateAmlJwtToken(Map<String, Object> map) throws Exception{
        return sqlSession.update(NAMESPACE + "updateAmlJwtToken", map);
    }

    public int updateJwtToken(Map<String, Object> map) throws Exception{
        return sqlSession.update(NAMESPACE + "updateJwtToken", map);
    }

    public int insertJwtToken(Map<String, Object> map) throws Exception{
        return sqlSession.insert(NAMESPACE + "insertJwtToken", map);
    }
}
