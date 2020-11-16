package com.vv.personal.twm.mongo.controller;

import com.netflix.discovery.EurekaClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Vivek
 * @since 16/11/20
 */
@RestController("controllerMain")
@RequestMapping("/mongo")
public class Controller {
    private static final Logger LOGGER = LoggerFactory.getLogger(Controller.class);

    @Autowired
    private EurekaClient eurekaClient;

    @Value("${spring.application.name}")
    private String appName;

    @RequestMapping("/greeting")
    public String greeting() {
        LOGGER.info("Invoked mongo's /mongo/greeting endpoint....");
        return String.format(
                "Hello from '%s'!", eurekaClient.getApplication(appName).getName());
    }

}