package ru.forxy.fraud.rest.pojo.rs;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "fraudResponse")
public class FraudResponse {

    private Boolean isFraud;
    private Double probability;
    private Double threshold;
    private Long transactionID;

    public Boolean getIsFraud() {
        return isFraud;
    }

    public void setIsFraud(Boolean isFraud) {
        this.isFraud = isFraud;
    }

    public Double getProbability() {
        return probability;
    }

    public void setProbability(Double probability) {
        this.probability = probability;
    }

    public Double getThreshold() {
        return threshold;
    }

    public void setThreshold(Double threshold) {
        this.threshold = threshold;
    }

    public Long getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(Long transactionID) {
        this.transactionID = transactionID;
    }
}