package com.epam.training.lab2

import groovy.json.JsonSlurper
import groovy.transform.ToString

/**
 * Created by Uladzislau_Prykhodzk on 9/12/14.
 */
@ToString
class Money {
    double amount;
    String currency;


    private Map<String, Double> exchangeRates = ['USD': 1.0, 'EUR' : 1.29239]

    Money plus(int add) {
        new Money(amount: amount + add, currency: currency);
    }

    Money plus(Money add) {
        def rate = new JsonSlurper().parseText( new URL( "http://rate-exchange.appspot.com/currency?from=${currency}&to=${this.currency}")).rate
        new Money(amount: exchangeRates[currency] * amount + exchangeRates[add.currency] * add.amount, currency: 'USD');
    }

        Double getRate() {


    }
}
