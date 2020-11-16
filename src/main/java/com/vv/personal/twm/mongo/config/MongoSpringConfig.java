package com.vv.personal.twm.mongo.config;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.vv.personal.twm.mongo.constants.Constants.MONGO_CLIENT_BASIC_HOST_PORT;

/**
 * @author Vivek
 * @since 08/11/20
 */
@Configuration
public class MongoSpringConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(MongoSpringConfig.class);

    @Value("${spring.data.mongodb.host}")
    private String host;

    @Value("${spring.data.mongodb.port}")
    private String port;

    @Bean
    public MongoClient mongoClient() {
        LOGGER.info("Creating mongo client here...");
        return MongoClients.create(String.format(MONGO_CLIENT_BASIC_HOST_PORT, host, port));
    }

}
