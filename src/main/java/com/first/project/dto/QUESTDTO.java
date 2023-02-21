package com.first.project.dto;

import lombok.Data;
import org.apache.ibatis.type.Alias;

@Data
@Alias("quest")
public class QUESTDTO {

    private int QUNUM;					// 문의번호
    private String MEMID;					// 아이디
    private String QUCONTENT;				    // 문의내용
    private String QUANSWER;				// 답변내용
    private String QUDATE;				// 문의 작성일
    private String QUANDATE;				// 답변 작성일

}

