package com.gzy.cloud.product.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class HelloController {

    @Value("${server.port}")
    private String port;

    @GetMapping(value = "/hello")
    public String hello(){

        return "hello , this is spring cloud client - product";
    }


    @GetMapping(value = "/product/getProduct")
    public String get(){

        return "hello , this is Apple Mac!";
    }

    /**
     * 提供的一个restful服务，返回内容格式：服务协议://服务器地址:服务端口/服务uri
     *
     * @param request
     * @return
     */
    @RequestMapping("/info")
    public String info(HttpServletRequest request) {
        String message = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getServletPath();
        return message;
    }


}
