package com.first.project.service;



import com.first.project.dto.DivEqDTO;
import com.first.project.dto.DivEqSortDTO;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

public interface ShopService {

    List<List<Object>> buttonList(DivEqSortDTO divEqSort);

    ModelAndView shopList(int page);

    List<Object> equipList(int page, String divEqSoNum);

    DivEqDTO eqDetail(String divEqNum);

    List<Object> buttonCustom(int page, int cusCurPage, String cbxList, String loginId);

    List<Object> cusBtnList(String loginId);

    List<Object> eqBasket(String divEqNum,int eqCount, String loginId);

    ModelAndView shopCheckout(String loginId);

    ModelAndView eqBuyList(String loginId, DivEqDTO eqBuyList);

    String customReset(String loginId);

    ModelAndView eqPay(List<String> eqList, String loginId);
}
