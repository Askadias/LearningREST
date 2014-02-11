package ru.forxy.fraud.db.dao;

/**
 * Data access to the currency exchange rates
 */
public interface ICurrencyExchangeDAO {

    void scheduledCurrencyUpdate();

    Double usdValue(final String currency, final Double amount);
}
