package com.first.project.dto;

import lombok.Data;
import org.apache.ibatis.type.Alias;

@Data
@Alias("Rese")
public class ReservationDTO {
    private String ObjectName;
    private String memId;
    private String reDate;
    private String reTime;
    private int rePeople;
    private String reDateTime;
    private int rePay;



}
