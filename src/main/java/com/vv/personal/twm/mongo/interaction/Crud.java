package com.vv.personal.twm.mongo.interaction;

import com.mongodb.client.MongoDatabase;

/**
 * @author Vivek
 * @since 16/11/20
 */
public abstract class Crud {

    private final MongoDatabase mongoDatabase;

    public Crud(MongoDatabase mongoDatabase) {
        this.mongoDatabase = mongoDatabase;
    }

    public MongoDatabase getMongoDatabase() {
        return mongoDatabase;
    }
}
