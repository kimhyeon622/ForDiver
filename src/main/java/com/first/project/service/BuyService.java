package com.first.project.service;


import com.first.project.dto.KakaoBuyDTO;
import com.first.project.dto.ReservationDTO;
import org.springframework.web.servlet.ModelAndView;

public interface BuyService {

    ModelAndView Res(ReservationDTO Rese, String BuyKind, String BuyObjectName);

    ModelAndView buyScuba(KakaoBuyDTO kakaoBuy);


    ModelAndView buyPool(KakaoBuyDTO kakaoBuy);

    String buyEquipment(KakaoBuyDTO kakaoBuy);
}
