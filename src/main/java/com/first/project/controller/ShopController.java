package com.first.project.controller;

import com.first.project.dto.DivEqDTO;
import com.first.project.dto.DivEqSortDTO;
import com.first.project.service.ShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ShopController {

    ModelAndView mav = new ModelAndView();

    private final HttpSession session;
    private final ShopService ssvc;

    @GetMapping("/shopList")
    public ModelAndView shopList(@RequestParam(value="page", defaultValue = "1") int page){



        return ssvc.shopList(page);
    }

    @PostMapping("/buttonList")
    public @ResponseBody List<List<Object>> buttonList(@ModelAttribute DivEqSortDTO divEqSort) {

        return ssvc.buttonList(divEqSort);
    }

    @PostMapping("/cusBtnList")
    public @ResponseBody List<Object> cusBtnList(@RequestParam String loginId) {

        return ssvc.cusBtnList(loginId);
    }


    @PostMapping("/customReset")
    public @ResponseBody String customReset(@RequestParam String loginId) {

        return ssvc.customReset(loginId);
    }

    @PostMapping("/buttonCustom")
    public @ResponseBody List<Object> buttonCustom(@RequestParam int page,@RequestParam int cusCurPage ,@RequestParam String cbxList, @RequestParam String loginId) {

        return ssvc.buttonCustom(page, cusCurPage, cbxList, loginId);
    }


    @PostMapping("/equipList")
    public @ResponseBody List<Object> equipList(@RequestParam int page,@RequestParam String divEqSoNum) {

        return ssvc.equipList(page, divEqSoNum);
    }

    @PostMapping("/eqDetail")
    public @ResponseBody DivEqDTO eqDetail(@RequestParam String divEqNum) {

        return ssvc.eqDetail(divEqNum);
    }

    @PostMapping("/eqBasket")
    public @ResponseBody List<Object> eqBasket(@RequestParam String divEqNum ,@RequestParam int eqCount, @RequestParam String loginId) {

        return ssvc.eqBasket(divEqNum, eqCount, loginId);
    }

    @GetMapping("/shopCheckout")
    public ModelAndView shopCheckout() {

        ModelAndView mav = new ModelAndView();

        String loginId = null;
        try{
            loginId = session.getAttribute("loginId").toString();
        }catch (Exception e){
            mav.setViewName("index");
            return mav;
        }

        return ssvc.shopCheckout(loginId);
    }

    @PostMapping("/eqBuyList")
    public ModelAndView eqBuyList(@ModelAttribute DivEqDTO eqBuyList) {

        ModelAndView mav = new ModelAndView();

        // DTO중에 장비번호를 divEqNum, 선택 개수를 totalPriceStr 로 가져온다.
        System.out.println(eqBuyList);

        String loginId = null;
        try{
            loginId = session.getAttribute("loginId").toString();
        }catch (Exception e){
            mav.setViewName("index");
            return mav;
        }
        return ssvc.eqBuyList(loginId, eqBuyList);
    }

    // 결제 성공시 장바구니 초기화 & DB에서 수량 감소
    @GetMapping("/eqCntDown")
    public String eqPay(@RequestParam("eqList") List<String> eqList)  {


        System.out.println("eqList : " + eqList);
        String loginId = null;
        try{
            loginId = session.getAttribute("loginId").toString();
            System.out.println("아이디 성공");
            mav = ssvc.eqPay(eqList, loginId);
        }catch (Exception e){
            System.out.println("아이디 실패");
        }


        return "redirect:/myBuyList?memId="+loginId;
    }


}
