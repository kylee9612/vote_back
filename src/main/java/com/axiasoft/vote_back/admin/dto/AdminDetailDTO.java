package com.axiasoft.vote_back.admin.dto;

import org.springframework.lang.Nullable;

import java.util.Map;

public class AdminDetailDTO {
    @Nullable
    private int ad_idx;
    private String ad_name;
    private String ad_phone;

    public AdminDetailDTO(){}
    // 아이디 생성 용
    public AdminDetailDTO(Map<String, Object> map){
        this.ad_idx = (int)map.get("ad_idx");
        this.ad_name = (String)map.get("ad_name");
        this.ad_phone= "123412342134";
    }

    // toString
    public String toString(){
        String toString =   "ad_idx         : " +    ad_idx     +
                "\nad_name        : " +    ad_name      +
                "\nad_phone        : " +    ad_phone;
        return toString;
    }


    // Getter
    public int getAd_idx() {return ad_idx;}
    public String getAd_name() {return ad_name;}
    public String getAd_phone() {return ad_phone;}
    // Setter
    public void setAd_idx(int ad_idx) {this.ad_idx = ad_idx;}
    public void setAd_name(String ad_name) {this.ad_name = ad_name;}
    public void setAd_phone(String ad_phone) {this.ad_phone = ad_phone;}
}
