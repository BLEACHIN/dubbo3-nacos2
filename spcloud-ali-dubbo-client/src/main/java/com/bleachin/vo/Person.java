package com.bleachin.vo;

import lombok.Data;

import java.io.Serializable;
/**
 * client vo对象
 * 注意：一定要实现Serializable接口进行序列化，否则调用会报错
 * @author bleachin
 */
@Data
public class Person implements Serializable {
	private String name;
	private Integer age;
}
