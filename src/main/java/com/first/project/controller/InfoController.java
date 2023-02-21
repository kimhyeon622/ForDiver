package com.first.project.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class InfoController {

    @GetMapping("divingClub")
    public String divingClub(){
        return "DivingClub";
    }

    //환경 단체
    @GetMapping("donateList")
    public String donateList(){
        return "DonateList";
    }

    @GetMapping("skinInfo")
    public String skinInfo(){
        return "SkinInfo";
    }

    @GetMapping("scubaInfo")
    public String scubaInfo(){return "ScubaInfo"; }

    @GetMapping("divingSpot")
    public String divingSpot() {
        return "DivingSpot";
    }
}
