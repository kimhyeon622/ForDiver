package com.first.project.dto;

import lombok.Data;
import org.apache.ibatis.type.Alias;

@Data
@Alias("pool")
public class PooltDTO {
    private int PoNum;
    private String PoName;
    private int PoPrice;
}
