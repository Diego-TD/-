package bank.ooptest;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.w3c.dom.Text;

import java.util.HashMap;

public class SeeAllClients extends AppCompatActivity {
    private static Bank bank;
    private static TextView textViewAccounts;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_see_all_clients);

        bank = Bank.getInstance();
        bank.initializeBank(getApplicationContext());

        textViewAccounts = findViewById(R.id.textViewSeeAllClients);
        Button buttonSeeAllClientsCancel = findViewById(R.id.buttonSeeAllClientsCancel);

        buttonSeeAllClientsCancel.setOnClickListener(view -> {
            finish();
        });

        seeAllClients();


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public static void seeAllClients(){
        StringBuilder clients = new StringBuilder("Clients: \n");
        ListSED<Client> clientsList = bank.getClientList();
        HashMap<Integer,Account> hashmap = bank.getAccountHashMap();


        for (Client client: clientsList) {
            if (client == null){
                continue;
            }
            if (client instanceof LocalClient){
                clients.append("\n\n\nID: ").append(client.getId()).append(" \nIdentity Key: ").append(client.getIdentityKey()).append(" \nFirst Name: ").append(client.getFirstName()).append(" \nLast Names: ").append(client.getLastNames()).append(" \nSex: ").append(client.getSex()).append(" \nAge: ").append(client.getAge());

                clients.append("\nAccounts:\n");
                ListSED<Integer> accountsIds = client.getAccountList();
                for (int id : accountsIds) {
                    Account account = hashmap.get(id);
                    if (account != null) {
                        clients.append("ID: ").append(account.getId()).append(" Identity Key: ").append(account.getIdentityKey()).append(" Balance: ").append(account.getBalance()).append(" Currency: ").append(account.getCurrency()).append("\n");
                    } else {
                        clients.append("No account found \n");
                    }
                }
                clients.append("\nMillionaire Accounts (not in this bank):\n");
                ListSED<Integer> millionaireAccountsIds = client.getMillionairesAccountsIDS();
                for (int id : millionaireAccountsIds) {
                    Account account = hashmap.get(id);
                    if (account != null) {
                        clients.append("ID: ").append(account.getId()).append(" Identity Key: ").append(account.getIdentityKey()).append(" Balance: ").append(account.getBalance()).append(" Currency: ").append(account.getCurrency()).append("\n");
                    } else {
                        clients.append("No account found \n");
                    }
                }
            } else if (client instanceof ForeignClient){
                clients.append("\n\n\nID: ").append(client.getId()).append(" \nIdentity Key: ").append(client.getIdentityKey()).append(" \nFirst Name: ").append(client.getFirstName()).append(" \nLast Names: ").append(client.getLastNames()).append(" \nSex: ").append(client.getSex()).append(" \nAge: ").append(client.getAge());

                clients.append("\nAccounts:\n");
                ListSED<Integer> accountsIds = client.getAccountList();
                for (int id : accountsIds) {
                    Account account = hashmap.get(id);
                    if (account != null) {
                        clients.append("ID: ").append(account.getId()).append(" Identity Key: ").append(account.getIdentityKey()).append(" Balance: ").append(account.getBalance()).append(" Currency: ").append(account.getCurrency()).append("\n");
                    } else {
                        clients.append("No account found \n");
                    }
                }
                clients.append("\nMillionaire Accounts (not in this bank):\n");
                ListSED<Integer> millionaireAccountsIds = client.getMillionairesAccountsIDS();
                for (int id : millionaireAccountsIds) {
                    Account account = hashmap.get(id);
                    if (account != null) {
                        clients.append("ID: ").append(account.getId()).append(" Identity Key: ").append(account.getIdentityKey()).append(" Balance: ").append(account.getBalance()).append(" Currency: ").append(account.getCurrency()).append("\n");
                    } else {
                        clients.append("No account found \n");
                    }
                }

                clients.append("\nNationailty: ").append(((ForeignClient) client).getNationality()).append("\nAddress: ").append(((ForeignClient) client).getAddress());
            }

        }

        textViewAccounts.setText(clients.toString());
    }
}