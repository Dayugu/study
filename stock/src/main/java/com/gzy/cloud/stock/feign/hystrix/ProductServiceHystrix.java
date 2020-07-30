package com.gzy.cloud.stock.feign.hystrix;

import com.gzy.cloud.stock.feign.ProductFeignService;
import org.springframework.stereotype.Component;

@Component
public class ProductServiceHystrix implements ProductFeignService {
    @Override
    public String getProductInfo() {
        return "product服务中断，请稍后再试！";
    }
}
