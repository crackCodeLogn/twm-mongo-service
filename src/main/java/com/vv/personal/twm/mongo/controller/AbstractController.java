package com.vv.personal.twm.mongo.controller;

import com.vv.personal.twm.mongo.config.MongoSpringConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * @author Vivek
 * @since 16/11/20
 */
public abstract class AbstractController {

    @Autowired
    private MongoSpringConfig mongoSpringConfig;

    public MongoTemplate getDbSpecificMongo(String database) {
        return new MongoTemplate(mongoSpringConfig.mongoClient(), database);
    }

}

