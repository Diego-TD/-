package bank.ooptest;

import java.util.HashMap;

public class Deposit extends Request{
    private String nameSender;
    public Deposit(int id, String identityKey, int idAccount, double amount, String nameSender) {
        super(id, identityKey, idAccount, amount);
        this.nameSender = nameSender;
    }

    public String getNameSender() {
        return nameSender;
    }

    public void setNameSender(String nameSender) {
        this.nameSender = nameSender;
    }


    @Override
    public HashMap<Integer, Account> updateMoney(HashMap<Integer, Account> accounts, Request request) {
        if (accounts == null || request == null) {
            throw new IllegalArgumentException("Accounts and request must not be null");
        }


        if (!(request instanceof Deposit)) {
            return accounts;
        }

        Deposit deposit = (Deposit) request;

        if (!accounts.containsKey(deposit.idAccount)) {
            throw new IllegalArgumentException("Account does not exist");
        }

        Account account = accounts.get(deposit.idAccount);
        account.setBalance(account.getBalance() + deposit.getAmount());

        return accounts;
    }
}
