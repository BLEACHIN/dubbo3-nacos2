# Dubbo3整合Nacos2
网上Dubbo3整合Nacos2的例子比较少，或者说有例子但是还需要花些时间踩坑才能完全跑起来并且访问得通。。。现在通过我认为极简的例子来展示Dubbo3如何整合Nacos2。

依赖如下：
springboot: 2.5.6
springcloud: 2020.0.4
springcloud alibaba: 2021.0.5.0
dubbo-spring-boot-starter: 3.2.14

步骤如下：
1. nacos server2安装、启动。
   这个网上有，坑相对比较少。

2. 引入依赖。
   父工程。
```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.5.6</version>
    </parent>

    <groupId>com.bleachin</groupId>
    <artifactId>spring-cloud-alibaba-demo</artifactId>
    <version>1.0-SNAPSHOT</version>
    <packaging>pom</packaging>

    <properties>
        <maven.compiler.source>8</maven.compiler.source>
        <maven.compiler.target>8</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <maven.compiler.source>1.8</maven.compiler.source>
        <maven.compiler.target>1.8</maven.compiler.target>
        <junit.version>4.12</junit.version>
        <log4j.version>1.2.17</log4j.version>
        <lombok.version>1.16.18</lombok.version>
    </properties>

    <dependencyManagement>
        <dependencies>
            <!--Spring Cloud 的版本信息-->
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>2020.0.4</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
            <!--Spring Cloud Alibaba 的版本信息-->
            <dependency>
                <groupId>com.alibaba.cloud</groupId>
                <artifactId>spring-cloud-alibaba-dependencies</artifactId>
                <!-- <version>2021.1</version> -->
                <!-- nacos-client版本提到2.0 -->
                <version>2021.0.5.0</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
            <!-- Dubbo -->
            <dependency>
                <groupId>org.apache.dubbo</groupId>
                <artifactId>dubbo-spring-boot-starter</artifactId>
                <version>3.2.14</version>
            </dependency>
            <!-- 与spring boot版本不兼容，不要引入 -->
            <!-- <dependency> -->
            <!--     <groupId>org.apache.dubbo</groupId> -->
            <!--     <artifactId>dubbo-bom</artifactId> -->
            <!--     <version>2.7.8</version> -->
            <!--     <type>pom</type> -->
            <!--     <scope>import</scope> -->
            <!-- </dependency> -->
            <!-- <dependency> -->
            <!--     <groupId>com.alibaba.cloud</groupId> -->
            <!--     <artifactId>spring-cloud-starter-dubbo</artifactId> -->
            <!--     <version>2021.1</version> -->
            <!--     <type>pom</type> -->
            <!--     <scope>import</scope> -->
            <!-- </dependency> -->
        </dependencies>
    </dependencyManagement>

    <modules>
        <!-- dubbo 服务提供者 client -->
        <module>spcloud-ali-dubbo-client</module>
        <!-- dubbo 服务提供者 -->
        <module>spcloud-ali-dubbo-provider-9887</module>
        <!-- dubbo 服务消费者 -->
        <module>spcloud-ali-dubbo-consumer-9886</module>
    </modules>
</project>
```

## 搭建dubbo client
4. 新建spcloud-ali-dubbo-client模块。
   新增pom.xml。
```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>com.bleachin</groupId>
        <version>1.0-SNAPSHOT</version>
        <artifactId>spring-cloud-alibaba-demo</artifactId>
    </parent>

    <artifactId>spcloud-ali-dubbo-client</artifactId>

    <properties>
        <java.version>1.8</java.version>
    </properties>

    <dependencies>
        <!-- 公共的依赖 start -->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
        </dependency>
        <!-- 公共的依赖 end -->
    </dependencies>
</project>
```

新增接口和入参对象。
```
/**
 * rpc远程调用接口
 * @author bleachin
 */
public interface HelloService {
    String sayHello(Person person);
}
```
```
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
```


