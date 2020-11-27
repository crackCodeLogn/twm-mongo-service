package com.vv.personal.twm.mongo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.protobuf.ProtobufHttpMessageConverter;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author Vivek
 * @since 16/11/20
 */
@EnableSwagger2
@EnableFeignClients
@EnableEurekaClient
@SpringBootApplication
public class MongoService {
    public static void main(String[] args) {
        SpringApplication.run(MongoService.class, args);
    }

    @Bean
    ProtobufHttpMessageConverter protobufHttpMessageConverter() {
        return new ProtobufHttpMessageConverter();
    }
}
