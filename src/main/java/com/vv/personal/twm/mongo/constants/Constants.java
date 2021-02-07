package com.vv.personal.twm.mongo.constants;

/**
 * @author Vivek
 * @since 10/11/20
 */
public class Constants {
    public static final String MONGO_CLIENT_BASIC_HOST_PORT = "mongodb://%s:%s";

    public static final String DB_BANK = "twm-bank";
    public static final String COLLECTION_BANKS = "banks";
    public static final String COLLECTION_FD = "fixedDeposits";

    public static final String DB_TW = "twm-tw";
    public static final String COLLECTION_VILLAS = "villas";

    public static final String EMPTY_STR = "";
    public static final String SEARCH_ALL = "ALL";

    //URL - FORMATTERS
    public static final String HEROKU_SWAGGER_UI_URL = "https://%s/swagger-ui/index.html";
    public static final String SWAGGER_UI_URL = "http://%s:%s/swagger-ui/index.html";
    public static final String HEROKU_HOST_URL = "https://%s";
    public static final String HOST_URL = "http://%s:%s";

    public static final String LOCALHOST = "localhost";
    public static final String LOCAL_SPRING_HOST = "local.server.host";
    public static final String LOCAL_SPRING_PORT = "local.server.port";
    public static final String SPRING_APPLICATION_HEROKU = "spring.application.heroku";

}
