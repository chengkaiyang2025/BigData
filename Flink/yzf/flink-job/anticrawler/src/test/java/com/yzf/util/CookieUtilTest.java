package com.yzf.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class CookieUtilTest {
    @Test
    public void testAccessToken1(){
        String cookie1 = "refresh_token=0e17f9cb4c624943bdbb109aaffcbcb8; access_token=eyJhbGciOiJIUzI1NiJ9.eyJzZXQiOiJ3ZWJzZXJ2ZXJzMTM0IiwicGhvbmUiOiIxNTMyMTY1NzAzOSIsImdzSWQiOiIzNiIsImlzcyI6ImF1dGgwIiwidXNlck5hbWUiOiJkZW1vIiwiZXhwIjoxNjExNzk5MTMzLCJ1c2VySWQiOiI3MzYxNzY2OTY3NzUyNDk5MjAifQ.aXHNR5psWjpIWT4kOK5Ux5gcwBdxi8aUBps2SauqpBo; SESSION=f7aacd3d-4eac-4b4f-8754-1f57693c10d9";
        System.out.println(CookieUtil.getCookieValueByCookieKey(cookie1,"access_token"));
    }
    @Test
    public void testAccessToken2(){
        String cookie1 = "\"refresh_token=0e17f9cb4c624943bdbb109aaffcbcb8; access_token=eyJhbGciOiJIUzI1NiJ9.eyJzZXQiOiJ3ZWJzZXJ2ZXJzMTM0IiwicGhvbmUiOiIxNTMyMTY1NzAzOSIsImdzSWQiOiIzNiIsImlzcyI6ImF1dGgwIiwidXNlck5hbWUiOiJkZW1vIiwiZXhwIjoxNjExNzk5MTMzLCJ1c2VySWQiOiI3MzYxNzY2OTY3NzUyNDk5MjAifQ.aXHNR5psWjpIWT4kOK5Ux5gcwBdxi8aUBps2SauqpBo; SESSION=f7aacd3d-4eac-4b4f-8754-1f57693c10d9\"";
        System.out.println(CookieUtil.getCookieValueByCookieKey(cookie1,"access_token"));
    }

    @Test
    public void testError1(){
        String cookie1 = "refresh_token=0e17f9cb4c624943bdbb109aaffcbcb8;ZXQiOiJ3ZWJzZXJ2ZXJzMTM0IiwicGhvbmUiOiIxNTMyMTY1NzAzOSIsImdzSWQiOiIzNiIsImlzcyI6ImF1dGgwIiwidXNlck5hbWUiOiJkZW1vIiwiZXhwIjoxNjExNzk5MTMzLCJ1c2VySWQiOiI3MzYxNzY2OTY3NzUyNDk5MjAifQ.aXHNR5psWjpIWT4kOK5Ux5gcwBdxi8aUBps2SauqpBo; SESSION=f7aacd3d-4eac-4b4f-8754-1f57693c10d9";
        String c =CookieUtil.getCookieValueByCookieKey(cookie1,"access_toke");
//        System.out.println();
    }

    @Test
    public void testError2(){
        String cookie1 = "\"refresh_token=0e17f9cb4c624943bdbb109aaffcbcb8";
        String c =CookieUtil.getCookieValueByCookieKey(cookie1,"access_toke");
    }
//    @Test
//    public void testError3(){
//        CookieUtil.getCookieValueByCookieKey(null,"access_toke");
//    }
    @Test
    public void testError4(){
        String c =CookieUtil.getCookieValueByCookieKey("","access_toke");
    }
}