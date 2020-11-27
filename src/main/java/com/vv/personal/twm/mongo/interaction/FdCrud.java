package com.vv.personal.twm.mongo.interaction;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.vv.personal.twm.artifactory.generated.deposit.FixedDepositProto;
import com.vv.personal.twm.mongo.util.JsonConverter;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
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
        //if (checkIfFdExists(newFd.getKey())) return false; //TODO -- get back to this

        String json = JsonConverter.convertToJson(newFd);
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
    public String listAll() {
        triggerCollectionRead();
        List<String> compilation = new LinkedList<>();
        for (Document search : mongoCollection.find()) {
            compilation.add(search.toJson());
        }
        LOGGER.info("Search query completed for 'ALL'");
        return compilation.toString();
    }

    //list-name
    public String listAllByName(String bankName) {
        triggerCollectionRead();
        List<String> compilation = new LinkedList<>();
        for (Document search : query(getNameFilter(bankName))) {
            compilation.add(search.toJson());
        }
        LOGGER.info("Search query completed for {}", bankName);
        return compilation.toString();
    }

    //list-ifsc
    public String listByKey(String fdKey) {
        triggerCollectionRead();
        List<String> compilation = new LinkedList<>();
        for (Document search : query(getFdKeyFilter(fdKey))) {
            compilation.add(search.toJson());
        }
        LOGGER.info("Search query completed for {}", fdKey);
        return compilation.toString();
    }

    private Bson getFdKeyFilter(String key) {
        return eq("key", key);
    }

    private Bson getNameFilter(String name) {
        return eq("bank", name);
    }

    private boolean checkIfFdExists(String key) {
        triggerCollectionRead();
        Bson filter = getFdKeyFilter(key);
        return mongoCollection.find(filter).iterator().hasNext();
    }

}
