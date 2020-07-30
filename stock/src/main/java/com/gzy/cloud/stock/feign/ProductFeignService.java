package com.gzy.cloud.stock.feign;

import com.gzy.cloud.stock.feign.hystrix.ProductServiceHystrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@FeignClient(value = "product",fallback = ProductServiceHystrix.class)
public interface ProductFeignService {

    @PostMapping(value = "/product/getProductInfo")
    public String getProductInfo();
}
