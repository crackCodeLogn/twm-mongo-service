package com.vv.personal.twm.mongo.controller.bank;

import com.vv.personal.twm.artifactory.generated.bank.BankProto;
import com.vv.personal.twm.mongo.controller.AbstractController;
import com.vv.personal.twm.mongo.interaction.BankCrud;
import com.vv.personal.twm.mongo.util.JsonConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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
    //@Singleton //Marking as @Configuration resolved the status of @Bean to singleton - imp learning
    public BankCrud BankCrud() {
        LOGGER.info("Creating Bank Crud now");
        return new BankCrud(BankMongoDatabase());
    }

    @PostMapping("/addBank")
    public String addBank(@RequestBody BankProto.Bank newBank) {
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
    public BankProto.BankList getBanks(@RequestParam("field") String field,
                                       @RequestParam(value = "value", required = false) String value) {
        LOGGER.info("Received '{}' to list Banks for field '{}'", value, field);
        BankProto.BankList.Builder banks = BankProto.BankList.newBuilder();
        List<String> result = new ArrayList<>();
        try {
            switch (field) {
                case "NAME":
                    result = BankCrud().listAllByName(value);
                    break;
                case "TYPE":
                    result = BankCrud().listAllByType(value);
                    break;
                case "IFSC":
                    result = BankCrud().listByIfsc(value);
                    break;
                default:
                    result = BankCrud().listAll();
            }
        } catch (Exception e) {
            LOGGER.error("Failed to list {}: {} from mongo! ", field, value, e);
        }
        result.forEach(document -> {
            try {
                banks.addBanks(JsonConverter.convertToBankProto(document));
            } catch (Exception e) {
                LOGGER.error("Failed to convert '{}' to proto. ", document, e);
            }
        });
        return banks.build();
    }

}
