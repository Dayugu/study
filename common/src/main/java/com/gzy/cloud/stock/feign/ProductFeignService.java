package com.gzy.cloud.stock.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "product")
public interface ProductFeignService {

    @PostMapping(value = "/product/getProductInfo")
    public String getProductInfo(@RequestParam String productName);
}
