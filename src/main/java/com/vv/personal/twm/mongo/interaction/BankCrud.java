package com.vv.personal.twm.mongo.interaction;

import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.vv.personal.twm.artifactory.generated.bank.BankProto;
import com.vv.personal.twm.mongo.util.JsonConverter;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.regex;
import static com.vv.personal.twm.mongo.constants.Constants.COLLECTION_BANKS;

/**
 * @author Vivek
 * @since 16/11/20
 */
public class BankCrud extends Crud {
    private static final Logger LOGGER = LoggerFactory.getLogger(BankCrud.class);

    public BankCrud(MongoDatabase mongoDatabase) {
        super(mongoDatabase, COLLECTION_BANKS);
        triggerCollectionRead();
    }

    //create
    public boolean add(BankProto.Bank newBank) {
        LOGGER.info("new bank doc: {}", newBank.toString());
        if (checkIfBankExistsOnIfsc(newBank.getIFSC())) return false;

        String json = JsonConverter.convertToBankJson(newBank);
        Document bsonConverted = Document.parse(json);
        mongoCollection.insertOne(bsonConverted);
        LOGGER.info("Addition op completed");
        return true;
    }

    //delete-many
    public void deleteMany(String ifscToDelete) {
        Bson filter = getIfscFilter(ifscToDelete);
        DeleteResult deleteResult = mongoCollection.deleteMany(filter);
        LOGGER.info("Deletion op -> {}: {}", deleteResult.wasAcknowledged(), deleteResult.getDeletedCount());
    }

    //delete-one
    public void deleteOne(String ifscToDelete) {
        Bson filter = getIfscFilter(ifscToDelete);
        DeleteResult deleteResult = mongoCollection.deleteOne(filter);
        LOGGER.info("Deletion op -> {}: {}", deleteResult.wasAcknowledged(), deleteResult.getDeletedCount());
    }

    //list-all
    public String listAll() {
        return queryAll();
    }

    //list-name
    public String listAllByName(String bankName) {
        return queryOn(getRegexFilterOnColumn("name", bankName));
    }

    //list-ifsc
    public String listByIfsc(String ifsc) {
        return queryOn(getIfscFilter(ifsc));
    }

    //list-type
    public String listAllByType(String bankType) {
        return queryOn(getTypeFilter(bankType));
    }

    private Bson getRegexFilterOnColumn(String column, String data) {
        return regex(column, data, "i"); //i -> ignore case
    }

    private Bson getIfscFilter(String ifsc) {
        return getRegexFilterOnColumn("IFSC", ifsc);
    }

    private Bson getTypeFilter(String type) {
        return eq("bankType", type);
    }

    private boolean checkIfBankExistsOnIfsc(String ifsc) {
        triggerCollectionRead();
        Bson filter = getIfscFilter(ifsc);
        return mongoCollection.find(filter).iterator().hasNext();
    }

}
