package com.axiasoft.vote_back.admin.dao;

import com.axiasoft.vote_back.admin.domain.RoundVO;
import com.axiasoft.vote_back.admin.dto.AdminDTO;
import com.axiasoft.vote_back.admin.dto.AdminDetailDTO;
import com.axiasoft.vote_back.util.cipher.SHA256Util;
import jakarta.annotation.PostConstruct;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class AdminDAO {

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;
    @Autowired
    private SHA256Util sha256Util;
    private static final String NAMESPACE = "com.axiasoft.vote_back.admin.dao.";

    private Set<String> sha256Set;

    @PostConstruct
    private void init() {
        setSha256Set();
    }

    private void setSha256Set() {
        sha256Set = new HashSet<>();
        sha256Set.add("ad_pw");
    }

    private Map<String, Object> checkSha256(Map<String, Object> map) {
        Map<String, Object> resultMap = new HashMap<>(map);
        for (String key : sha256Set) {
            if (resultMap.containsKey(key)) {
                String str = sha256Util.encrypt(resultMap.get(key).toString());
                if (str != null)
                    resultMap.put(key, str);
                else
                    return null;
            }
        }
        return resultMap;
    }

    public Map<String, Object> selectLogin(Map<String, Object> map) {
        return sqlSessionTemplate.selectOne(NAMESPACE + "selectLogin", checkSha256(map));
    }

    public int selectLatestRound() {
        return sqlSessionTemplate.selectOne(NAMESPACE + "selectLatestRound");
    }

    public int selectStartRound() {
        return sqlSessionTemplate.selectOne(NAMESPACE + "selectStartRound");
    }

    public List<Map<String, Object>> selectVoteResult(int round) {
        return sqlSessionTemplate.selectList(NAMESPACE + "selectVoteResult", round);
    }

    public List<Map<String, Object>> selectVoteList(int round) {
        return sqlSessionTemplate.selectList(NAMESPACE + "selectVoteList", round);
    }

    public List<RoundVO> selectRoundList(){
        return sqlSessionTemplate.selectList(NAMESPACE + "selectRoundList");
    }

    public RoundVO selectRoundInfo(int num) {
        return sqlSessionTemplate.selectOne(NAMESPACE + "selectRoundInfo", num);
    }

    public List<Map<String, Object>> selectLatestVoteResult() {
        return sqlSessionTemplate.selectList(NAMESPACE + "selectLatestVoteResult");
    }

    public List<Map<String, Object>> selectCurrentRound() {
        return sqlSessionTemplate.selectList(NAMESPACE + "selectCurrentRound");
    }

    public List<Map<String, Object>> selectUnfinishedRound(){
        return sqlSessionTemplate.selectList(NAMESPACE + "selectUnfinishedRound");
    }

    public int getAdminListCount (Map<String, Object> map){
        return (int)sqlSessionTemplate.selectOne(NAMESPACE+ "selectAdminListCount", map);
    }
    public List<Map<String,Object>> selectAdminList (Map<String, Object> map){
        return sqlSessionTemplate.selectList(NAMESPACE+ "selectAdminList", map);
    }
    public Map<String,Object> selectAdmin (int ad_idx){
        return sqlSessionTemplate.selectOne(NAMESPACE+ "selectAdmin", ad_idx);
    }
    public int createAdmin(AdminDTO adminDTO){
        return sqlSessionTemplate.insert(NAMESPACE+"insertAdmin", adminDTO);
    }
    public void createAdminDetail(AdminDetailDTO adminDetailDTO){
        sqlSessionTemplate.insert(NAMESPACE+"insertAdminDetail", adminDetailDTO);
    }

    public int insertCoin(Map<String, Object> map) {
        return sqlSessionTemplate.insert(NAMESPACE + "insertCoin", map);
    }
    public int insertCoinPicture(Map<String, Object> map){
        return sqlSessionTemplate.insert(NAMESPACE+"insertCoinPicture",map);
    }
    public int insertVote(RoundVO vo){
        return sqlSessionTemplate.insert(NAMESPACE + "insertVote",vo);
    }
}
