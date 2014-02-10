package ru.forxy.fraud.rest.pojo;

import ru.forxy.fraud.rest.pojo.payment.Payment;
import ru.forxy.fraud.rest.pojo.person.Account;
import ru.forxy.fraud.rest.pojo.product.Product;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "transaction")
public class Transaction extends Entity {
    private String ipAddress;
    private String machineGUID;
    private Account account;
    private List<Payment> payments;
    private List<? extends Product> products;

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getMachineGUID() {
        return machineGUID;
    }

    public void setMachineGUID(String machineGUID) {
        this.machineGUID = machineGUID;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }

    public List<? extends Product> getProducts() {
        return products;
    }

    public void setProducts(List<? extends Product> products) {
        this.products = products;
    }
}