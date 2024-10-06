package com.bleachin.controller;

import com.bleachin.service.HelloService;
import com.bleachin.vo.Person;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @DubboReference
    private HelloService helloService;

    @RequestMapping("/hello")
    public String hello(Person person) {
        String result = helloService.sayHello(person);
        return "远程调用成功 获得结果: " + result;
    }
}
