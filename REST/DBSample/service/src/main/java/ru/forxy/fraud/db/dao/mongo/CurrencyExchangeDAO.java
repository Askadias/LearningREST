package ru.forxy.fraud.db.dao.mongo;

import org.apache.commons.lang.time.DateUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import ru.forxy.common.utils.NetworkUtils;
import ru.forxy.fraud.db.dao.ICurrencyExchangeDAO;
import ru.forxy.fraud.db.dto.Currency;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CurrencyExchangeDAO implements ICurrencyExchangeDAO, InitializingBean {

    private static final String YAHOO_ALL_CURRENCIES_XML = "http://finance.yahoo.com/webservice/v1/symbols/allcurrencies/quote?format=json";
    private static final DateFormat UTC_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
    private Date lastUpdateDate = null;

    private MongoTemplate mongoTemplate;

    private Map<String, Currency> currencyExchangeRatesToUSD = new HashMap<String, Currency>();

    @Override
    public void performScheduledCurrencyUpdate() throws IOException, ParseException {
        if (new Date().getTime() - lastUpdateDate.getTime() > DateUtils.MILLIS_PER_DAY) {
            updateCurrencyExchangeRates();
        }
    }

    private void updateCurrencyExchangeRates() throws IOException, ParseException {
        String jsonString = NetworkUtils.getURLResource(YAHOO_ALL_CURRENCIES_XML);
        JSONObject jsonObject = new JSONObject(jsonString);
        JSONObject listObject = jsonObject.getJSONObject("list");
        JSONArray resources = listObject.getJSONArray("resources");
        Map<String, Currency> currencies = new HashMap<String, Currency>(resources.length());
        Date maxUpdateDate = new Date(0);
        for (int i = 0; i < resources.length(); i++) {
            JSONObject resource = resources.getJSONObject(i).getJSONObject("resource");
            JSONObject fields = resource.getJSONObject("fields");
            String name = fields.getString("name");
            String pair[] = name.split("/");
            String utcTime = fields.getString("utctime");
            Date updateDate = UTC_DATE_FORMAT.parse(utcTime);
            if (pair.length == 2 && pair[0].equals("USD") && !pair[1].equals("USD")) {
                Double price = fields.getDouble("price");
                Currency currency = new Currency(pair[1], price, updateDate);
                currencies.put(pair[1], currency);
                mongoTemplate.save(currency);
            }
            if (maxUpdateDate.getTime() < updateDate.getTime()) {
                maxUpdateDate = updateDate;
            }
        }
        lastUpdateDate = maxUpdateDate;
        if (currencies.size() > 0) {
            currencyExchangeRatesToUSD = currencies;
        }
    }

    @Override
    public Double usdValue(String currencySymbol, Double amount) {
        Currency currency = currencyExchangeRatesToUSD.get(currencySymbol);
        return currency != null && amount != null ? amount * currency.getUsdRate() : null;
    }

    public void setMongoTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        List<Currency> currencies = mongoTemplate.find(Query.query(new Criteria()), Currency.class);
        if (currencies != null && currencies.size() > 0) {
            Date maxUpdateDate = new Date(0);
            for (Currency currency : currencies) {
                currencyExchangeRatesToUSD.put(currency.getSymbol(), currency);
                if (maxUpdateDate.getTime() < currency.getUpdateDate().getTime()) {
                    maxUpdateDate = currency.getUpdateDate();
                }
            }
            lastUpdateDate = maxUpdateDate;
        } else {
            updateCurrencyExchangeRates();
        }
    }
}
