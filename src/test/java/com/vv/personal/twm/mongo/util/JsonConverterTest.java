package com.vv.personal.twm.mongo.util;

import com.vv.personal.twm.artifactory.bank.Bank;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.LinkedList;
import java.util.List;

import static com.vv.personal.twm.artifactory.bank.BankType.PRIVATE;
import static com.vv.personal.twm.mongo.util.JsonConverter.convertToJson;
import static org.junit.Assert.assertEquals;

/**
 * @author Vivek
 * @since 17/11/20
 */
@RunWith(JUnit4.class)
public class JsonConverterTest {

    @Test
    public void testConvertToJson() {
        Bank bank1 = new Bank().setName("TR1").setType(PRIVATE).setIFSC("TRN1234").setContactNumber(1213131L);
        Bank bank2 = new Bank().setName("TR2").setType(PRIVATE).setIFSC("TRN12344").setContactNumber(12131231L);
        List<String> list = new LinkedList<>();
        list.add(bank1.toString());
        list.add(bank2.toString());

        String json = convertToJson(list);
        assertEquals("[\"{\\\"name\\\":\\\"TR1\\\",\\\"type\\\":\\\"PRIVATE\\\",\\\"IFSC\\\":\\\"TRN1234\\\",\\\"contactNumber\\\":1213131}\",\"{\\\"name\\\":\\\"TR2\\\",\\\"type\\\":\\\"PRIVATE\\\",\\\"IFSC\\\":\\\"TRN12344\\\",\\\"contactNumber\\\":12131231}\"]", json);
    }
}