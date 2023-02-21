package com.first.project.dto;

import lombok.Data;
import org.apache.ibatis.type.Alias;

@Data
@Alias("pcLike")
public class PCommentLikeDTO {
    private int pclNum;
    private int pcmNum;
    private String memId;
}
