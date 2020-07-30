package com.gzy.cloud.stock.feign.hystrix;

import com.gzy.cloud.stock.feign.ProductFeignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ProductFeignServiceHystrix implements ProductFeignService {

    public String getProductInfo(String productName) {
        log.warn("调用product服务失败！");
        return "调用product服务失败!";
    }
}
