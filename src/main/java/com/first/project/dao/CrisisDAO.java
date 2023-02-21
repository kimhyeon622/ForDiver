package com.first.project.dao;

import com.first.project.dto.PointDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CrisisDAO {
    int getPoint(PointDTO point);

    PointDTO checkPoint(String memId);
}
