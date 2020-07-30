package com.gzy.cloud.product.controller;

import com.gzy.cloud.stock.feign.ProductFeignService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class ProductController {

    @Value("${server.port}")
    private String port;

    @RequestMapping(value = "/product/getProductInfo")
    public String getProductInfo(HttpServletRequest request){
        String message = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getServletPath();
        return "this is "+message;
    }

}
