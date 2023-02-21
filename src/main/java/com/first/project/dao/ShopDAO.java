package com.first.project.dao;


import com.first.project.dto.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ShopDAO {


    List<DivEqSortDTO> btnList(DivEqSortDTO divEqSort);

    DivEqSortDTO hisList(String hisNum);

    int eqCount(String divEqSoNum);

    List<DivEqDTO> eqList(DivEqListDTO divEqList);

    DivEqDTO eqDetail(String divEqNum);

    List<DivEqSortDTO> cusBtnList(PageDTO paging1);

    void cusDBSet(ShopMemDTO shopMem);

    String cusDBView(String loginId);

    DivEqSortDTO cusSel(int rowNum);

    void eqBasketSet(ShopMemDTO shopMem);

    String eqBasketView(String loginId);

    void buyListSet(ShopMemDTO shopMem);

    void shopMemSet(String loginId);

    int eqPay(DivEqDTO divEq);


    void eqBasketReset(ShopMemDTO shopMem);
}
