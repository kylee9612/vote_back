package com.axiasoft.vote_back.admin.dto;

import org.springframework.lang.Nullable;

import java.util.Date;
import java.util.Map;

public class AdminDTO {
    @Nullable
    private int ad_idx;
    private String ad_id;
    private String ad_pw;
    private String ad_otp_code ;
    @Nullable
    private int ad_level ;
    @Nullable
    private Date reg_date;

    // 생성자
    public AdminDTO(){}
    // 아이디 생성 용
    public AdminDTO(Map<String, Object> map){
        this.ad_id = (String)map.get("ad_id");
        this.ad_pw = (String)map.get("ad_pw");
        this.ad_level= Integer.parseInt((String)map.get("ad_level"));
    }
    // toString
    public String toString(){
        String toString =   "ad_idx         : " +    ad_idx     +
                            "\nad_id        : " +    ad_id      +
                            "\nad_pw        : " +    ad_pw      +
                            "\nad_otp_code  : " +    ad_otp_code+
                            "\nad_level     : " +    ad_level   +
                            "\nreg_date     :"  +    reg_date   ;
        return toString;
    }
    // Getter
    public int getAd_idx() {return ad_idx;}
    public String getAd_id() { return ad_id;}
    public String getAd_pw() {return ad_pw;}
    public String getAd_otp_code() {return ad_otp_code;}
    public int getAd_level() {return ad_level;}
    public Date getReg_date() {return reg_date;}
    // Setter
    public void setAd_idx(int ad_idx) {this.ad_idx = ad_idx;}
    public void setAd_id(String ad_id) {this.ad_id = ad_id;}
    public void setAd_pw(String ad_pw) {this.ad_pw = ad_pw;}
    public void setAd_otp_code(String ad_otp_code) {this.ad_otp_code = ad_otp_code;}
    public void setAd_level(int ad_level) {this.ad_level = ad_level;}
    public void setReg_date(Date reg_date) {this.reg_date = reg_date;}
}
