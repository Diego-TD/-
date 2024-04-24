package bank.ooptest;

import java.util.Objects;

public class Client {
    protected int id;

    protected String identityKey;
    protected String firstName;
    protected String lastNames;
    protected char sex;
    protected int age;
    protected ListSED<Integer> accountListIDs;
    protected ListSED<Integer> millionairesAccountsIDS;

    public Client(int id, String identityKey, String firstName, String lastNames, char sex, int age, ListSED<Integer> accountListIDs, ListSED<Integer> millionairesAccountsIDS) {
        this.id = id;
        this.identityKey = identityKey;
        this.firstName = firstName;
        this.lastNames = lastNames;
        this.sex = sex;
        this.age = age;
        this.accountListIDs = accountListIDs;
        this.millionairesAccountsIDS = millionairesAccountsIDS;
    }

    public Client() {

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

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastNames() {
        return lastNames;
    }

    public void setLastNames(String lastNames) {
        this.lastNames = lastNames;
    }

    public char getSex() {
        return sex;
    }

    public void setSex(char sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public ListSED<Integer> getAccountList() {
        return accountListIDs;
    }

    public void setAccountList(ListSED<Integer> accountListIDs) {
        this.accountListIDs = accountListIDs;
    }

    public void deleteAccountFromList(int id){
        NodeSED<Integer> current = accountListIDs.head;

        while (current != null) {
            if (Objects.equals(current.getValue(), id)) {
                accountListIDs.delete(current);
            }
            current = current.getNext();
        }
    }
    public void addAccount(int id){
        this.accountListIDs.add(id);
    }

    public ListSED<Integer> getMillionairesAccountsIDS() {
        return millionairesAccountsIDS;
    }

    public void setMillionairesAccountsIDS(ListSED<Integer> millionairesAccountsIDS) {
        this.millionairesAccountsIDS = millionairesAccountsIDS;
    }

    public void addMillionaireAccount(int id){
        this.millionairesAccountsIDS.add(id);
        deleteAccountFromList(id);
    }
}
