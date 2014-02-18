package ru.forxy.fraud.db.dto;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * Currency exchange information
 */
@Document
public class Currency {
    @Id
    private String symbol;
    private Double usdRate;
    private Date updateDate;

    public Currency() {
    }

    public Currency(String symbol, Double usdRate, Date updateDate) {
        this.symbol = symbol;
        this.usdRate = usdRate;
        this.updateDate = updateDate;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Double getUsdRate() {
        return usdRate;
    }

    public void setUsdRate(Double usdRate) {
        this.usdRate = usdRate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
