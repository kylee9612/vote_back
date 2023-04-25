package com.axiasoft.vote_back.admin.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CoinVO {
    private int round;
    private String coin_name;
    private String coin_symbol;
    private String coin_info;
}
