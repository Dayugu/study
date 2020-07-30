package com.gzy.cloud.ribbon.controller;

import com.gzy.cloud.ribbon.service.LoadBalanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoadBalanceController {
    @Autowired
    LoadBalanceService loadBalanceService;

    @GetMapping("/rib/getPort")
    public String getPortTest(){
       return loadBalanceService.getInfo();
    }

}
