package com.wyq.trainMember;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.env.Environment;

@SpringBootApplication
@EnableAspectJAutoProxy
@MapperScan("com.wyq.trainMember.mapper")
@ComponentScan(basePackages = {"com.wyq",
        "com.wyq.trainCommon"})
@EnableDiscoveryClient
public class memberApplication {
    private static final Logger LOG = LoggerFactory.getLogger(memberApplication.class);

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(memberApplication.class);
        Environment env = app.run(args).getEnvironment();
        LOG.info("启动成功！！");
        LOG.info("测试地址: \thttp://127.0.0.1:{}{}/hello", env.getProperty("server.port"), env.getProperty("server.servlet.context-path"));
        LOG.info("文档地址: \thttp://127.0.0.1:{}{}/doc.html", env.getProperty("server.port"), env.getProperty("server.servlet.context-path"));
    }
}
