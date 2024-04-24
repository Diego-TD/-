package bank.ooptest;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONException;

import java.util.HashMap;
import java.util.List;

public class SeeClientAccounts extends AppCompatActivity {
    private Bank bank;
    private TextView textViewSeeClientsAccounts;
    EditText editTextSeeClientAccountIdentityKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_see_client_accounts);

        bank = Bank.getInstance();
        bank.initializeBank(getApplicationContext());

        editTextSeeClientAccountIdentityKey = findViewById(R.id.editTextSeeClientAccountIdentityKey);
        textViewSeeClientsAccounts = findViewById(R.id.textViewSeeClientsAccounts);

        Button buttonSeeClientsAccountsSee = findViewById(R.id.buttonSeeClientsAccountsSee);
        Button buttonCreateSeeClientsAccountsCancel = findViewById(R.id.buttonCreateSeeClientsAccountsCancel);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        buttonSeeClientsAccountsSee.setOnClickListener(view -> {
            if (validateFields()) {
                seeClientAccounts();
            }
        });

        buttonCreateSeeClientsAccountsCancel.setOnClickListener(view -> {
            finish();
        });
    }

    private void seeClientAccounts(){
        StringBuilder accounts = new StringBuilder("Accounts: \n");
        String identityKey= editTextSeeClientAccountIdentityKey.getText().toString();
        Client client = bank.getClient(identityKey);
        ListSED<Integer> accountsIds = client.getAccountList();
        HashMap<Integer,Account> hashmap = bank.getAccountHashMap();
        for (int id : accountsIds) {
            Account account = hashmap.get(id);
            if (account != null) {
                accounts.append("ID: ").append(account.getId()).append(" Identity key: ").append(account.getIdentityKey()).append(" Balance: ").append(account.getBalance()).append(" Currency: ").append(account.getCurrency()).append("\n");
            } else {
                accounts.append("No account found \n");
            }
        }

        textViewSeeClientsAccounts.setText(accounts.toString());
    }
    private boolean validateFields() {
        if (editTextSeeClientAccountIdentityKey.getText().toString().isEmpty()){
            Toast.makeText(SeeClientAccounts.this, "Please fill out all fields correctly.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!bank.verifyClient(editTextSeeClientAccountIdentityKey.getText().toString())){
            Toast.makeText(SeeClientAccounts.this, "Client does not exists.", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}