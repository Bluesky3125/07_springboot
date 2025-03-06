package com.ohgiraffers.viewresolver;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/*")
public class MainController {

//    @RequestMapping("/")
//    public String defaultLocation() {
//        return "main";
//    }
//
//    @RequestMapping("/main")
//    public void main() {}

    @RequestMapping(value = {"/","/main"})
    public String main() {
        return "main";
    }
}
