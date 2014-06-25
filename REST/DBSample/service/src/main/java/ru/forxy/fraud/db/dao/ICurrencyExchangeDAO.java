package ru.forxy.fraud.db.dao;

import java.io.IOException;
import java.text.ParseException;

/**
 * Data access to the currency exchange rates
 */
public interface ICurrencyExchangeDAO {

    void performScheduledCurrencyUpdate() throws IOException, ParseException;

    Double usdValue(final String currency, final Double amount);
}
