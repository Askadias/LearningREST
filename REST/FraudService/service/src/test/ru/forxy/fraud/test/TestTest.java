package ru.forxy.fraud.test;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import ru.forxy.common.utils.NetworkUtils;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tiger on 12.02.14.
 */
public class TestTest extends BaseFraudServiceTest {

    private static final String YAHOO_ALL_CURRENCIES_XML = "http://finance.yahoo.com/webservice/v1/symbols/allcurrencies/quote?format=json";
    private static final DateFormat UTC_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");

    @Test
    public void testJson() throws IOException, ParseException {
        String jsonString = NetworkUtils.getURLResource(YAHOO_ALL_CURRENCIES_XML);
        JSONObject jsonObject = new JSONObject(jsonString);
        JSONObject listObject = jsonObject.getJSONObject("list");
        JSONArray resources = listObject.getJSONArray("resources");
        Map<String, Double> currencies = new HashMap<String, Double>(resources.length());
        for (int i = 0; i < resources.length(); i++) {
            JSONObject resource = resources.getJSONObject(i).getJSONObject("resource");
            JSONObject fields = resource.getJSONObject("fields");
            String name = fields.getString("name");
            String pair[] = name.split("/");
            if (pair.length == 2 && pair[0].equals("USD") && !pair[1].equals("USD")) {
                Double price = fields.getDouble("price");
                currencies.put(pair[1], price);
            }
            String utctime = fields.getString("utctime");
            Date updateDate = UTC_DATE_FORMAT.parse(utctime);
        }
        Assert.assertTrue(currencies.size() > 0);
    }
}
