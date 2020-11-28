package com.vv.personal.twm.mongo.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import com.vv.personal.twm.artifactory.generated.bank.BankProto;
import com.vv.personal.twm.artifactory.generated.deposit.FixedDepositProto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author Vivek
 * @since 16/11/20
 * <p>
 * Thinking of shifting this to artifactory method
 */
public class JsonConverter {
    private static final Logger LOGGER = LoggerFactory.getLogger(JsonConverter.class);

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

    public static String convertProtoToBankJson(BankProto.Bank bankObject) {
        return BANK_GSON.toJson(bankObject);
    }

    public static BankProto.Bank convertToBankProto(String json) {
        BankProto.Bank.Builder builder = BankProto.Bank.newBuilder();
        try {
            JsonFormat.parser().ignoringUnknownFields().merge(json, builder);
        } catch (InvalidProtocolBufferException e) {
            LOGGER.error("Failed to convert {} to FD proto. ", json, e);
        }
        return builder.build();
    }

    private static final GsonBuilder FD_GSON_BUILDER = new GsonBuilder();
    private static final Gson FD_GSON = FD_GSON_BUILDER.registerTypeAdapter(FixedDepositProto.FixedDeposit.class, new TypeAdapter<FixedDepositProto.FixedDeposit>() {

        @Override
        public void write(JsonWriter jsonWriter, FixedDepositProto.FixedDeposit fixedDeposit) throws IOException {
            jsonWriter.jsonValue(JsonFormat.printer().print(fixedDeposit));
        }

        @Override
        public FixedDepositProto.FixedDeposit read(JsonReader jsonReader) throws IOException {
            return null;
        }
    }).create();

    public static String convertToFdJson(FixedDepositProto.FixedDeposit fixedDepositObject) {
        return FD_GSON.toJson(fixedDepositObject);
    }

    public static FixedDepositProto.FixedDeposit convertToFixedDepositProto(String json) {
        FixedDepositProto.FixedDeposit.Builder builder = FixedDepositProto.FixedDeposit.newBuilder();
        try {
            JsonFormat.parser().ignoringUnknownFields().merge(json, builder);
        } catch (InvalidProtocolBufferException e) {
            LOGGER.error("Failed to convert {} to FD proto. ", json, e);
        }
        return builder.build();
    }

    public static <T> String convertToJson(T object) {
        return GSON.toJson(object);
    }

}
