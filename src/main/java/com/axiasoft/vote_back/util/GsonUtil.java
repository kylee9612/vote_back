package com.axiasoft.vote_back.util;

import com.google.gson.Gson;
import org.springframework.util.StringUtils;

public class GsonUtil {
    public static String toJsonString(Object obj){
        Gson gson = new Gson();
        String jsonStr;

        try {
            jsonStr = gson.toJson(obj);
        } catch (Exception e) {
            e.printStackTrace();
            jsonStr = "{}";
        }

        if("\"\\u0027\\u0027\"".equalsIgnoreCase(jsonStr) || "null".equalsIgnoreCase(jsonStr) || "''".equalsIgnoreCase(jsonStr) || StringUtils.isEmpty(jsonStr) == true ||jsonStr.equalsIgnoreCase("\"\"") ) {
            System.out.println("true");
            jsonStr = "{}";
        }

        return jsonStr;
    }
}
