package com.wyq.trainBatch.controller;

import com.wyq.trainBatch.feign.businessFeign;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @Resource
    private businessFeign businessFeign;
    @GetMapping("/hello")
    public String hello()  {
        return businessFeign.hello();
    }
}
