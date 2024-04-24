package bank.ooptest;

public class Account {
    private int id;
    private String identityKey; //client foreign key
    private double balance;
    private String currency; //peso mxn

    public Account(int id, String identityKey, double balance, String currency) {
        this.id = id;
        this.identityKey = identityKey;
        this.balance = balance;
        this.currency = currency;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIdentityKey() {
        return identityKey;
    }

    public void setIdentityKey(String identityKey) {
        this.identityKey = identityKey;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double money) {
        this.balance = money;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }


}
