package ru.forxy.fraud.rest.pojo;

import ru.forxy.fraud.rest.pojo.payment.Payment;
import ru.forxy.fraud.rest.pojo.person.Account;
import ru.forxy.fraud.rest.pojo.product.Product;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "transaction")
public class Transaction extends Entity {
    private Account account;
    private List<Payment> payments;
    private List<Product> products;
}