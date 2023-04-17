package com.axiasoft.vote_back.vote.service;

import com.axiasoft.vote_back.vote.controller.UserController;
import com.axiasoft.vote_back.vote.dao.VoteDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Service
public class VoteService {

    @Autowired
    private VoteDAO voteDAO;

    private final static Logger log = LogManager.getLogger(VoteService.class);

    public List<Map<String, Object>> selectVoteList() throws Exception{
        return voteDAO.selectVoteList();
    }

    public List<Map<String, Object>> selectCoinList(Map<String, Object> paramMap) throws Exception{
        return voteDAO.getCoinList(paramMap);
    }

    public List<Map<String, Object>> getCoinPics(Map<String, Object> paramMap) throws Exception{
        return voteDAO.getPicList(paramMap);
    }
}
