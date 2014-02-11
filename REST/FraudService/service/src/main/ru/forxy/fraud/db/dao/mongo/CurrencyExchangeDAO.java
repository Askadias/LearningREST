package ru.forxy.fraud.db.dao.mongo;

import org.springframework.data.mongodb.core.MongoTemplate;
import ru.forxy.fraud.db.dao.ICurrencyExchangeDAO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

public class CurrencyExchangeDAO implements ICurrencyExchangeDAO {

    private static final String YAHOO_ALL_CURRENCIES_XML = "http://finance.yahoo.com/webservice/v1/symbols/allcurrencies/quote";

    private MongoTemplate mongoTemplate;

    private Map<String, Double> currencyExchangeRatesToUSD = new HashMap<String, Double>();

    @Override
    public void scheduledCurrencyUpdate() {
    }

    private void updateCurrencyExchangeRates() throws IOException {
        URL currenciesXmlUrl = new URL(YAHOO_ALL_CURRENCIES_XML);
        URLConnection urlConnection = currenciesXmlUrl.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        String inputLine;
        while ((inputLine = in.readLine()) != null)
            System.out.println(inputLine);
        in.close();
    }

    @Override
    public Double usdValue(String currency, Double amount) {
        Double rate = currencyExchangeRatesToUSD.get(currency);
        return rate != null && amount != null ? amount * rate : null;
    }

    public void setMongoTemplate(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }
}
