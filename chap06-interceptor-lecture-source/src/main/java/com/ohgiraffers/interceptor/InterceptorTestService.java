package com.ohgiraffers.interceptor;

import org.springframework.stereotype.Service;

@Service
public class InterceptorTestService {
    public void test() {
        System.out.println("서비스 bean 동작 확인");
    }
}
