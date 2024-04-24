package bank.ooptest;

public class LocalClient extends Client{
    public LocalClient(int id, String identityKey, String firstName, String lastNames, char sex, int age, ListSED<Integer> accountListIDs,ListSED<Integer>  millionairesAccountsIDS){
        super(id, identityKey, firstName, lastNames, sex, age, accountListIDs, millionairesAccountsIDS);
    }
}
