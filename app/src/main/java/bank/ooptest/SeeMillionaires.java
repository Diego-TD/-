package bank.ooptest;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.HashMap;

public class SeeMillionaires extends AppCompatActivity {
    private static Bank bank;
    private static TextView textViewMillionaires;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_see_millionaires);

        bank = Bank.getInstance();
        bank.initializeBank(getApplicationContext());

        textViewMillionaires = findViewById(R.id.textViewSeeMillionaires);
        Button buttonCancel = findViewById(R.id.buttonSeeMillionairesCancel);

        buttonCancel.setOnClickListener(view -> {
            finish();
        });


        seeMillionaires();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public static void seeMillionaires() {
        StringBuilder clients = new StringBuilder("Millionaires: \n");
        ListSED<Client> clientsList = bank.getClientList();


        for (Client client: clientsList) {
            if (client == null){
                continue;
            }

            if (client.getMillionairesAccountsIDS().isEmpty()){
                continue;
            }

            if (client instanceof LocalClient){
                clients.append("\n\n\nID: ").append(client.getId()).append(" \nIdentity Key: ").append(client.getIdentityKey()).append(" \nFirst Name: ").append(client.getFirstName()).append(" \nLast Names: ").append(client.getLastNames()).append(" \nSex: ").append(client.getSex()).append(" \nAge: ").append(client.getAge());

                clients.append("\nAccounts:\n");
                ListSED<Integer> accountsIds = client.getMillionairesAccountsIDS();
                HashMap<Integer,Account> hashmap = bank.getAccountHashMap();
                for (int id : accountsIds) {
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
                ListSED<Integer> accountsIds = client.getMillionairesAccountsIDS();
                HashMap<Integer,Account> hashmap = bank.getAccountHashMap();
                for (int id : accountsIds) {
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

        textViewMillionaires.setText(clients.toString());
    }
}