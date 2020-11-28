package com.vv.personal.twm.mongo.interaction;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.vv.personal.twm.artifactory.generated.deposit.FixedDepositProto;
import com.vv.personal.twm.mongo.util.JsonConverter;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import static com.vv.personal.twm.mongo.constants.Constants.COLLECTION_FD;

/**
 * @author Vivek
 * @since 17/11/20
 */
public class FdCrud extends Crud {
    private static final Logger LOGGER = LoggerFactory.getLogger(FdCrud.class);

    public FdCrud(MongoDatabase mongoDatabase) {
        super(mongoDatabase, COLLECTION_FD);
    }

    //create
    public boolean add(FixedDepositProto.FixedDeposit newFd) {
        LOGGER.info("new FD doc: {}", newFd.toString());
        if (checkIfFdExists(newFd.getKey())) return false;

        String json = JsonConverter.convertToFdJson(newFd);
        Document bsonConverted = Document.parse(json);
        mongoCollection.insertOne(bsonConverted);
        LOGGER.info("Addition op completed");
        return true;
    }

    //delete-many
    public void deleteMany(String fdKey) {
        Bson filter = getFdKeyFilter(fdKey);
        DeleteResult deleteResult = mongoCollection.deleteMany(filter);
        LOGGER.info("Deletion op -> {}: {}", deleteResult.wasAcknowledged(), deleteResult.getDeletedCount());
    }

    //delete-one
    public void deleteOne(String fdKey) {
        Bson filter = getFdKeyFilter(fdKey);
        DeleteResult deleteResult = mongoCollection.deleteOne(filter);
        LOGGER.info("Deletion op -> {}: {}", deleteResult.wasAcknowledged(), deleteResult.getDeletedCount());
    }

    //list-all
    public List<String> listAll() {
        return queryAll();
    }

    //list-name
    public List<String> listAllByName(String fdBankName) {
        return queryOn(getRegexFilterOnColumn("bankIFSC", fdBankName));
    }

    //list-ifsc
    public List<String> listByKey(String fdKey) {
        return queryOn(getFdKeyFilter(fdKey));
    }

    private Bson getFdKeyFilter(String key) {
        return eq("key", key);
    }

    private boolean checkIfFdExists(String key) {
        triggerCollectionRead();
        Bson filter = getFdKeyFilter(key);
        return mongoCollection.find(filter).iterator().hasNext();
    }

}
