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

public class MakeWithdraw extends AppCompatActivity {
    private Bank bank;
    private int withdrawCounter;

    private EditText editTextMakeWithdrawIdentityKey, editTextMakeWithdrawAccountId, editTextMakeWithdrawAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_make_withdraw);

        bank = Bank.getInstance();
        bank.initializeBank(getApplicationContext());

        withdrawCounter = getIntent().getIntExtra("withdrawCounter", 0);

        editTextMakeWithdrawIdentityKey = findViewById(R.id.editTextMakeWithdrawIdentityKey);
        editTextMakeWithdrawAccountId = findViewById(R.id.editTextMakeWithdrawAccountId);
        editTextMakeWithdrawAmount = findViewById(R.id.editTextMakeWithdrawAmount);

        Button buttonMakeWithdrawWithdraw = findViewById(R.id.buttonMakeWithdrawWithdraw);
        Button buttonMakeWithdrawCancel = findViewById(R.id.buttonMakeWithdrawCancel);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        buttonMakeWithdrawWithdraw.setOnClickListener(view -> {
            if (validateFields()) {
                try {
                    makeWithdraw();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        buttonMakeWithdrawCancel.setOnClickListener(view -> {
            finish();
        });
    }

    private boolean validateFields() {
        if (editTextMakeWithdrawIdentityKey.getText().toString().isEmpty() ||
                editTextMakeWithdrawAccountId.getText().toString().isEmpty() ||
                editTextMakeWithdrawAmount.getText().toString().isEmpty()){
            Toast.makeText(MakeWithdraw.this, "Please fill out all fields correctly.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!bank.verifyClient(editTextMakeWithdrawIdentityKey.getText().toString())){
            Toast.makeText(MakeWithdraw.this, "Client does not exists.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!bank.verifyAccount(editTextMakeWithdrawIdentityKey.getText().toString(), Integer.parseInt(editTextMakeWithdrawAccountId.getText().toString()))){
            Toast.makeText(MakeWithdraw.this, "No account found.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (Double.parseDouble(editTextMakeWithdrawAmount.getText().toString()) <= 1){
            Toast.makeText(MakeWithdraw.this, "Amount to withdraw must be higher than 1.", Toast.LENGTH_SHORT).show();
            return false;
        }

        Account account = bank.getAccount(Integer.parseInt(editTextMakeWithdrawAccountId.getText().toString()));
        if (Double.parseDouble(editTextMakeWithdrawAmount.getText().toString()) > account.getBalance()){
            Toast.makeText(MakeWithdraw.this, "Account has insufficient funds.", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void makeWithdraw() throws JSONException {
        String identityKey = editTextMakeWithdrawIdentityKey.getText().toString();
        Client client = bank.getClient(identityKey);


        if (client instanceof LocalClient){
            Withdraw withdraw = new Withdraw(withdrawCounter, identityKey, Integer.parseInt(editTextMakeWithdrawAccountId.getText().toString()),Double.parseDouble(editTextMakeWithdrawAmount.getText().toString()), client.getFirstName(), client.getLastNames(), client.getSex());
            bank.makeWithdraw(withdraw);
        } else if (client instanceof ForeignClient) {
            Withdraw withdraw = new Withdraw(withdrawCounter, identityKey, Integer.parseInt(editTextMakeWithdrawAccountId.getText().toString()),Double.parseDouble(editTextMakeWithdrawAmount.getText().toString()), client.getFirstName(), client.getLastNames(), client.getSex(), ((ForeignClient) client).getNationality(),((ForeignClient) client).getAddress());
            bank.makeWithdraw(withdraw);
        }

        Toast.makeText(MakeWithdraw.this, "Withdraw done.", Toast.LENGTH_SHORT).show();
        bank.saveData(getApplicationContext());
        CounterManager.saveCounters(getApplicationContext());
        finish();
    }
}