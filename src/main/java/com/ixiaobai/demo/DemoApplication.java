package com.ixiaobai.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.env.ConfigurableEnvironment;

import javax.annotation.Resource;

@SpringBootApplication
@ImportResource(locations = { "classpath:applicationContext.xml" })
@Slf4j
public class DemoApplication implements ApplicationContextAware {
    @Resource
    private DemoCtx ctx;
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(DemoApplication.class);
        app.run(args);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        System.out.println("ctx.ctxPath value is "+ ctx.getCtxPath());
    }
}
