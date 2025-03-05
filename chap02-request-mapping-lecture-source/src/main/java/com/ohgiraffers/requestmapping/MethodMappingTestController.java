package com.ohgiraffers.requestmapping;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class MethodMappingTestController {
//    @RequestMapping(value = "/menu/regist") // request method 무관(GET 요청뿐 아니라 다른 요청도 받음)
    @RequestMapping(value = "/menu/regist", method= RequestMethod.GET)
    public String registMenu(Model model) { // Model은 SSR에서 쓰는 것으로 응답할 페이지의 재료가 된다.
        System.out.println("/menu/regist 경로의 GET 요청 받아보기");

        model.addAttribute("message", "(GET)신규 메뉴 등록용 핸들러 메소드 호출함...");
        // "핸들러 메소드"

        /* 설명. 핸들러 메소드에서 반환한 String은 응답 값이 아닌 view(html 파일 이름)이 된다. */
        return "mappingResult"; // 페이지(view)의 이름
    }

    @RequestMapping(value = "/menu/modify", method = RequestMethod.POST)
    public String modifyMenu(Model model) {
        model.addAttribute("message", "POST 방식의 메뉴 수정용 핸들러 메소드 호출함...");

        return "mappingResult";
    }

    @GetMapping("/menu/delete")
    public String getDeleteMenu(Model model) {
        model.addAttribute("message", "GET 방식의 메뉴 삭제용 핸들러 메소드 호출함...");

        return "mappingResult";
    }

    @PostMapping("/menu/delete")
    public String postDeleteMenu(Model model) {
        model.addAttribute("message", "POST 방식의 메뉴 삭제용 핸들러 메소드 호출함...");

        return "mappingResult";
    }
}
