package com.first.project.controller;

import com.first.project.dto.CrisisDTO;
import com.first.project.dto.PointDTO;
import com.first.project.service.CrisisService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
public class CrisisController {

    private ModelAndView mav = new ModelAndView();

    private final CrisisService csvc;

    //해양위기 자가진단 페이지로 이동
    @GetMapping("selfCheck")
    public String selfCheckForm(){
        return "SelfCheck";
    }

    //해양위기 자가진단 결과 페이지로 이동
    @PostMapping("selfCheckResult")
    public ModelAndView selfCheck(@ModelAttribute CrisisDTO crisis){

        mav.addObject("crisis",crisis);
        mav.setViewName("SelfCheckResult");

        return mav;
    }

    //해양위기 정보 페이지 : 해양 위기 현황과 실천 사례 등을 볼 수 있음. 벤치마킹은 WTF Ocean
    @GetMapping("crisisInfo")
    public String crisisInfo(){
        return "CrisisInfo";
    }

    //포인트 획득
    @PostMapping("getPoint")
    public @ResponseBody String getPoint(@ModelAttribute PointDTO point){

        String result = "";

        System.out.println("컨트롤러");

        int check = csvc.getPoint(point);

        System.out.println("포인트 : " + point);

        if(check > 0){
            result = "OK";
        } else {
            result = "NO";
        }

        return result;
    }

    //포인트 보유 여부 확인
    @PostMapping("checkPoint")
    public @ResponseBody String checkPoint(@RequestParam("memId") String memId){

        String result = "";

        PointDTO check = csvc.checkPoint(memId);

        if(check == null){
            result = "OK";
        } else {
            result = "NO";
        }

        return result;
    }

}
