package com.axiasoft.vote_back.vote.dao;

import org.apache.ibatis.session.SqlSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class NoticeDAO {
    private static final Logger log = LogManager.getLogger(JwtDAO.class);
    private static final String NAMESAPCE="com.axiasoft.vote_back.notice.dao.";
    private final SqlSession sqlSession;

    public NoticeDAO(@Qualifier("sqlSessionTemplate") SqlSession sqlSession) {this.sqlSession = sqlSession;}

    public int getNoticeListCount (Map<String, Object> map){
        return (int)sqlSession.selectOne(NAMESAPCE+ "getNoticeListCount", map);
    }

    public List<Map<String,Object>> getNoticeList (Map<String, Object> map){
        return sqlSession.selectList(NAMESAPCE+ "getNoticeList", map);
    }

    public void increaseViews(Map<String,Object> map){
        sqlSession.update(NAMESAPCE+"increaseViews",map);
    }

    public void insertNotice(Map<String,Object> map){
        sqlSession.insert(NAMESAPCE+"insertNotice",map);
    }
    public void updateNotice(Map<String,Object> map){
        sqlSession.update(NAMESAPCE+"updateNotice",map);
    }

    public  Map<String , Object > getNotice (Map<String, Object> map){
        return sqlSession.selectOne(NAMESAPCE+"getNotice",map);
    }
}
