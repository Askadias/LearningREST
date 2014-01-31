package ru.forxy.fraud.rest.pojo;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "fraudResponse")
public class FraudResponse {

    private Boolean isFraud;
    private Double probability;
    private Double threshold;

    private Entity root;
}
