package bank.ooptest;

public class ForeignClient extends Client {
    private String nationality;
    private String address;
    public ForeignClient(int id, String identityKey, String firstName, String lastNames, char sex, int age, ListSED<Integer> accountListIDs, ListSED<Integer> millionairesAccountsIDS, String nationality, String address){
        super(id, identityKey, firstName, lastNames, sex, age, accountListIDs, millionairesAccountsIDS);
        this.nationality = nationality;
        this.address = address;
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
}
