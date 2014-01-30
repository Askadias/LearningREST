package ru.forxy.fraud.rest.pojo;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "fraudResponse")
public class FraudResponse {
    Boolean isFraud;
    Double probability;
    Double threeshold;
}
