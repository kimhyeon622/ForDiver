package com.first.project.dto;

import lombok.Data;
import org.apache.ibatis.type.Alias;

@Data
@Alias("notice")
public class NOTICEDTO {

    private int NONUM;					// 공지번호
    private String NOTITLE;				// 공지제목
    private String NOCONTENT;		    // 공지내용
    private String NODATE;				// 공지시간


}

