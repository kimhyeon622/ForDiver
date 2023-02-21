package com.first.project.service;

import com.first.project.dao.BuyDAO;
import com.first.project.dto.KakaoBuyDTO;
import com.first.project.dto.PooltDTO;
import com.first.project.dto.ReservationDTO;
import com.first.project.dto.ScubaPlacetDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

@Service
@RequiredArgsConstructor
public class BuyServicelmpl implements BuyService {

    private ModelAndView mav;

    private final HttpSession session;

    private final BuyDAO bDao;

    @Override
    public ModelAndView Res(ReservationDTO Rese, String BuyKind, String BuyObjectName) {

        ModelAndView mav = new ModelAndView();
        String reDate = Rese.getReDate();
        int point = 0;

        try {
             point = bDao.point(Rese.getMemId());
        } catch (Exception e){
            System.out.println("Point 없음");
        }
        mav.addObject("point", point);

        if (BuyKind.equals("바다")) {
            ScubaPlacetDTO ScubaList = bDao.buyScubaList(BuyObjectName);
            mav.addObject("ScubaList", ScubaList);
            Rese.setReDateTime(reDate);
            mav.setViewName("kakaopayScuba");
        } else {
            PooltDTO PoolList = bDao.buyPoolList(BuyObjectName);
            mav.addObject("PoolList", PoolList);
            String reTime = Rese.getReTime();
            Rese.setReDateTime(reDate + " " + reTime);
            mav.setViewName("kakaopayPool");
        }


        mav.addObject("Rese", Rese);


        return mav;
    }


    @Override
    public ModelAndView buyScuba(KakaoBuyDTO kakaoBuy) {
        ModelAndView mav = new ModelAndView();

        int result = bDao.buyScuba(kakaoBuy);

        if(result > 0){
            bDao.usePoint(kakaoBuy.getMemId(), kakaoBuy.getUsePoint());
            mav.setViewName("redirect:/myBuyList?memId="+kakaoBuy.getMemId());
        } else{
            mav.setViewName("kakaoPayScuba");
        }

        return mav;
    }

    @Override
    public ModelAndView buyPool(KakaoBuyDTO kakaoBuy) {
        ModelAndView mav = new ModelAndView();

        int result = bDao.buyPool(kakaoBuy);

        if(result > 0){
            bDao.usePoint(kakaoBuy.getMemId(), kakaoBuy.getUsePoint());
            mav.setViewName("redirect:/myBuyList?memId="+kakaoBuy.getMemId());
        } else{
            mav.setViewName("kakaoPayPool");
        }

        return mav;
    }

    @Override
    public String buyEquipment(KakaoBuyDTO kakaoBuy) {

        int result = bDao.buyEquipment(kakaoBuy);
        String data = null;

        if(result > 0){
            data = "OK";
        } else{
            data = "NO";
        }

        return data;
    }

}
