package com.vv.personal.twm.mongo.util;

import com.vv.personal.twm.artifactory.FixedDepositKeyUtil;
import com.vv.personal.twm.artifactory.generated.bank.BankProto;
import com.vv.personal.twm.artifactory.generated.deposit.FixedDepositProto;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.LinkedList;
import java.util.List;

import static com.vv.personal.twm.artifactory.generated.bank.BankProto.BankType.PRIVATE;
import static com.vv.personal.twm.mongo.util.JsonConverter.*;
import static org.junit.Assert.assertEquals;

/**
 * @author Vivek
 * @since 17/11/20
 */
@RunWith(JUnit4.class)
public class JsonConverterTest {

    @Test
    @Ignore
    public void testConvertToJson() {
        BankProto.Bank bank1 = BankProto.Bank.newBuilder().setName("TR1").setBankType(PRIVATE).setIFSC("TRN1234").setContactNumber("1213131").build();
        BankProto.Bank bank2 = BankProto.Bank.newBuilder().setName("TR2").setBankType(PRIVATE).setIFSC("TRN12344").setContactNumber("12131231").build();
        List<String> list = new LinkedList<>();
        list.add(bank1.toString());
        list.add(bank2.toString());

        System.out.println(list.toString());
        String json = convertToJson(list);
        System.out.println(json);
        assertEquals("[\"{\\\"name\\\":\\\"TR1\\\",\\\"type\\\":\\\"PRIVATE\\\",\\\"IFSC\\\":\\\"TRN1234\\\",\\\"contactNumber\\\":1213131}\",\"{\\\"name\\\":\\\"TR2\\\",\\\"type\\\":\\\"PRIVATE\\\",\\\"IFSC\\\":\\\"TRN12344\\\",\\\"contactNumber\\\":12131231}\"]", json);
    }

    @Test
    public void testConvertToBankJson() {
        BankProto.Bank bank1 = BankProto.Bank.newBuilder().setName("TR1").setBankType(PRIVATE).setIFSC("TRN1234").setContactNumber("1213131").build();
        String json = convertToBankJson(bank1);
        assertEquals("{\n" +
                "  \"name\": \"TR1\",\n" +
                "  \"bankType\": \"PRIVATE\",\n" +
                "  \"IFSC\": \"TRN1234\",\n" +
                "  \"contactNumber\": \"1213131\"\n" +
                "}", json);
    }

    @Test
    public void testConvertToFdJson() {
        FixedDepositProto.FixedDeposit.Builder fdB = FixedDepositProto.FixedDeposit.newBuilder()
                .setUser("V2").setBankIFSC("TRN1234").setDepositAmount(9999.99).setRateOfInterest(9.9).setStartDate("20201128")
                .setMonths(25).setDays(1).setInterestType(FixedDepositProto.InterestType.ON_MATURITY).setNominee("--")
                .setInsertionTime(Long.MAX_VALUE);
        FixedDepositProto.FixedDeposit fd = fdB.setKey(FixedDepositKeyUtil.generateFdKey(fdB)).build();
        String json = convertToFdJson(fd);
        assertEquals("{\n" +
                "  \"user\": \"V2\",\n" +
                "  \"bankIFSC\": \"TRN1234\",\n" +
                "  \"depositAmount\": 9999.99,\n" +
                "  \"rateOfInterest\": 9.9,\n" +
                "  \"startDate\": \"20201128\",\n" +
                "  \"months\": 25,\n" +
                "  \"days\": 1,\n" +
                "  \"nominee\": \"--\",\n" +
                "  \"insertionTime\": \"9223372036854775807\",\n" +
                "  \"key\": \"V2-TRN1234-9999.99-20201128-9223372036854775807\"\n" +
                "}", json);
    }
}