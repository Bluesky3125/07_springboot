package com.ohgiraffers.handlermethod;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/first")
public class FirstController {

    /* 설명. 핸들러 메소드에서 반환형이 없을 경우 요청 경로를 반환한다.(view의 경로 및 이름) */
    @GetMapping("/register")
//    public String register() {
//        return "/first/register";
//    }
    // 반환값이 없으면, 요청한 경로로 자동으로 반환
    public void register() {
    }

    /* 설명. request는 사용자의 입력값(parameter)를 담는 용도로 쓰고
     *  Model은 백엔드에서 동적 페이지를 만들 때 사용하는 용도로 쓰자.
     * */
    @PostMapping("register")
    public String registerMenu(HttpServletRequest request, Model model) {
        String name = request.getParameter("name");
        int price = Integer.parseInt(request.getParameter("price"));
        int categoryCode = Integer.parseInt(request.getParameter("categoryCode"));

        /* 설명. 비즈니스 로직 이후 DB에 insert를 성공하고 돌아왔다는 가정 */
        String message = name + "을(를) 신규 메뉴 목록의 " + categoryCode + "번 카테고리에 " + price + "원으로 등록하였습니다";
        model.addAttribute("message", message);

        return "first/messagePrinter";
    }

    @GetMapping("modify")
    public void modify() {
    }

    /* 설명.
     *  request의 parameter로 넘어오는 값들의 key값과 일치하는 변수명(어노테이션 생략 가능)을 작성하고 @RequestParam
     *  어노테이션을 적용하면 알아서 값을 꺼내고 해당 매개변수의 자료형에 맞게 변환까지 해준다.(물론 가능한 경우)
     *  　
     *  1. defaultValue: 사용자의 입력 값이 없거나("") 아니면 request의 parameter 키값과 일치하지 않는
     *                   매개변수 사용 시 매개변수가 가질 기본값을 작성한다.
     *  2. name: request parameter의 키값과 다른 매개변수 명을 사용하고 싶을 때 request parameter의
     *           키값을 작성한다.
     * */
    @PostMapping("modify1")
    public String modify1(Model model,
                          @RequestParam(name = "name", defaultValue = "디폴트") String modifyName,
                          @RequestParam(name = "modifyPrice", defaultValue = "0") int modifyPrice
//                          int modifyPrice
    ) {  // 넘어온 parameter의 키값과 핸들러 메소드의 매개변수 이름이 같으면 생략 가능

        /* 설명. 넘어온 데이터들로 update를 하고 성공했다는 가정 */

        String message = modifyName + " 메뉴의 가격을 " + modifyPrice + "로 변경하였습니다.";
        model.addAttribute("message", message);

        return "first/messagePrinter";
    }

    @PostMapping("modify2")
    public String modify2(Model model,
                          @RequestParam Map<String, String> parameterMap
    ) {
        String modifyName = parameterMap.get("name");
        int modifyPrice = Integer.parseInt(parameterMap.get("modifyPrice"));

        String message = modifyName + " 메뉴의 가격을 " + modifyPrice + "로 변경하였습니다.";
        model.addAttribute("message", message);

        return "first/messagePrinter";
    }

    @GetMapping("search")
    public void searchMenu() {
    }

    /* 설명.
     *  핸들러 메소드에 우리가 작성한 클래스(bean X)를 매개변수로 작성하면 스프링이 객체를 만들어주고(기본 생성자)
     *  setter로 값도 주입해준다. 이러한 클래스의 객체를 '커맨드 객체'라고 부른다.
     *  (커맨드 객체는 기본 생성자와 setter가 필수)
     *  　
     *  @ModelAttribute 어노테이션을 활용하면 커맨드 객체를 모델에 attribute로 담아주며 이후 view의 재료로
     *  사용할 수 있다.(키 값 작성 유무에 따라 화면에서 활용하는 방법이 다르다.)
     * */
    @PostMapping("search")
    public String searchMenu(@ModelAttribute(name="menu") MenuDTO menu) {
        System.out.println("menu = " + menu);

        return "first/searchResult";
    }
}
