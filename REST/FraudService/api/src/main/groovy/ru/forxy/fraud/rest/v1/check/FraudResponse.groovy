package ru.forxy.fraud.rest.v1.check;

class FraudResponse {
    Boolean isFraud;
    Double probability;
    Double threshold;
    Long transactionID;
}
