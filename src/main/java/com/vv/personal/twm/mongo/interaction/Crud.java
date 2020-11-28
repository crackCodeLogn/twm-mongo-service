package com.vv.personal.twm.mongo.interaction;

import com.mongodb.Block;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;

import static com.mongodb.client.model.Filters.regex;

/**
 * @author Vivek
 * @since 16/11/20
 */
public abstract class Crud {
    private static final Logger LOGGER = LoggerFactory.getLogger(Crud.class);
    private final MongoDatabase MONGO_DB;
    private final String COLLECTION;
    protected MongoCollection<Document> mongoCollection;

    public Crud(MongoDatabase mongoDatabase, String collection) {
        this.MONGO_DB = mongoDatabase;
        this.COLLECTION = collection;
    }

    protected void triggerCollectionRead() {
        mongoCollection = MONGO_DB.getCollection(COLLECTION);
        LOGGER.info("Number of documents in collection '{}': {}", COLLECTION, mongoCollection.countDocuments());
    }

    protected String queryOn(Bson filter) {
        triggerCollectionRead();
        List<String> compilation = new LinkedList<>();
        query(filter).forEach((Block<? super Document>) document -> compilation.add(document.toJson()));
        LOGGER.info("Search query completed for '{}', with {} results", filter, compilation.size());
        return compilation.toString();
    }

    protected String queryAll() {
        triggerCollectionRead();
        List<String> compilation = new LinkedList<>();
        mongoCollection.find().forEach((Block<? super Document>) document -> compilation.add(document.toJson()));
        LOGGER.info("Search query completed for '{}', with {} results", "ALL", compilation.size());
        return compilation.toString();
    }

    protected FindIterable<Document> query(Bson filter) {
        return mongoCollection.find(filter);
    }

    protected Bson getRegexFilterOnColumn(String column, String data) {
        return regex(column, data, "i"); //i -> ignore case
    }
}
