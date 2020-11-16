package com.vv.personal.twm.mongo.interaction;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.vv.personal.twm.artifactory.bank.Bank;
import com.vv.personal.twm.mongo.util.JsonConverter;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import static com.vv.personal.twm.mongo.constants.Constants.COLLECTION_BANKS;

/**
 * @author Vivek
 * @since 16/11/20
 */
public class BankCrud extends Crud {
    private static final Logger LOGGER = LoggerFactory.getLogger(BankCrud.class);
    private MongoCollection<Document> bankCollection;

    public BankCrud(MongoDatabase mongoDatabase) {
        super(mongoDatabase);

        triggerBankCollectionRead();
    }

    private void triggerBankCollectionRead() {
        bankCollection = getMongoDatabase().getCollection(COLLECTION_BANKS);
        LOGGER.info("Number of documents in collection '{}': {}", COLLECTION_BANKS, bankCollection.countDocuments());
    }

    //create
    public boolean add(Bank newBank) {
        LOGGER.info("new bank doc: {}", newBank.toString());
        if (checkIfBankExistsOnIfsc(newBank.getIFSC())) return false;

        String json = JsonConverter.convertToJson(newBank);
        Document bsonConverted = Document.parse(json);
        bankCollection.insertOne(bsonConverted);
        LOGGER.info("Addition op completed");
        return true;
    }

    //delete-many
    public void deleteMany(String ifscToDelete) {
        Bson filter = getIfscFilter(ifscToDelete);
        DeleteResult deleteResult = bankCollection.deleteMany(filter);
        LOGGER.info("Deletion op -> {}: {}", deleteResult.wasAcknowledged(), deleteResult.getDeletedCount());
    }

    //delete-one
    public void deleteOne(String ifscToDelete) {
        Bson filter = getIfscFilter(ifscToDelete);
        DeleteResult deleteResult = bankCollection.deleteOne(filter);
        LOGGER.info("Deletion op -> {}: {}", deleteResult.wasAcknowledged(), deleteResult.getDeletedCount());
    }

    private FindIterable<Document> search(Bson filter) {
        return bankCollection.find(filter);
    }

    //list-all
    public String listAll() {
        triggerBankCollectionRead();
        List<String> compilation = new LinkedList<>();
        for (Document search : bankCollection.find()) {
            compilation.add(search.toJson());
        }
        LOGGER.info("Search query completed for 'ALL'");
        return compilation.toString();
    }

    //list-name
    public String listAllByName(String bankName) {
        triggerBankCollectionRead();
        List<String> compilation = new LinkedList<>();
        for (Document search : search(getNameFilter(bankName))) {
            compilation.add(search.toJson());
        }
        LOGGER.info("Search query completed for {}", bankName);
        return compilation.toString();
    }

    //list-ifsc
    public String listByIfsc(String ifsc) {
        triggerBankCollectionRead();
        List<String> compilation = new LinkedList<>();
        for (Document search : search(getIfscFilter(ifsc))) {
            compilation.add(search.toJson());
        }
        LOGGER.info("Search query completed for {}", ifsc);
        return compilation.toString();
    }

    //list-type
    public String listAllByType(String bankType) {
        triggerBankCollectionRead();
        List<String> compilation = new LinkedList<>();
        for (Document search : search(getTypeFilter(bankType))) {
            compilation.add(search.toJson());
        }
        LOGGER.info("Search query completed for {}", bankType);
        return compilation.toString();
    }

    private Bson getIfscFilter(String ifsc) {
        return eq("IFSC", ifsc);
    }

    private Bson getNameFilter(String name) {
        return eq("name", name);
    }

    private Bson getTypeFilter(String type) {
        return eq("type", type);
    }

    private boolean checkIfBankExistsOnIfsc(String ifsc) {
        triggerBankCollectionRead();
        Bson filter = getIfscFilter(ifsc);
        return bankCollection.find(filter).iterator().hasNext();
    }


}
