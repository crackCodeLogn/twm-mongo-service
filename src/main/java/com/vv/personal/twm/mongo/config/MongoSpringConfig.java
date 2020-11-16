package com.vv.personal.twm.mongo.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import static com.vv.personal.twm.mongo.constants.Constants.MONGO_CLIENT_BASIC_HOST_PORT;

/**
 * @author Vivek
 * @since 08/11/20
 */
@Configuration
public class MongoSpringConfig {

    @Value("${spring.data.mongodb.host}")
    private String host;

    @Value("${spring.data.mongodb.port}")
    private String port;

    @Bean
    @Scope("prototype")
    public MongoClient mongoClient() {
        System.out.println("Creating mongo client here...");
        return MongoClients.create(String.format(MONGO_CLIENT_BASIC_HOST_PORT, host, port));
    }

}
