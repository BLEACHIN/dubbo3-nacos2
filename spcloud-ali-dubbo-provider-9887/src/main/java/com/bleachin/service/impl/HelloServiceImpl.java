package com.bleachin.service.impl;

import com.bleachin.service.HelloService;
import com.bleachin.vo.Person;
import org.apache.dubbo.config.annotation.DubboService;

@DubboService
public class HelloServiceImpl implements HelloService {
    @Override
    public String sayHello(Person person) {
        String name = person.getName();
        return "你好啊 " + name;
    }
}
