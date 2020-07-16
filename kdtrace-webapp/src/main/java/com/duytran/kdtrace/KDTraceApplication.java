package com.duytran.kdtrace;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@Slf4j
@SpringBootApplication
@EnableJpaAuditing
@EnableAsync
public class KDTraceApplication{
    public static void main(String[] args) {
        SpringApplication.run(KDTraceApplication.class);
        log.info("Simple log statement with inputs {}, {} and {}", 1, 2, 3);
    }
    @PostConstruct
    public void init(){
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+7"));
    }
}
