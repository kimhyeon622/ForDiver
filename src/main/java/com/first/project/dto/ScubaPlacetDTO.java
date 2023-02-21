package com.first.project.dto;

import lombok.Data;
import org.apache.ibatis.type.Alias;

@Data
@Alias("scuba")
public class ScubaPlacetDTO {

    private int ScupNum;
    private String ScupName;
    private int ScupPrice;

}
