package com.first.project.dto;

import lombok.Data;
import org.apache.ibatis.type.Alias;

@Data
@Alias("kakaoBuy")
public class KakaoBuyDTO {

    private String memId;
    private String buyPay;
    private String usePoint;
    private String reDateTime;
    private String rePeople;
    private String scubaName;
    private String poolName;
    private String equitmentName;
}
