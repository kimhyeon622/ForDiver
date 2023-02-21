package com.first.project.dto;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import java.time.LocalDateTime;

@Data
@Alias("pComment")
public class PCommentDTO {
    private int pcmNum;
    private int pNum;
    private String memId;
    private int pcmParent;
    private String pcmContent;
    private LocalDateTime pcmDate;
    private int pcmBool; //1과 0으로 구분. delete 대신 update 사용해 시퀀스 유지 위함.
}
