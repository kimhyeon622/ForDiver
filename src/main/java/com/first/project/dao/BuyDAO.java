package com.first.project.dao;


import com.first.project.dto.KakaoBuyDTO;
import com.first.project.dto.PooltDTO;
import com.first.project.dto.ScubaPlacetDTO;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface BuyDAO {

    int point(String memId);

    ScubaPlacetDTO buyScubaList(String buyObjectName);

    PooltDTO buyPoolList(String buyObjectName);

    int buyScuba(KakaoBuyDTO kakaoBuy);

    int buyPool(KakaoBuyDTO kakaoBuy);

    int buyEquipment(KakaoBuyDTO kakaoBuy);

    void usePoint(String memId, String usePoint);
}
