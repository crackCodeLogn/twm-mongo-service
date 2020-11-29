package com.vv.personal.twm.mongo.controller;

import com.mongodb.client.MongoDatabase;
import com.vv.personal.twm.mongo.config.MongoSpringConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.vv.personal.twm.mongo.constants.Constants.DB_BANK;
import static com.vv.personal.twm.mongo.constants.Constants.DB_TW;

/**
 * @author Vivek
 * @since 16/11/20
 */
@Configuration
public abstract class AbstractController {

    @Autowired
    private MongoSpringConfig mongoSpringConfig;

    public MongoDatabase getDbSpecificMongo(String database) {
        return mongoSpringConfig.mongoClient().getDatabase(database);
    }

    @Bean
    public MongoDatabase BankMongoDatabase() {
        return getDbSpecificMongo(DB_BANK);
    }

    @Bean
    public MongoDatabase TWMongoDatabase() {
        return getDbSpecificMongo(DB_TW);
    }

}

