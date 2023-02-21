package com.first.project.dto;

import lombok.Data;
import org.apache.ibatis.type.Alias;

@Data
@Alias("restore")
public class RESTOREDTO {

    private int RESNUM;					// 복구번호
    private String MEMID;				// 아이디
    private String RESREASON;		    // 복구사유
    private String RESSENDDATE;		    // 복구신청일자


}

