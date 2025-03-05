package com.ohgiraffers.chap01;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfiguration {
    @Value("${test.value}")
    private String testValue;

    @Value("${test.age}")
    private String testAge;

    @Value("${username}")
    private String username;

    @Bean
    public Object propertyReadTest() {
        System.out.println("testValue: " + testValue);
        System.out.println("testAge: " + testAge);
        System.out.println("username: " + username);

        return new Object();
    }
}
