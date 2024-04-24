package bank.ooptest;

import java.util.HashMap;

public class Withdraw extends Request {
    private String firstName;
    private String lastName;
    private char sex;
    private String nationality;
    private String address;

    public Withdraw(int id, String identityKey, int idAccount, double amount, String firstName, String lastName, char sex) {
        super(id, identityKey, idAccount, amount);
        this.firstName = firstName;
        this.lastName = lastName;
        this.sex = sex;
    }
    public Withdraw(int id, String identityKey, int idAccount, double amount, String firstName, String lastName, char sex, String nationality, String address) {
        super(id, identityKey, idAccount, amount);
        this.firstName = firstName;
        this.lastName = lastName;
        this.sex = sex;
        this.nationality = nationality;
        this.address = address;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public char getSex() {
        return sex;
    }

    public void setSex(char sex) {
        this.sex = sex;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public HashMap<Integer, Account> updateMoney(HashMap<Integer, Account> accounts, Request request) {
        if (accounts == null || request == null) {
            throw new IllegalArgumentException("Accounts and request must not be null");
        }


        if (!(request instanceof Withdraw)) {
            return accounts;
        }

        Withdraw Withdraw = (Withdraw) request;

        if (!accounts.containsKey(Withdraw.idAccount)) {
            throw new IllegalArgumentException("Account does not exist");
        }

        Account account = accounts.get(Withdraw.idAccount);
        account.setBalance(account.getBalance() - Withdraw.getAmount());

        return accounts;
    }
}

