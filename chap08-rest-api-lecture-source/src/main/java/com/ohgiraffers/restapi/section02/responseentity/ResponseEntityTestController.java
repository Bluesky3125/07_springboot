package com.ohgiraffers.restapi.section02.responseentity;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/entity")
public class ResponseEntityTestController {

    /* 설명.
     *  ResponseEntity란?
     *   HttpRequest를 응답하기 위해 httpEntity를 상속받아 HttpStatus, HttpHeaders, HttpBody를
     *  정의하여 사용하는 클래스이다.
    *  */
    private List<UserDTO> users;

    public ResponseEntityTestController() {
        this.users = new ArrayList<>();
        users.add(new UserDTO(1, "user01", "pass01", "홍길동", new java.util.Date()));
        users.add(new UserDTO(2, "user02", "pass02", "유관순", new java.util.Date()));
        users.add(new UserDTO(3, "user03", "pass03", "이순신", new java.util.Date()));
    }

    /* 설명. 1. ResponseEntity 생성자를 이용한 응답 */
    @GetMapping("/users")
    public ResponseEntity<ResponseMessage> findAllUsers() {
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(
                new MediaType("application", "json", Charset.forName("UTF-8")));

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("users", users);

        ResponseMessage responseMessage = new ResponseMessage(200, "조회 성공!", responseMap);

        return new ResponseEntity<>(responseMessage, headers, HttpStatus.OK);
    }

    /* 설명. 2. 빌더를 활용한 메소드 체이닝 방식으로 ResponseEntity 객체 만들기(요즘 유행) */
    @GetMapping("/users/{userNo}")
    public ResponseEntity<ResponseMessage> findUserByNo(@PathVariable("userNo") int userNo) {
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(
                new MediaType("application", "json", Charset.forName("UTF-8")));

        UserDTO foundUser = users.stream()
                                 .filter(user -> user.getNo() == userNo)
                                 .collect(Collectors.toList()).get(0);

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("foundUser", foundUser);

        return ResponseEntity.ok()
                             .headers(headers)
                             .body(new ResponseMessage(200, "조회성공!", responseMap));
    }

    @PostMapping("/user")
    public ResponseEntity<?> registUser(@RequestBody UserDTO newUser) {
        System.out.println("newUser = " + newUser);

        int lastUserNo = users.get(users.size() - 1).getNo();   // 컬렉션에 담긴 마지막 회원의 번호
        newUser.setNo(lastUserNo + 1);
        
        users.add(newUser);                                     // 컬렉션에 회원 추가

        return ResponseEntity
                .created(URI.create("/entity/users/" + (lastUserNo + 1)))
                .build();
    }

    @PutMapping("users/{userNo}")
    public ResponseEntity<?> modifyUser(@RequestBody UserDTO modifyInfo, @PathVariable int userNo) {

        UserDTO foundUser =
                users.stream().filter(user -> user.getNo() == userNo)
                        .collect(Collectors.toList()).get(0);

        foundUser.setId(modifyInfo.getId());
        foundUser.setPwd(modifyInfo.getPwd());
        foundUser.setName(modifyInfo.getName());

        return ResponseEntity
                .created(URI.create("/entity/users/" + userNo))
                .build();
    }

    @DeleteMapping("users/{userNo}")
    public ResponseEntity<?> removeUser(@RequestBody UserDTO modifyInfo, @PathVariable int userNo) {

        UserDTO foundUser =
                users.stream().filter(user -> user.getNo() == userNo)
                        .collect(Collectors.toList()).get(0);

        users.remove(foundUser);

        return ResponseEntity
                .noContent()    // 204
                .build();
    }
}
