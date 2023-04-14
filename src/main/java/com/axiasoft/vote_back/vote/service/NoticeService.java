package com.axiasoft.vote_back.vote.service;

import com.axiasoft.vote_back.vote.dao.NoticeDAO;
import com.axiasoft.vote_back.vote.dao.UserDAO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NoticeService {
    private final static Logger log = LogManager.getLogger(UserService.class);

    @Autowired
    private NoticeDAO noticeDAO;

    public int getNoticeListCount(Map<String, Object> map){
        return  noticeDAO.getNoticeListCount(map);
    }
    public Map<String,Object> getNotice(Map<String, Object> map){
        return noticeDAO.getNotice(map);
    }
    public List<Map<String,Object>> getNoticeList(Map<String, Object> map){
        return noticeDAO.getNoticeList(map);
    }

    public void insertNotice(Map<String, Object> map){
        noticeDAO.insertNotice(map);
    }
    public void updateNotice(Map<String, Object> map){
        noticeDAO.updateNotice(map);
    }

    public void increaseViews(Map<String,Object> map){
        noticeDAO.increaseViews(map);
    }

}
