package ru.forxy.fraud.test.utils.data;

import ru.forxy.common.test.utils.gen.AbstractGenerator;
import ru.forxy.fraud.rest.v1.Transaction;
import ru.forxy.fraud.rest.v1.payment.Amount;
import ru.forxy.fraud.rest.v1.payment.Payment;
import ru.forxy.fraud.rest.v1.person.Account;
import ru.forxy.fraud.rest.v1.person.Person;
import ru.forxy.fraud.rest.v1.person.Telephone;
import ru.forxy.fraud.rest.v1.person.Traveler;
import ru.forxy.fraud.rest.v1.product.Product;
import ru.forxy.fraud.rest.v1.product.travel.TravelProduct;

import java.util.ArrayList;
import java.util.List;

import static ru.forxy.common.test.utils.gen.DateGenerator.generateDateInFuture;
import static ru.forxy.common.test.utils.gen.FinanceGenerator.generateCurrencyCode;
import static ru.forxy.common.test.utils.gen.NumbersGenerator.generateGUID;
import static ru.forxy.common.test.utils.gen.NumbersGenerator.generateInt;
import static ru.forxy.common.test.utils.gen.NumbersGenerator.generateNumber;
import static ru.forxy.common.test.utils.gen.PersonGenerator.generateAge;
import static ru.forxy.common.test.utils.gen.PersonGenerator.generateBirthDate;
import static ru.forxy.common.test.utils.gen.PersonGenerator.generateEmail;
import static ru.forxy.common.test.utils.gen.PersonGenerator.generateFirstName;
import static ru.forxy.common.test.utils.gen.PersonGenerator.generateGender;
import static ru.forxy.common.test.utils.gen.PersonGenerator.generateLastName;
import static ru.forxy.common.test.utils.gen.PersonGenerator.generatePasswordData;

/**
 * Generates Fraud Specific data
 */
public abstract class TravelDataGenerator extends AbstractGenerator {

    public static Transaction generateTransaction() {
        Transaction transaction = new Transaction();
        transaction.setAccount(generateAccount());
        transaction.setMachineGUID(generateGUID());

        int productsCount = generateInt(1, 4);
        List<Product> products = new ArrayList<Product>(productsCount);
        for (int i = 0; i < productsCount; i++) {
            products.add(generateTravelProduct(TravelProduct.Type.values()[RAND.nextInt(4)], generateInt(100, 10000)));
        }
        transaction.setProducts(products);

        int paymentsCount = generateInt(1, 2);
        List<Payment> payments = new ArrayList<Payment>(paymentsCount);
        for (int i = 0; i < paymentsCount; i++) {
            payments.add(generatePayment(transaction.getAccount()));
        }
        transaction.setPayments(payments);

        return transaction;
    }

    public static Payment generatePayment(Person owner) {
        Payment payment = new Payment();
        payment.setAmount(generateAmount(generateInt(300, 20000)));
        payment.setFormOfPayment("CreditCard");
        payment.setOwner(owner);
        return payment;
    }

    public static Product generateProduct(final double amount) {
        return fillProductData(new Product(), amount);
    }

    public static TravelProduct generateTravelProduct(final TravelProduct.Type type, final double amount) {
        TravelProduct product = fillProductData(new TravelProduct(), amount);
        product.setDateStart(generateDateInFuture(5, 10));
        product.setDateEnd(generateDateInFuture(10, 15));
        product.setType(type);
        return product;
    }

    private static <T extends Product> T fillProductData(final T product, final double amount) {
        product.setPrice(generateAmount(amount));
        return product;
    }

    public static Amount generateAmount(final double amount) {
        return fillAmountData(new Amount(), amount);
    }

    private static <T extends Amount> T fillAmountData(final T amount, final double value) {
        amount.setCurrency(generateCurrencyCode());
        amount.setValue(value);
        amount.setUsdValue(value);
        return amount;
    }

    public static Person generatePerson() {
        return fillPersonData(new Person());
    }

    public static Traveler generateTraveler() {
        Traveler traveler = new Traveler();
        fillPersonData(traveler);
        traveler.setIsPrimary(RAND.nextBoolean());
        return traveler;
    }

    public static Account generateAccount() {
        Account account = new Account();
        fillPersonData(account);
        account.setLogin(generateEmail(account.getFirstName()));
        account.setPassword(generatePasswordData());
        return account;
    }

    private static <T extends Person> T fillPersonData(final T person) {
        person.setAge(generateAge());
        person.setGender(generateGender().charAt(0));
        person.setFirstName(generateFirstName(person.getGender() == 'M'));
        person.setLastName(generateLastName());
        person.setEmail(generateEmail(person.getFirstName()));
        person.setBirthDate(generateBirthDate(person.getAge()));
        person.setMiddleName(generateFirstName(person.getGender() == 'M'));

        final int phonesCount = RAND.nextInt(4);
        if (phonesCount > 0) {
            List<Telephone> telephones = new ArrayList<Telephone>(phonesCount);
            for (int i = 0; i < phonesCount; i++) {
                Telephone telephone = new Telephone();
                telephone.setAreaCode(generateNumber(3));
                telephone.setCountryAccessCode(generateNumber(2));
                telephone.setPhoneNumber(generateNumber(5, false));
                telephone.setType(Telephone.Type.values()[RAND.nextInt(3)]);
                telephones.add(telephone);
            }
            person.setTelephones(telephones);
        }
        return person;
    }
}
