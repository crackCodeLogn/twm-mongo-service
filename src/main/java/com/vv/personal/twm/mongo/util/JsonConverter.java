package com.vv.personal.twm.mongo.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.google.protobuf.util.JsonFormat;
import com.vv.personal.twm.artifactory.generated.bank.BankProto;

import java.io.IOException;

/**
 * @author Vivek
 * @since 16/11/20
 */
public class JsonConverter {

    private static final Gson GSON = new Gson();

    private static final GsonBuilder BANK_GSON_BUILDER = new GsonBuilder();
    private static final Gson BANK_GSON = BANK_GSON_BUILDER.registerTypeAdapter(BankProto.Bank.class, new TypeAdapter<BankProto.Bank>() {
        @Override
        public void write(JsonWriter jsonWriter, BankProto.Bank bank) throws IOException {
            jsonWriter.jsonValue(JsonFormat.printer().print(bank));
        }

        @Override
        public BankProto.Bank read(JsonReader jsonReader) throws IOException {
            return null; //empty on purpose
        }
    }).create();

    public static <T> String convertToJson(T object) {
        return GSON.toJson(object);
    }

    public static String convertToBankJson(BankProto.Bank bankObject) {
        return BANK_GSON.toJson(bankObject);
    }
}
