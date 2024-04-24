package bank.ooptest;

import java.util.HashMap;

public abstract class Request {
    protected int id;
    protected String identityKey;// client foreign key, could be this or the int id
    protected int idAccount; // account foreign key
    protected double amount;

    public Request(int id, String identityKey, int idAccount, double amount) {
        this.id = id;
        this.identityKey = identityKey;
        this.idAccount = idAccount;
        this.amount = amount;
    }

    public abstract HashMap<Integer,Account> updateMoney(HashMap<Integer,Account> accounts, Request request);

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

    public int getIdAccount() {
        return idAccount;
    }

    public void setIdAccount(int idAccount) {
        this.idAccount = idAccount;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }


}
