package com.vv.personal.twm.mongo.controller.bank;

import com.vv.personal.twm.artifactory.generated.deposit.FixedDepositProto;
import com.vv.personal.twm.mongo.controller.AbstractController;
import com.vv.personal.twm.mongo.interaction.FdCrud;
import com.vv.personal.twm.mongo.util.JsonConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static com.vv.personal.twm.mongo.constants.Constants.EMPTY_STR;
import static com.vv.personal.twm.mongo.constants.Constants.SEARCH_ALL;

/**
 * @author Vivek
 * @since 17/11/20
 */
@RestController("FixedDepositMongoController")
@RequestMapping("/mongo/fd")
@Configuration
public class FixedDepositMongoController extends AbstractController {
    private static final Logger LOGGER = LoggerFactory.getLogger(FixedDepositMongoController.class);

    @Autowired
    private BankMongoController bankMongoController;

    @Bean
    //@Singleton //Marking as @Configuration resolved the status of @Bean to singleton - imp learning
    public FdCrud FdCrud() {
        LOGGER.info("Creating FD Crud now");
        return new FdCrud(BankMongoDatabase());
    }

    @PostMapping("/addFd")
    public String addFd(@RequestBody FixedDepositProto.FixedDeposit newFd) {
        LOGGER.info("Received new FD to be added to Mongo: {}", newFd);
        if (bankMongoController.getBanks(SEARCH_ALL, EMPTY_STR).getBanksList().stream().anyMatch(bank -> bank.getIFSC().equals(newFd.getBankIFSC()))) {
            try {
                if (FdCrud().add(newFd)) return "OK";
            } catch (Exception e) {
                LOGGER.error("Failed to add {} to mongo! ", newFd.getFdNumber(), e);
            }
        } else {
            LOGGER.warn("IFSC code mentioned in new FD '{}' not found in existing bank record! Not adding to DB", newFd.getBankIFSC());
        }
        return "FAILED";
    }

    @PostMapping("/deleteFd")
    public String deleteFd(@RequestBody String fdKey) {
        LOGGER.info("Received FD-Key to delete: {}", fdKey);
        try {
            FdCrud().deleteOne(fdKey);
            return "OK";
        } catch (Exception e) {
            LOGGER.error("Failed to delete FD: {} from mongo! ", fdKey, e);
        }
        return "FAILED";
    }

    @PostMapping("/update-by-replacing")
    public String updateRecordByReplacing(@RequestBody FixedDepositProto.FixedDeposit fixedDeposit) {
        LOGGER.info("Going to replace FD with key: {} with updated information", fixedDeposit.getFdNumber());
        if (deleteFd(fixedDeposit.getFdNumber()).equals("OK")) {
            if (addFd(fixedDeposit).equals("OK")) {
                LOGGER.info("Added updated FD details in mongo for key: {}", fixedDeposit.getFdNumber());
                return "OK";
            } else {
                LOGGER.error("Failed to update FD with key: {}", fixedDeposit.getFdNumber());
            }
        }
        return "FAILED";
    }

    //read
    @GetMapping("/getFds")
    public FixedDepositProto.FixedDepositList getFds(@RequestParam("field") String field,
                                                     @RequestParam(value = "value", required = false) String value) {
        LOGGER.info("Received '{}' to list Fixed Deposits for field '{}'", value, field);
        FixedDepositProto.FixedDepositList.Builder fixedDeposits = FixedDepositProto.FixedDepositList.newBuilder();
        List<String> result = new ArrayList<>();
        try {
            switch (field) {
                case "BANK-SHORT":
                    result = FdCrud().listAllByName(value);
                    break;
                case "KEY": //TODO -- replace with constants, which are to be placed in the artifactory
                    result = FdCrud().listByKey(value);
                    break;
                default:
                    result = FdCrud().listAll();
            }
        } catch (Exception e) {
            LOGGER.error("Failed to list {}: {} from mongo! ", field, value, e);
        }
        result.forEach(document -> {
            try {
                fixedDeposits.addFixedDeposit(JsonConverter.convertToFixedDepositProto(document));
            } catch (Exception e) {
                LOGGER.error("Failed to convert '{}' to proto. ", document, e);
            }
        });
        return fixedDeposits.build();
    }
}