## 搭建dubbo服务提供者
5. 新建spcloud-ali-dubbo-provider-9887模块，实现client接口。
   新增pom.xml。
```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>com.bleachin</groupId>
        <version>1.0-SNAPSHOT</version>
        <artifactId>spring-cloud-alibaba-demo</artifactId>
    </parent>

    <artifactId>spcloud-ali-dubbo-provider-9887</artifactId>

    <properties>
        <java.version>1.8</java.version>
    </properties>

    <dependencies>
        <!-- 公共的依赖 start -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <!--spring-boot程序的监控系统，可以实现系统健康的检测
        可以通过http://localhost:10000/actuator 看到相关信息-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-autoconfigure</artifactId>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
        </dependency>
        <!-- 公共的依赖 end -->

        <!-- 引入nacos服务注册与发现依赖-->
        <dependency>
            <groupId>com.alibaba.cloud</groupId>
            <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
        </dependency>
        <!-- dubbo starter -->
        <dependency>
            <groupId>org.apache.dubbo</groupId>
            <artifactId>dubbo-spring-boot-starter</artifactId>
        </dependency>
        <!-- &lt;!&ndash; dubbo &ndash;&gt; -->
        <!-- <dependency> -->
        <!--     <groupId>org.apache.dubbo</groupId> -->
        <!--     <artifactId>dubbo</artifactId> -->
        <!-- </dependency> -->
        <!-- <dependency> -->
        <!--     <groupId>org.apache.dubbo</groupId> -->
        <!--     <artifactId>dubbo-registry-nacos</artifactId> -->
        <!-- </dependency> -->
        <!-- &lt;!&ndash;使用dubbo协议 要引入dubbo-rpc-dubbo&ndash;&gt; -->
        <!-- <dependency> -->
        <!--     <groupId>org.apache.dubbo</groupId> -->
        <!--     <artifactId>dubbo-rpc-dubbo</artifactId> -->
        <!-- </dependency> -->

        <!-- dubbo client依赖 -->
        <dependency>
            <groupId>com.bleachin</groupId>
            <artifactId>spcloud-ali-dubbo-client</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <excludes>
                        <exclude>
                            <groupId>org.projectlombok</groupId>
                            <artifactId>lombok</artifactId>
                        </exclude>
                    </excludes>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```

新增application.yml。
```
server:
  port: 9887
spring:
  application:
    name: spcloud-ali-dubbo-provider-9887
  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848 #配置 Nacos 地址
dubbo:
  application:
    name: spcloud-ali-dubbo-provider-9887-dubbo #dubbo服务名
  protocol:
    name: dubbo #协议名称
    #host: localhost #本机ip地址
    #协议端口，指定dubbo服务的tcp端口
    #默认为20880，设置-1表示从20880开始自增端口，避免端口冲突
    # port: 8887
    port: -1
  registry: #配置并启用 Nacos
    address: nacos://localhost:8848
    # Dubbo3 默认采用 “应用级服务发现 + 接口级服务发现” 的双注册模式
    # 因此会发现应用级服务（应用名）和接口级服务（接口名）同时出现在nacos控制台
    # 可以通过配置 register-mode来改变注册行为，interface为接口级服务发现
    #register-mode: interface
  scan: #Dubbo服务实现类扫描基准包的位置
    base-packages: com.bleachin.** #扫描的接口包位置
#  monitor:
#    protocol: registry # 监控中心配置，从注册中心发现监控中心地址

#配置暴露所有的监控点
management:
  endpoints:
    web:
      exposure:
        include: '*'
```


实现client接口，重点是打上@DubboService注解。
```
@DubboService
public class HelloServiceImpl implements HelloService {
    @Override
    public String sayHello(Person person) {
        String name = person.getName();
        return "你好啊 " + name;
    }
}
```

