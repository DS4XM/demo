package com.ixiaobai.demo;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class DemoCtx {
    @Value("${server.servlet.context-path:/}")
    private String ctxPath;
}
