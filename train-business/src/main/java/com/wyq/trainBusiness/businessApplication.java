package com.wyq.trainBusiness;

import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
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

import java.util.ArrayList;
import java.util.List;

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
        initFlowRules();
    }
    private static void initFlowRules(){
        List<FlowRule> rules = new ArrayList<>();
        FlowRule rule = new FlowRule();
        rule.setResource("doConfirm");
        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        // Set limit QPS to 20.
        rule.setCount(100);
        rules.add(rule);
        FlowRuleManager.loadRules(rules);
    }

}
