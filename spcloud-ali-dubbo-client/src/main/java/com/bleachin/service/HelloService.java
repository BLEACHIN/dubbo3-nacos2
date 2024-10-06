package com.bleachin.service;

import com.bleachin.vo.Person;
/**
 * rpc远程调用接口
 * @author bleachin
 */
public interface HelloService {
    String sayHello(Person person);
}
