package com.vv.personal.twm.mongo.controller;

import com.mongodb.client.MongoDatabase;
import com.vv.personal.twm.mongo.config.MongoSpringConfig;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Vivek
 * @since 16/11/20
 */
public abstract class AbstractController {

    @Autowired
    private MongoSpringConfig mongoSpringConfig;

    public MongoDatabase getDbSpecificMongo(String database) {
        return mongoSpringConfig.mongoClient().getDatabase(database);
    }


}

