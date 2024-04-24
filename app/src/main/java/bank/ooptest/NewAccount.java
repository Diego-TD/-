package bank.ooptest;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONException;

public class NewAccount extends AppCompatActivity {
    private Bank bank;
    private int accountCounter;
    private EditText editTextNewAccountIdentityKey, editTextNewAccountBalance, editTextNewAccountCurrency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_new_account);

        bank = Bank.getInstance();
        bank.initializeBank(getApplicationContext());

        accountCounter = getIntent().getIntExtra("accountCounter", 0);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button buttonCreateAccount = findViewById(R.id.buttonCreateAccount);
        Button buttonCreateAccountCancel = findViewById(R.id.buttonCreateAccountCancel);
        editTextNewAccountIdentityKey = findViewById(R.id.editTextNewAccountIdentityKey);
        editTextNewAccountBalance = findViewById(R.id.editTextNewAccountBalance);
        editTextNewAccountCurrency = findViewById(R.id.editTextNewAccountCurrency);



        buttonCreateAccount.setOnClickListener(view -> {
            if (validateFields()) {
                try {
                    createAccount();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        buttonCreateAccountCancel.setOnClickListener(view -> {
            finish();
        });
    }

    private void createAccount() throws JSONException {
        Account account = new Account(accountCounter,editTextNewAccountIdentityKey.getText().toString(), Double.parseDouble(editTextNewAccountBalance.getText().toString()), editTextNewAccountCurrency.getText().toString());
        bank.addAccount(account.getId(),account);
        bank.setClientAccount(editTextNewAccountIdentityKey.getText().toString(), account);

        Toast.makeText(NewAccount.this, "Account created.", Toast.LENGTH_SHORT).show();
        bank.saveData(getApplicationContext());
        CounterManager.saveCounters(getApplicationContext());
        finish();

    }

    private boolean validateFields() {
        if (editTextNewAccountIdentityKey.getText().toString().isEmpty() ||
                editTextNewAccountBalance.getText().toString().isEmpty() ||
                editTextNewAccountCurrency.getText().toString().isEmpty()) {
            Toast.makeText(NewAccount.this, "Please fill out all fields correctly.", Toast.LENGTH_SHORT).show();
            return false;
        }

        double balance = Double.parseDouble(editTextNewAccountBalance.getText().toString());
        if (balance <= 1) {
            Toast.makeText(NewAccount.this, "Opening balance should be greater than 1.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!bank.verifyClient(editTextNewAccountIdentityKey.getText().toString())){
            Toast.makeText(NewAccount.this, "Client not registered, please register the client from outside.", Toast.LENGTH_SHORT).show(); //not the most intuitive, but works
            return false;
        }


        return true;
    }
}