package bank.ooptest;


import android.content.Context;

import org.json.JSONException;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Bank  {
    private static Bank instance;
    private ListSED<Client> clientList;
    private HashMap<Integer,Account> accountHashMap;
    private ListSED<Request> Requests;

    private ListSED<Account> millionaireAccounts; //these are in another bank

    private Bank() {
        // Initialize your clientList and accountHashMap here
        clientList = new ListSED<>();
        accountHashMap = new HashMap<>();
        Requests = new ListSED<>();
        millionaireAccounts = new ListSED<>();
    }


    public static Bank getInstance() {
        if (instance == null) {
            instance = new Bank();
        }
        return instance;
    }

    public void initializeBank(Context context) {
        clientList = JsonUtils.readClientsFromFile(context, "clients.json");
        accountHashMap = JsonUtils.readAccountsFromFile(context, "accounts.json");
        Requests = JsonUtils.readRequestsFromFile(context,"requests.json");
    }

    public void saveData(Context context) throws JSONException {
        JsonUtils.writeClientsToFile(context, "clients.json", clientList);
        JsonUtils.writeAccountsToFile(context, "accounts.json", accountHashMap);
        JsonUtils.writeRequestsToFile(context,"requests.json", this, Requests);
    }

    public void addClient(Client newClient) {
        clientList.add(newClient);
    }

    //add account to bank account list
    public void addAccount(int idAccount,Account newAccount) {
        accountHashMap.put(idAccount, newAccount);
    }

    //set account to client account list
    public void setClientAccount(String identityKey, Account account){
        NodeSED<Client> current = clientList.head;

        while (current != null) {
            if (Objects.equals(current.getValue().getIdentityKey(), identityKey)) {
                ListSED<Integer> accountList = current.getValue().getAccountList();
                accountList.add(account.getId());
                current.getValue().setAccountList(accountList);
                return;
            }
            current = current.getNext();
        }
    }
    public void deleteClient(String identityKey){
        try{
            ListSED<Integer> clientAccountList = new ListSED<>();
            NodeSED<Client> current = clientList.head;

            while (current != null && !current.getValue().getIdentityKey().equals(identityKey)) {
                current = current.getNext();
            }

            if (current != null) {
                clientAccountList = current.getValue().getAccountList();
                clientList.delete(current);

                for (int id : clientAccountList) {
                    deleteAccount(id);
                }
            }
        } catch (Exception e ){
            throw new RuntimeException(e);
        }
    }

    public void deleteAccount(int id){
        accountHashMap.remove(id);
        deleteAccountIdFromClient(id);
    }
    public void deleteAccountIdFromClient(int id){
        NodeSED<Client> current = clientList.head;

        while (current != null) {
            if (Objects.equals(current.getValue().getAccountList().get(current.getValue().getAccountList().search(id)), id)) {

                current.getValue().deleteAccountFromList(id);
            }
            current = current.getNext();
        }
    }

    public boolean verifyClient(String identityKey){
        for (Client client: this.clientList) {
            if (client.getIdentityKey().equals(identityKey)){
                return true;
            }
        }
        return false;
    }


    public boolean verifyAccount(String identityKey, int idAccount){
        //with this approach I don't need the o(n) complexity
        try {
            if (Objects.equals(Objects.requireNonNull(accountHashMap.get(idAccount)).getIdentityKey(), identityKey)){
                return true;
            }

        } catch (Exception e){
            return false;
        }
        return false;
    }

    public void makeDeposit(Deposit deposit){
        Requests.add(deposit);
        accountHashMap = deposit.updateMoney(accountHashMap, deposit);//?


    }
    public void makeWithdraw(Withdraw withdraw){
        Requests.add(withdraw);
        accountHashMap = withdraw.updateMoney(accountHashMap, withdraw);//?
    }

    public ListSED<Client> getClientList() {
        return clientList;
    }

    public void setClientList(ListSED<Client> clientList) {
        this.clientList = clientList;
    }

    public HashMap<Integer, Account> getAccountHashMap() {
        return accountHashMap;
    }

    public void setAccountHashMap(HashMap<Integer, Account> accountHashMap) {
        this.accountHashMap = accountHashMap;
    }

    public Account getAccount(int id){
        return accountHashMap.get(id);
    }
    public Client getClient(String identityKey) {
        NodeSED<Client> current = clientList.head;

        while (current != null) {
            if (Objects.equals(current.getValue().getIdentityKey(), identityKey)) {
                return current.getValue();
            }
            current = current.getNext();
        }

        return null;
    }

    public boolean moveMillionaires(){
        Iterator<Map.Entry<Integer, Account>> iterator = accountHashMap.entrySet().iterator();
        boolean flag = false;

        while (iterator.hasNext()) {
            Map.Entry<Integer, Account> entry = iterator.next();
            Integer key = entry.getKey();
            Account account = entry.getValue();

            if (account.getBalance() > 1000000){
                iterator.remove();
                millionaireAccounts.add(account);
                NodeSED<Client>current = clientList.head;
                while (current != null){
                    if (Objects.equals(current.getValue().getIdentityKey(), account.getIdentityKey())){
                        current.getValue().addMillionaireAccount(account.getId());
                    }
                    current = current.getNext();
                }

                flag=true;
            }
        }
        return flag;
    }


    public ListSED<Request> getRequests() {
        return Requests;
    }

    public void setRequests(ListSED<Request> requests) {
        Requests = requests;
    }

    public ListSED<Account> getMillionaireAccounts() {
        return millionaireAccounts;
    }

    public void setMillionaireAccounts(ListSED<Account> millionaireAccounts) {
        this.millionaireAccounts = millionaireAccounts;
    }
}
