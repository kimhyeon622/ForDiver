package com.first.project.dto;

import lombok.Data;
import org.apache.ibatis.type.Alias;

@Data
@Alias("point")
public class PointDTO {
    private int pointNum;       // 포인트 넘버
    private String memId;       // 사용자
    private int usablePoint;    // 사용가능한 포인트
}
