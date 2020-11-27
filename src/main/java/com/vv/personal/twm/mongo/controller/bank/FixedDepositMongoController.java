package com.vv.personal.twm.mongo.controller.bank;

import com.vv.personal.twm.artifactory.KeyUtil;
import com.vv.personal.twm.artifactory.generated.deposit.FixedDepositProto;
import com.vv.personal.twm.mongo.controller.AbstractController;
import com.vv.personal.twm.mongo.interaction.FdCrud;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.*;

/**
 * @author Vivek
 * @since 17/11/20
 */
@RestController("FixedDepositMongoController")
@RequestMapping("/mongo/fd")
@Configuration
public class FixedDepositMongoController extends AbstractController {
    private static final Logger LOGGER = LoggerFactory.getLogger(FixedDepositMongoController.class);

    @Bean
    //@Singleton //Marking as @Configuration resolved the status of @Bean to singleton - imp learning
    public FdCrud FdCrud() {
        LOGGER.info("Creating FD Crud now");
        return new FdCrud(BankMongoDatabase());
    }

    @PostMapping("/addFD")
    public String addFd(@RequestBody FixedDepositProto.FixedDeposit newFd) {
        LOGGER.info("Received new FD to be added to Mongo: {}", newFd);
        try {
            if (FdCrud().add(newFd)) return "OK";
        } catch (Exception e) {
            LOGGER.error("Failed to add {} to mongo! ", KeyUtil.generateFdKey(newFd), e);
        }
        return "FAILED";
    }

    @PostMapping("/deleteFD")
    public String deleteFd(@RequestBody String fdKey) {
        LOGGER.info("Received FD-Key to delete: {}", fdKey);
        try {
            FdCrud().deleteOne(fdKey);
            return "OK";
        } catch (Exception e) {
            LOGGER.error("Failed to delete IFSC: {} from mongo! ", fdKey, e);
        }
        return "FAILED";
    }

    //read
    @GetMapping("/getFDs")
    public String getFds(@RequestParam("field") String field,
                         @RequestParam(value = "value", required = false) String value) {
        LOGGER.info("Received {} to list for field {}", value, field);
        try {
            String result; //list of json binded in a list
            switch (field) {
                case "NAME":
                    result = FdCrud().listAllByName(value);
                    break;
                case "KEY":
                    result = FdCrud().listByKey(value);
                    break;
                default:
                    result = FdCrud().listAll();
            }
            return result;
        } catch (Exception e) {
            LOGGER.error("Failed to list {}: {} from mongo! ", field, value, e);
        }
        return "FAILED";
    }
}
