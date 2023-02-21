package com.first.project.dto;

import lombok.Data;
import org.apache.ibatis.type.Alias;

@Data
@Alias("commentLike")
public class CommentLikeDTO {
    private int clNum;
    private int scNum;
    private String memId;
}
