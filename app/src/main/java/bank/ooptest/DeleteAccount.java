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

public class DeleteAccount extends AppCompatActivity {
    private Bank bank;
    private EditText editTextDeleteAccountAccountId, editTextDeleteAccountIdentityKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_delete_account);

        bank = Bank.getInstance();
        bank.initializeBank(getApplicationContext());

        Button buttonDeleteAccountDelete = findViewById(R.id.buttonDeleteAccountDelete);
        Button buttonDeleteAccountCancel = findViewById(R.id.buttonDeleteAccountCancel);
        editTextDeleteAccountAccountId = findViewById(R.id.editTextDeleteAccountAccountId);
        editTextDeleteAccountIdentityKey = findViewById(R.id.editTextDeleteAccountIdentityKey);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        buttonDeleteAccountDelete.setOnClickListener(view -> {
            if (validateFields()) {
                try {
                    deleteAccount();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        buttonDeleteAccountCancel.setOnClickListener(view -> {
            finish();
        });
    }

    private boolean validateFields() {
        if (editTextDeleteAccountAccountId.getText().toString().isEmpty() ||
                editTextDeleteAccountIdentityKey.getText().toString().isEmpty()) {
            Toast.makeText(DeleteAccount.this, "Please fill out all fields correctly.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!bank.verifyClient(editTextDeleteAccountIdentityKey.getText().toString())){
            Toast.makeText(DeleteAccount.this, "Client not registered, please register the client from outside.", Toast.LENGTH_SHORT).show(); //not the most intuitive, but works
            return false;
        }

        if (!bank.verifyAccount(editTextDeleteAccountIdentityKey.getText().toString(),Integer.parseInt(editTextDeleteAccountAccountId.getText().toString()))){
            Toast.makeText(DeleteAccount.this, "The account does not exists.", Toast.LENGTH_SHORT).show();
            return false;
        }



        if(bank.getClient(editTextDeleteAccountIdentityKey.getText().toString()).getAccountList().length() == 1){
            Toast.makeText(DeleteAccount.this, "Client only have one account, delete client instead.", Toast.LENGTH_SHORT).show();
        }

        return true;
    }

    private void deleteAccount() throws JSONException {
        bank.deleteAccount(Integer.parseInt(editTextDeleteAccountAccountId.getText().toString()));
        Toast.makeText(DeleteAccount.this, "Account deleted.", Toast.LENGTH_SHORT).show();
        //TODO: delete the account in the client account list id
        bank.saveData(getApplicationContext());
        finish();
    }
}