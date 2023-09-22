package com.wyq.trainBatch;

import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAspectJAutoProxy
@MapperScan("com.wyq.trainBatch.mapper")
@ComponentScan(basePackages = {"com.wyq",
        "com.wyq.trainCommon"})
public class batchApplication {
    private static final Logger LOG = LoggerFactory.getLogger(batchApplication.class);

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(batchApplication.class);
        Environment env = app.run(args).getEnvironment();
        LOG.info("启动成功！！");
        LOG.info("文档地址: \thttp://127.0.0.1:{}{}/doc.html", env.getProperty("server.port"), env.getProperty("server.servlet.context-path"));
    }
}
