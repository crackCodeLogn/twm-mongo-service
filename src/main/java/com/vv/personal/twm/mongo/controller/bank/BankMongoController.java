package com.vv.personal.twm.mongo.controller.bank;

import com.mongodb.client.MongoDatabase;
import com.vv.personal.twm.artifactory.bank.Bank;
import com.vv.personal.twm.mongo.controller.AbstractController;
import com.vv.personal.twm.mongo.interaction.BankCrud;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.*;

import javax.inject.Singleton;

import static com.vv.personal.twm.mongo.constants.Constants.DB_BANK;

/**
 * @author Vivek
 * @since 16/11/20
 */
@RestController("BankMongoController")
@RequestMapping("/mongo/bank")
@Configuration
public class BankMongoController extends AbstractController {

    private static final Logger LOGGER = LoggerFactory.getLogger(BankMongoController.class);

    @Bean
    public MongoDatabase BankMongoDatabase() {
        return getDbSpecificMongo(DB_BANK);
    }

    @Bean
    @Singleton
    public BankCrud BankCrud() {
        LOGGER.info("Creating Bank Crud now");
        return new BankCrud(BankMongoDatabase());
    }

    @PostMapping("/addBank")
    public String addBank(@RequestBody Bank newBank) {
        LOGGER.info("Received new bank to be added to Mongo: {}", newBank);
        try {
            if (BankCrud().add(newBank)) return "OK";
        } catch (Exception e) {
            LOGGER.error("Failed to add {} to mongo! ", newBank.getName(), e);
        }
        return "FAILED";
    }

    @PostMapping("/deleteBank")
    public String deleteBank(@RequestBody String ifscToDelete) {
        LOGGER.info("Received IFSC to delete: {}", ifscToDelete);
        try {
            BankCrud().deleteMany(ifscToDelete);
            return "OK";
        } catch (Exception e) {
            LOGGER.error("Failed to delete IFSC: {} from mongo! ", ifscToDelete, e);
        }
        return "FAILED";
    }

    //read
    @GetMapping("/getBanks")
    public String getBanks(@RequestParam("field") String field,
                           @RequestParam(value = "value", required = false) String value) {
        LOGGER.info("Received {} to list for field {}", value, field);
        try {
            switch (field) {
                case "NAME":
                    return BankCrud().listAllByName(value);
                case "TYPE":
                    return BankCrud().listAllByType(value);
                case "IFSC":
                    return BankCrud().listByIfsc(value);
                default:
                    return BankCrud().listAll();
            }
        } catch (Exception e) {
            LOGGER.error("Failed to list {}: {} from mongo! ", field, value, e);
        }
        return "FAILED";
    }

}