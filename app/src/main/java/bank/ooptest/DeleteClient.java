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

public class DeleteClient extends AppCompatActivity {
    private EditText editTextIdentityKey;
    private Bank bank;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_delete_client);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        bank = Bank.getInstance();
        bank.initializeBank(getApplicationContext());

        editTextIdentityKey = findViewById(R.id.editTextDeleteClientIdentityKey);
        Button buttonDelete = findViewById(R.id.buttonDeleteClient);
        Button buttonCancel = findViewById(R.id.buttonDeleteClientCancel);

        buttonDelete.setOnClickListener(view -> {
            if (validateFields()) {
                try {
                    deleteClient();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        buttonCancel.setOnClickListener(view -> {
            finish();
        });


    }

    private void deleteClient() throws JSONException {
        String identityKey = editTextIdentityKey.getText().toString();
        bank.deleteClient(identityKey);
        Toast.makeText(DeleteClient.this, "Client and accounts deleted.", Toast.LENGTH_SHORT).show();
        bank.saveData(getApplicationContext());
        finish();
    }

    private boolean validateFields() {
        if (editTextIdentityKey.getText().toString().isEmpty()) {
            Toast.makeText(DeleteClient.this, "Please fill out all fields correctly.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!bank.verifyClient(editTextIdentityKey.getText().toString())){
            Toast.makeText(DeleteClient.this, "Client doens't exists.", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}