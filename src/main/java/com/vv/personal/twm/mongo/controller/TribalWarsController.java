package com.vv.personal.twm.mongo.controller;

import com.vv.personal.twm.artifactory.generated.tw.VillaProto;
import com.vv.personal.twm.mongo.interaction.TwCrud;
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
 * @since 29/11/20
 */
@RestController("TribalWarsMongoController")
@RequestMapping("/mongo/tw")
@Configuration
public class TribalWarsController extends AbstractController {
    private static final Logger LOGGER = LoggerFactory.getLogger(TribalWarsController.class);

    @Bean
    //@Singleton //Marking as @Configuration resolved the status of @Bean to singleton - imp learning
    public TwCrud TwCrud() {
        LOGGER.info("Creating TW Crud now");
        return new TwCrud(TWMongoDatabase());
    }

    @PostMapping("/addVilla")
    public String addVilla(@RequestBody VillaProto.Villa newVilla) {
        LOGGER.info("Received new Villa to be added to Mongo: {}", newVilla);
        try {
            if (TwCrud().add(newVilla)) return "OK";
        } catch (Exception e) {
            LOGGER.error("Failed to add {} to mongo! ", newVilla.getId(), e);
        }
        return "FAILED";
    }

    @PostMapping("/deleteVilla")
    public String deleteVilla(@RequestBody String villaKey) {
        LOGGER.info("Received TW-Key to delete: {}", villaKey);
        try {
            TwCrud().deleteOne(villaKey);
            return "OK";
        } catch (Exception e) {
            LOGGER.error("Failed to delete Villa: {} from mongo! ", villaKey, e);
        }
        return "FAILED";
    }

    @GetMapping("/read/all")
    public VillaProto.VillaList readAllVillasFromMongo(@PathVariable String world) {
        LOGGER.info("Received request to read all villas for world {}", world);
        VillaProto.VillaList.Builder villas = VillaProto.VillaList.newBuilder();
        List<String> result = new ArrayList<>();
        try {
            result = TwCrud().listAllByWorld(world);
            LOGGER.info("Read complete for world {}", world);
        } catch (Exception e) {
            LOGGER.error("Failed to list {} from mongo! ", world, e);
        }
        result.forEach(document -> {
            try {
                villas.addVillas(JsonConverter.convertToVillaProto(document));
            } catch (Exception e) {
                LOGGER.error("Failed to convert '{}' to proto. ", document, e);
            }
        });
        return villas.build();
    }

}