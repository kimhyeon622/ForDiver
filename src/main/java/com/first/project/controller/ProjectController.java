package com.first.project.controller;


import com.first.project.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
public class ProjectController {

    ModelAndView mav = new ModelAndView();

    private final HttpSession session;
    private final MemberService msvc;



    @GetMapping("/")
    public String home(){
        return "intro";
    }

    @GetMapping("/index")
    public String index() {
        return "index";
    }

    // 바다, 수영장 선택페이지
    @GetMapping("/selfReservation")
    public String selfReservation() {
        return "selfReservation";
    }

    // 바다 리스트
    @GetMapping("/selfOcean")
    public String selfOcean() {
        return "selfOcean";
    }

    // 수영장 리스트
    @GetMapping("/selfPool")
    public String selfPool() {
        return "selfPool";
    }


    /* 제주도 지역 */
    // 범섬
    @GetMapping("/tiger_island")
    public String tiger_island() {
        return "tiger_island";
    }

    // 섶섬 한개창
    @GetMapping("/Seopseom_Island_Small_Window")
    public String smail_Seopseom() {
        return "Small_Seopseom";
    }

    // 섶섬
    @GetMapping("/Seopseom_Island")
    public String Seopseom() {
        return "Seopseom";
    }

    // 문섬
    @GetMapping("/Munseom")
    public String Munseom() {
        return "Munseom";
    }


    /* 울릉도 지역 */
    // 곰바위
    @GetMapping("/Bear_rock")
    public String Bear_rock() {
        return "Bear_rock";
    }

    // 코끼리 바위
    @GetMapping("/Elephant_Rock")
    public String Elephant_Rock() {
        return "Elephant_Rock";
    }

    // 학포케이브
    @GetMapping("/cave")
    public String cave() {
        return "cave";
    }

    // 죽도
    @GetMapping("/Jukdo")
    public String Jukdo() {
        return "Jukdo";
    }


    /* 서울 지역 */
    // 올림픽수영장
    @GetMapping("/Olympic_Swimming_Pool")
    public String Olympic_Swimming_Pool() {
        return "Olympic_Swimming_Pool";
    }

    // 잠실 스킨스쿠버 다이빙
    @GetMapping("/Jamsil_Skin_Scuba_Diving")
    public String Jamsil_Skin_Scuba_Diving() {
        return "Jamsil_Skin_Scuba_Diving";
    }

    // 다이브라이프
    @GetMapping("/DiveLife")
    public String DiveLife() {
        return "DiveLife";
    }


    /* 경기도 지역 */
    // K-26
    @GetMapping("/K_26")
    public String K_26() {
        return "K_26";
    }

    // 딥스테이션
    @GetMapping("/DEEP_STATION")
    public String DEEP_STATION() {
        return "DEEP_STATION";
    }

    // 아쿠아라인
    @GetMapping("/Aqua_Line")
    public String Aqua_Line(){
        return "Aqua_Line";
    }

    // 월드컵 스킨스쿠버 다이빙풀
    @GetMapping("/World_Cup_Skin_Scuba_Diving_Pool")
    public String World_Cup_Skin_Scuba_Diving_Pool() {
        return "World_Cup_Skin_Scuba_Diving_Pool";
    }

    // 이벤트 목록
    @GetMapping("/eventList")
    public String eventList(){ return "EventList";}

    // 모달 로그인창
    @GetMapping("/Loginmodel")
    public String Loginmodel(){ return "Loginmodel";}

    // 회원 애니메이션
    @GetMapping("/userAmt")
    public String userAmt(){ return "userAmt"; }

}
