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

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.*;

@Service
public class NoticeService {
    private final static Logger log = LogManager.getLogger(NoticeService.class);

    @Autowired
    private NoticeDAO noticeDAO;


    // page 변환용 count
    public int getNoticeListCount(Map<String, Object> map){
        return  noticeDAO.getNoticeListCount(map);
    }
    // 공지사항 1개
    public HashMap<String,Object> getNotice(Map<String, Object> map) throws SQLException, IOException {
        HashMap<String, Object> result = new HashMap<>();
        Map<String, Object> notice = noticeDAO.getNotice(map);
        List<Map<String, Object>> noticePictures = noticeDAO.getNoticePicture(map);
//        blobToImage(noticePictures);
        result.put("noticePictures", noticePictures);
        result.put("notice", notice);
        return result;
    }
//    private void blobToImage(List<Map<String, Object>> list) throws SQLException, IOException {
//        for(int i =0 ; i < list.size() ; i ++){
//            byte[] byteData = (byte[])list.get(i).get("picture");
//            ByteArrayInputStream bis = new ByteArrayInputStream(byteData);
//            BufferedImage image = ImageIO.read(bis);
//            list.get(i).put("picture",image);
//        }
//        log.info("success!!!");
//    }
    // 공지사항 리스트
    public List<Map<String,Object>> getNoticeList(Map<String, Object> map){
        log.info("map : "+ map);
        return noticeDAO.getNoticeList(map);
    }
    public ApiResponse<?> editNotice(Map<String, Object> map , @Nullable List<MultipartFile> fileList){
        log.info("edit Notice Start");
        log.info("paramMap :::" + map);
        log.info("notice_pic_list :::" + fileList);

        Map<String, Object> returnMap = new HashMap<>();

        CommonErrorCode code = null;

        for (MultipartFile file : Objects.requireNonNull(fileList)) {
            log.info(file.getOriginalFilename());
        }
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
            return new ApiResponse<>(CommonErrorCode.CODE_9999);
        }
        int idx = Integer.parseInt(map.get("nt_no").toString());
        log.info("edit Notice End");
        return new ApiResponse<>(addNoticePicture(fileList,idx) ? code : CommonErrorCode.CODE_9999, map);
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

    // DB이미지 삭제 후 순서 재 설정
    public void deleteNoticeImage(Map<String,Object> map){
        int truee = noticeDAO.getNoticeImageCount(map);
        log.info("truee : "+ truee);
        if(truee >= 1){
            try{
                noticeDAO.deleteNoticeImage(map);
            }catch (Exception e){
                log.info(e);
            }

            try{
                noticeDAO.updateIdAfterDelete(map);
            }catch (Exception e){
                log.info(e);
            }

        }
    }

    private boolean addNoticePicture(List<MultipartFile> fileList, int noticeIdx) {
        log.info("addNoticePicture");
        Map<String, Object> map = new HashMap<>();
        map.put("nt_no", noticeIdx);
        int order_idx = noticeDAO.getNoticePictureLastOrderIdx(map);
        try {
            for (int i = 0; i < fileList.size(); i++) {
                map.put("order_idx", i+order_idx);
                map.put("picture", fileList.get(i).getBytes());
                noticeDAO.insertNoticePicture(map);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
            return false;
        }
        log.info("addNoticePicture end");
        return true;
    }

}
