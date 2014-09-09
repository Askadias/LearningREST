package ru.forxy.fraud.rest.v1.check;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import ru.forxy.fraud.rest.v1.check.payment.Payment;
import ru.forxy.fraud.rest.v1.check.person.Account;
import ru.forxy.fraud.rest.v1.check.product.Product;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Document(collection = "transaction")
public class Transaction extends Entity {

    private static final long serialVersionUID = 4484743540234608280L;

    @Id
    private String transactionID;
    private String ipAddress;
    private String machineGUID;
    private Account account;
    private List<Payment> payments;
    private List<? extends Product> products;

    public String getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(String transactionID) {
        this.transactionID = transactionID;
    }

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
