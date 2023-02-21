package com.first.project.service;

import com.first.project.dto.PointDTO;

public interface CrisisService {
    int getPoint(PointDTO point);

    PointDTO checkPoint(String memId);
}
