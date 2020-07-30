package com.gzy.cloud.ribbon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class LoadBalanceService {


    @Autowired
    private RestTemplate restTemplate;

    public String getInfo(){

        String message = null;

        System.out.println("调用 服务 EUREKA-CLIENT/info");
        message = restTemplate.getForObject("http://PRODUCT/info", String.class);

        System.out.println("服务 EUREKA-CLIENT/info 返回信息 : " + message);
        System.out.println("调用 服务 EUREKA-CLIENT/info 成功！");
        System.out.println("调用 服务 EUREKA-CLIENT/info 成功！");

        return message;
    }


}
