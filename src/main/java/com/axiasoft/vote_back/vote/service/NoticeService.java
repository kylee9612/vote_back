package com.axiasoft.vote_back.vote.service;

import com.axiasoft.vote_back.util.response.ApiResponse;
import com.axiasoft.vote_back.util.response.CommonErrorCode;
import com.axiasoft.vote_back.vote.dao.NoticeDAO;
import com.axiasoft.vote_back.vote.dao.UserDAO;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class NoticeService {
    private final static Logger log = LogManager.getLogger(UserService.class);

    @Autowired
    private NoticeDAO noticeDAO;


    // page 변환용 count
    public int getNoticeListCount(Map<String, Object> map){
        return  noticeDAO.getNoticeListCount(map);
    }
    // 공지사항 1개
    public Map<String,Object> getNotice(Map<String, Object> map){
        return noticeDAO.getNotice(map);
    }
    // 공지사항 리스트
    public List<Map<String,Object>> getNoticeList(Map<String, Object> map){
        return noticeDAO.getNoticeList(map);
    }
    public ResponseEntity<?> editNotice(Map<String, Object> map , @Nullable List<MultipartFile> fileList){
        log.info("edit Notice Start");
        log.info("paramMap :::" + map);
        log.info("notice_pic_list :::" + fileList);

        Map<String, Object> returnMap = new HashMap<>();

        CommonErrorCode code = null;
        try {
            if (map.get("nt_no").equals("0")) {
                // 생성하기
                noticeDAO.insertNotice(map);
                code = CommonErrorCode.CODE_1201;
            } else {
                // 업데이트 하기
                noticeDAO.updateNotice(map);
                code = CommonErrorCode.CODE_1202;
            }

        } catch (Exception e) {
            log.info(e);
            code = CommonErrorCode.CODE_9999;
        }
        log.info("edit Notice End");
        return ResponseEntity.ok(new ApiResponse(code, returnMap));
    }
    // 공지사항 생성
    @Transactional
    public void insertNotice(Map<String, Object> map , @Nullable List<MultipartFile> fileList){
        noticeDAO.insertNotice(map);
    }
    // 공지사항 수정
    @Transactional
    public void updateNotice(Map<String, Object> map , @Nullable List<MultipartFile> fileList){
        noticeDAO.updateNotice(map );
    }
    // 뷰 카운트 업데이트
    public void increaseViews(Map<String,Object> map){
        noticeDAO.increaseViews(map);
    }

}
