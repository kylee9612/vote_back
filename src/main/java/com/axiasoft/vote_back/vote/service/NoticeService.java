package com.axiasoft.vote_back.vote.service;

import com.axiasoft.vote_back.vote.dao.NoticeDAO;
import com.axiasoft.vote_back.vote.dao.UserDAO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class NoticeService {
    private final static Logger log = LogManager.getLogger(UserService.class);

    @Autowired
    private NoticeDAO noticeDAO;

    public Map<String , Object> getNoticeList(HttpServletResponse response, HttpServletRequest request){
        log.info("---------------------------- get notice list ----------------------------");
        Map<String, Object> ParamMap = new HashMap<>();

        try {
            noticeDAO.getNoticeList(ParamMap);
        }catch (Exception e){
            log.error(e);
        }

        return null;
    }
}
