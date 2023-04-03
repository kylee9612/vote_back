package com.axiasoft.vote_back.util;

import com.axiasoft.vote_back.vote.controller.UserController;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class PageUtil {

    private final static Logger log = LogManager.getLogger(UserController.class);

    public static Map<String,Integer> curPage(Map<String, Object> map){

        Map<String,Integer> returnMap = new HashMap<>();
        System.out.println("curPage :: " + map.get("curPage").getClass());
        try {
            if(map.containsKey("curPage")){
                int curPage = Integer.parseInt((String)map.get("curPage"));
                int startNum = (curPage-1) * 10;
                int lastNum = curPage * 10;

                returnMap.put("startNum", startNum);
                returnMap.put("lastNum", lastNum);

            }
        }catch (Exception e){
           log.error(e);
        }
            return returnMap;
    }

    public static int getLastPage(int count){
        int lastPage = 0 ;

        while(count > 0){
            count -= 10;
            lastPage++;
        }

        return lastPage;
    }
}
