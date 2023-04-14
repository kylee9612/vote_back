package com.axiasoft.vote_back.vote.service;

import com.axiasoft.vote_back.vote.dao.VoteDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class VoteService {

    @Autowired
    VoteDAO voteDAO;

    public List<Map<String, Object>> selectVoteList() throws Exception{
        return voteDAO.selectVoteList();
    }
}
