package com.wyq.trainBusiness;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.env.Environment;

@SpringBootApplication
@EnableAspectJAutoProxy
@MapperScan("com.wyq.trainBusiness.mapper")
@ComponentScan(basePackages = {"com.wyq",
        "com.wyq.trainCommon"})
@EnableFeignClients("com.wyq.trainBusiness.feign")
@EnableCaching
public class businessApplication {
    private static final Logger LOG = LoggerFactory.getLogger(businessApplication.class);

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(businessApplication.class);
        Environment env = app.run(args).getEnvironment();
        LOG.info("启动成功！！");
        LOG.info("测试地址: \thttp://127.0.0.1:{}{}/hello", env.getProperty("server.port"), env.getProperty("server.servlet.context-path"));
        LOG.info("文档地址: \thttp://127.0.0.1:{}{}/doc.html", env.getProperty("server.port"), env.getProperty("server.servlet.context-path"));
    }
}
