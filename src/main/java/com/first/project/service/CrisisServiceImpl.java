package com.first.project.service;

import com.first.project.dao.CrisisDAO;
import com.first.project.dto.PointDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CrisisServiceImpl implements CrisisService{

    private final CrisisDAO cdao;
    @Override
    public int getPoint(PointDTO point) {
        return cdao.getPoint(point);
    }

    @Override
    public PointDTO checkPoint(String memId) {
        return cdao.checkPoint(memId);
    }
}
