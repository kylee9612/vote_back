package com.axiasoft.vote_back.vote.dao;

import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class VoteDAO {

    @Autowired
    private Environment env;

    private static final String NAMESPACE="com.axiasoft.vote_back.vote.dao.";

    private final SqlSession sqlSession;

    private final static Logger log = LogManager.getLogger(VoteDAO.class);

    public VoteDAO(@Qualifier("sqlSessionTemplate") SqlSession sqlSession) {this.sqlSession = sqlSession;}

    public List<Map<String, Object>> selectVoteList(){
        List<Map<String, Object>> voteList = sqlSession.selectList(NAMESPACE+"selectVoteList");
        return voteList;
    }

    public List<Map<String, Object>> getCoinList(Map<String, Object> map){
        List<Map<String, Object>> coinList = sqlSession.selectList(NAMESPACE + "getCoinList",map.get("round"));
        return coinList;
    }

    public List<Map<String, Object>> getPicList(Map<String, Object> map){
        List<Map<String, Object>> picList = sqlSession.selectList(NAMESPACE + "getCoinImg",map);
        return picList;
    }

}
