package com.first.project.dto;

import lombok.Data;
import org.apache.ibatis.type.Alias;
import java.time.LocalDateTime;

@Data
@Alias("code")
public class CodeDTO {

    private int codeNum; //게시글 번호
    private String memId; //회원 아이디
    private String codeTitle; //게시글 제목
    private String summercode; //태그
    private LocalDateTime codeDate; //날짜
    private int codeHit; //조회수

}
