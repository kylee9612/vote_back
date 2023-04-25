package com.axiasoft.vote_back.admin.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class RoundVO {
    private int round;
    private String title;
    private String content;
    private Date startDate;
    private Date endDate;
}
