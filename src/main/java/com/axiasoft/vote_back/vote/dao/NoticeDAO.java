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
    private static final Logger log = LogManager.getLogger(NoticeDAO.class);
    private static final String NAMESPACE="com.axiasoft.vote_back.notice.dao.";
    private final SqlSession sqlSession;

    public NoticeDAO(@Qualifier("sqlSessionTemplate") SqlSession sqlSession) {this.sqlSession = sqlSession;}

    public void insertNotice(Map<String,Object> map){sqlSession.insert(NAMESPACE+"insertNotice",map);}
    public int insertNoticePicture(Map<String, Object> map){return (int)sqlSession.insert(NAMESPACE+"insertNoticePicture",map);}
    public int getNoticeListCount (Map<String, Object> map){return (int)sqlSession.selectOne(NAMESPACE+ "selectNoticeListCount", map);}
    public List<Map<String,Object>> getNoticeList (Map<String, Object> map){return sqlSession.selectList(NAMESPACE+ "selectNoticeList", map);}
    public List<Map<String,Object>> getNoticePicture(Map<String, Object> map){return sqlSession.selectList(NAMESPACE+"selectNoticePictures",map);}
    public int getNoticeImageCount(Map<String, Object> map){return sqlSession.selectOne(NAMESPACE+"selectNoticeImageCount",map);}
    public  Map<String , Object > getNotice (Map<String, Object> map){return sqlSession.selectOne(NAMESPACE+"selectNotice",map);}

    public void increaseViews(Map<String,Object> map){sqlSession.update(NAMESPACE+"increaseViews",map);}
    public void updateNotice(Map<String,Object> map){sqlSession.update(NAMESPACE+"updateNotice",map);}
    public void updateIdAfterDelete(Map<String,Object> map){sqlSession.update(NAMESPACE+"updateIdAfterDelete",map);}

    public void deleteNoticeImage(Map<String,Object> map){sqlSession.delete(NAMESPACE+"deleteNoticeImage",map);}
}
