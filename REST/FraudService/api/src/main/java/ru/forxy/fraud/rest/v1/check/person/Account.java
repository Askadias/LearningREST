package ru.forxy.fraud.rest.v1.check.person;

public class Account extends Person {

    private static final long serialVersionUID = 1935391474832166380L;

    protected String login;
    protected String password;
    protected Statistics statistics;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Statistics getStatistics() {
        return statistics;
    }

    public void setStatistics(Statistics statistics) {
        this.statistics = statistics;
    }
}
