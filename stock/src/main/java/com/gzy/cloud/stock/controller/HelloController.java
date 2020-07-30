package com.gzy.cloud.stock.controller;

import com.gzy.cloud.stock.feign.ProductFeignService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Api(description = "stock产品服务")
@RestController
public class HelloController {

    @Resource
    private ProductFeignService productFeignService;

    @ApiOperation(value = "查询库存信息",notes = "查询库存信息")
    @GetMapping(value = "/stock/getProductStock")
    public String get(){
        String productInfo = productFeignService.getProductInfo();
        return "hello , this is " + productInfo + " stock : 10";
    }
    @GetMapping(value = "/hello")
    public String hello(){

        return "hello , this is spring cloud client - product";
    }


}
