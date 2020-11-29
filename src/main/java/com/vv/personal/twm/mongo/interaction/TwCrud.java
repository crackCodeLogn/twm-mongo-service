package com.vv.personal.twm.mongo.interaction;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.vv.personal.twm.artifactory.generated.tw.VillaProto;
import com.vv.personal.twm.mongo.util.JsonConverter;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import static com.vv.personal.twm.mongo.constants.Constants.COLLECTION_VILLAS;

/**
 * @author Vivek
 * @since 29/11/20
 */
public class TwCrud extends Crud {
    private static final Logger LOGGER = LoggerFactory.getLogger(TwCrud.class);

    public TwCrud(MongoDatabase mongoDatabase) {
        super(mongoDatabase, COLLECTION_VILLAS);
    }

    //create
    public boolean add(VillaProto.Villa newVilla) {
        LOGGER.info("new Villa doc: {}", newVilla.toString());
        if (checkIfVillaExists(newVilla.getId())) return false;

        String json = JsonConverter.convertToVillaJson(newVilla);
        Document bsonConverted = Document.parse(json);
        mongoCollection.insertOne(bsonConverted);
        LOGGER.info("Addition op completed");
        return true;
    }

    //delete-many
    public void deleteMany(String villaKey) {
        Bson filter = getVillaKeyFilter(villaKey);
        DeleteResult deleteResult = mongoCollection.deleteMany(filter);
        LOGGER.info("Deletion op -> {}: {}", deleteResult.wasAcknowledged(), deleteResult.getDeletedCount());
    }

    //delete-one
    public void deleteOne(String villaKey) {
        Bson filter = getVillaKeyFilter(villaKey);
        DeleteResult deleteResult = mongoCollection.deleteOne(filter);
        LOGGER.info("Deletion op -> {}: {}", deleteResult.wasAcknowledged(), deleteResult.getDeletedCount());
    }

    //list-all
    public List<String> listAll() {
        return queryAll();
    }

    //list-world-wise
    public List<String> listAllByWorld(String world) {
        return queryOn(getRegexFilterOnColumn("world", world));
    }

    //list-id-key
    public List<String> listByKey(String villaKey) {
        return queryOn(getVillaKeyFilter(villaKey));
    }

    private Bson getVillaKeyFilter(String key) {
        return eq("id", key);
    }

    private boolean checkIfVillaExists(String key) {
        triggerCollectionRead();
        Bson filter = getVillaKeyFilter(key);
        return mongoCollection.find(filter).iterator().hasNext();
    }
}
