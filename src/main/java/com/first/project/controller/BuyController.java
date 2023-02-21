package com.first.project.controller;

import com.first.project.dto.KakaoBuyDTO;
import com.first.project.dto.ReservationDTO;
import com.first.project.service.BuyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
public class BuyController {

    private final BuyService bsev;

    // 예약
    @PostMapping("/Reservation")
    public ModelAndView Reservation(@ModelAttribute ReservationDTO Rese,
                                    @ModelAttribute("BuyKind") String BuyKind,
                                    @ModelAttribute("BuyObjectName") String BuyObjectName){
        System.out.println("Rese" + Rese);

        return bsev.Res(Rese, BuyKind, BuyObjectName);
    }

    @GetMapping("/kakaoPayScuba")
    public String kakaoPayScuba() {

        return "kakaoPayScuba";
    }

    @GetMapping("/kakaoPayPool")
    public String kakaoPayPool() {

        return "kakaoPayPool";
    }

    @PostMapping("/buyScuba")
    public ModelAndView buyScuba(@ModelAttribute KakaoBuyDTO kakaoBuy){

        return bsev.buyScuba(kakaoBuy);
    }

    @PostMapping("/buyPool")
    public ModelAndView buyPool(@ModelAttribute KakaoBuyDTO kakaoBuy){

        return bsev.buyPool(kakaoBuy);
    }

    @PostMapping("/buyEquipment")
    public @ResponseBody String buyEquipment(@ModelAttribute KakaoBuyDTO kakaoBuy){
        bsev.buyEquipment(kakaoBuy);
        String data = null;
        
        return data;
    }

}
