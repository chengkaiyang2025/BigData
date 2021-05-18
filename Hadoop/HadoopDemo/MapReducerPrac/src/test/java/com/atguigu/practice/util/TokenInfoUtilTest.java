package com.atguigu.practice.util;

import com.atguigu.practice.bean.TokenInfo;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokenInfoUtilTest {
    @Test
    public void decode() {
        TokenInfo tokenInfo = TokenInfoUtil.decode("eyJhbGciOiJIUzI1NiJ9.eyJzZXQiOiJ3ZWJzZXJ2ZXJzMTEiLCJwaG9uZSI6IjE4ODMzOTk5MDA3IiwiZ3NJZCI6Ijk1OSIsImlzcyI6ImF1dGgwIiwidXNlck5hbWUiOiJrdWFpamk2enUiLCJleHAiOjE2MTE3OTYxODEsInVzZXJJZCI6IjY5NDQ5Njc1MjQ2MjgyNzUyMCJ9.PvQANeMrfokoRqQlLBspZ7k6uTvwvV7ucKju9M7_N2A");
        System.out.println(tokenInfo);
        TokenInfo tokenInfo2 = TokenInfoUtil.decode("eyJhbGciOiJIUzI1NiJ9.eyJzZXQiOiIiLCJwaG9uZSI6IjE1MzIxNjU3MDM5IiwiZ3NJZCI6IjM2IiwiaXNzIjoiYXV0aDAiLCJ1c2VyTmFtZSI6ImRlbW8iLCJleHAiOjE2MTE4MjgyMzQsInVzZXJJZCI6IjczNjE3NjY5Njc3NTI0OTkyMCJ9.WoK09h9IJkfoY1Hbyx-nvN3hx9TmNZzNpqxU1GDfMkc");
        System.out.println(tokenInfo2);
        String cookie = "eyJhbGciOiJIUzI1NiJ9.eyJzZXQiOiJ3ZWJzZXJ2ZXJzMTM0IiwicGhvbmUiOiIxNTMyMTY1NzAzOSIsImdzSWQiOiIzNiIsImlzcyI6ImF1dGgwIiwidXNlck5hbWUiOiJkZW1vIiwiZXhwIjoxNjExNzk5MTMzLCJ1c2VySWQiOiI3MzYxNzY2OTY3NzUyNDk5MjAifQ.aXHNR5psWjpIWT4kOK5Ux5gcwBdxi8aUBps2SauqpBo";

        TokenInfo tokenInfo3 = TokenInfoUtil.decode(cookie);
        System.out.println(tokenInfo3);
//        TokenInfo tokenInfo4 = TokenInfoUtil.decode("假toekn");
        System.out.println(tokenInfo3);
    }
}