新增启动类，重点是打上@EnableDubbo启用dubbo。
```
@SpringBootApplication
@EnableDubbo
// dubbo3需要使用nacos 2.0以上的版本
public class SpCloudAliDubboProv9887Application {
    public static void main(String[] args) {
        SpringApplication.run(SpCloudAliDubboProv9887Application.class, args);
    }
}
```


## 搭建dubbo服务消费者
6. 新建spcloud-ali-dubbo-consumer-9886模块，调用client接口。
   新增pom.xml。
```
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>com.bleachin</groupId>
        <version>1.0-SNAPSHOT</version>
        <artifactId>spring-cloud-alibaba-demo</artifactId>
    </parent>

    <artifactId>spcloud-ali-dubbo-consumer-9886</artifactId>

    <properties>
        <java.version>1.8</java.version>
    </properties>

    <dependencies>
        <!-- 公共的依赖 start -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <!--spring-boot程序的监控系统，可以实现系统健康的检测
        可以通过http://localhost:10000/actuator 看到相关信息-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-autoconfigure</artifactId>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
        </dependency>
        <!-- 公共的依赖 end -->

        <!-- 引入nacos服务注册与发现依赖-->
        <dependency>
            <groupId>com.alibaba.cloud</groupId>
            <artifactId>spring-cloud-starter-alibaba-nacos-discovery</artifactId>
        </dependency>
        <!-- dubbo starter -->
        <dependency>
            <groupId>org.apache.dubbo</groupId>
            <artifactId>dubbo-spring-boot-starter</artifactId>
        </dependency>
        <!-- &lt;!&ndash; dubbo &ndash;&gt; -->
        <!-- <dependency> -->
        <!--     <groupId>org.apache.dubbo</groupId> -->
        <!--     <artifactId>dubbo</artifactId> -->
        <!-- </dependency> -->
        <!-- <dependency> -->
        <!--     <groupId>org.apache.dubbo</groupId> -->
        <!--     <artifactId>dubbo-registry-nacos</artifactId> -->
        <!-- </dependency> -->

        <!-- dubbo client依赖 -->
        <dependency>
            <groupId>com.bleachin</groupId>
            <artifactId>spcloud-ali-dubbo-client</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <excludes>
                        <exclude>
                            <groupId>org.projectlombok</groupId>
                            <artifactId>lombok</artifactId>
                        </exclude>
                    </excludes>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```

新增application.yml。
```
server:
  port: 9886
spring:
  application:
    name: spcloud-ali-dubbo-provider-9886
  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848 #配置 Nacos 地址
dubbo:
  application:
    name: spcloud-ali-dubbo-provider-9886-dubbo #dubbo服务名
  protocol:
    name: dubbo #协议名称
    #host: localhost #本机ip地址
    #协议端口，指定dubbo服务的tcp端口
    #默认为20880，设置-1表示从20880开始自增端口，避免端口冲突
    # port: 8886
    port: -1
  registry: #配置并启用 Nacos
    #Dubbo3.0.0版本以后，增加了是否注册消费者的参数
    #如果需要将消费者注册到nacos上设置为true，默认是false。
    #加上这个参数后dubbo-admin才能看到我们的消费者
    address: nacos://localhost:8848?register-consumer-url=true
#  monitor:
#    protocol: registry # 监控中心配置，从注册中心发现监控中心地址

#配置暴露所有的监控点
management:
  endpoints:
    web:
      exposure:
        include: '*'
```


调用client接口。
```
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
```

新增启动类，重点是打上@EnableDubbo启用dubbo。
```
@SpringBootApplication
@EnableDubbo
public class SpCloudAliDubboCons9886Application {
    public static void main(String[] args) {
        SpringApplication.run(SpCloudAliDubboCons9886Application.class, args);
    }
}
```

启动nacos server，spcloud-ali-dubbo-provider-9887，spcloud-ali-dubbo-consumer-9886。

浏览器访问：http://localhost:9886/hello?name=123
远程调用成功 获得结果: 你好啊 123
