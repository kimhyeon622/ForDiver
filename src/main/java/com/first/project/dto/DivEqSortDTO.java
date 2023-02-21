package com.first.project.dto;

import lombok.Data;
import org.apache.ibatis.type.Alias;

@Data
@Alias("divEqSort")
public class DivEqSortDTO {

    private String divEqSoNum;
    private String divEqSoName;
    private String divEqSoPhotoName;

    private String cbxBool = "";

}
