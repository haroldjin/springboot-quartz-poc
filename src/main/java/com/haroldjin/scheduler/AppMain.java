package com.haroldjin.scheduler;

import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
//@ImportResource("classpath:config/spring.xml")
public class AppMain {
    public static void main(String[] args) {
        new SpringApplicationBuilder()
                .sources(AppMain.class)
                .bannerMode(Banner.Mode.CONSOLE)
                .run(args);
    }
    
}