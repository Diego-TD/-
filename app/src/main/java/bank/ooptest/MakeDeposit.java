package bank.ooptest;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONException;

public class MakeDeposit extends AppCompatActivity {
    private Bank bank;
    private int depositCounter;
    private EditText editTextMakeDepositIdentityKey, editTextMakeDepositAccountId, editTextakeDepositAmount, editTextakeDepositSender;
    private boolean foreignCurrency;
    private RadioGroup radioGroupForeignCurrency;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_make_deposit);

        bank = Bank.getInstance();
        bank.initializeBank(getApplicationContext());

        depositCounter = getIntent().getIntExtra("depositCounter", 0);
        radioGroupForeignCurrency = findViewById(R.id.radioGroupForeignCurrency);
        editTextMakeDepositIdentityKey = findViewById(R.id.editTextMakeDepositIdentityKey);
        editTextMakeDepositAccountId = findViewById(R.id.editTextMakeDepositAccountId);
        editTextakeDepositAmount = findViewById(R.id.editTextakeDepositAmount);
        editTextakeDepositSender = findViewById(R.id.editTextakeDepositSender);



        Button buttonMakeDepositDeposit = findViewById(R.id.buttonMakeDepositDeposit);
        Button buttonMakeDepositCancel = findViewById(R.id.buttonMakeDepositCancel);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        radioGroupForeignCurrency.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioButtonForeignCurrencyTrue) {
                foreignCurrency = true;
            } else if (checkedId == R.id.radioButtonForeignCurrencyFalse) {
                foreignCurrency = false;
            }
        });

        buttonMakeDepositDeposit.setOnClickListener(view -> {
            if (validateFields()) {
                try {
                    makeDeposit();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        buttonMakeDepositCancel.setOnClickListener(view -> {
            finish();
        });
    }

    private boolean validateFields() {
        if (editTextMakeDepositIdentityKey.getText().toString().isEmpty() ||
                editTextMakeDepositAccountId.getText().toString().isEmpty() ||
                editTextakeDepositAmount.getText().toString().isEmpty() ||
                editTextakeDepositSender.getText().toString().isEmpty() ||
                radioGroupForeignCurrency.getCheckedRadioButtonId() == -1
        ){
            Toast.makeText(MakeDeposit.this, "Please fill out all fields correctly.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!bank.verifyClient(editTextMakeDepositIdentityKey.getText().toString())){
            Toast.makeText(MakeDeposit.this, "Client does not exists.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!bank.verifyAccount(editTextMakeDepositIdentityKey.getText().toString(), Integer.parseInt(editTextMakeDepositAccountId.getText().toString()))){
            Toast.makeText(MakeDeposit.this, "No account found.", Toast.LENGTH_SHORT).show();
            // here I could display a message to select if i want to create an account, the client exists.
            // start method, select yes or on, new activity, then I do this request? does that involves threads?
            return false;
        }

        if (Double.parseDouble(editTextakeDepositAmount.getText().toString()) <= 1){
            Toast.makeText(MakeDeposit.this, "Amount to deposit must be higher than 1.", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void makeDeposit() throws JSONException {
        double moneyToDeposit = Double.parseDouble(editTextakeDepositAmount.getText().toString());
        if (foreignCurrency){
            moneyToDeposit *= 20;
        }

        Deposit deposit = new Deposit(depositCounter, editTextMakeDepositIdentityKey.getText().toString(), Integer.parseInt(editTextMakeDepositAccountId.getText().toString()),moneyToDeposit,editTextakeDepositSender.getText().toString());
        bank.makeDeposit(deposit);

        Toast.makeText(MakeDeposit.this, "Deposit done.", Toast.LENGTH_SHORT).show();
        bank.saveData(getApplicationContext());
        CounterManager.saveCounters(getApplicationContext());
        finish();
    }
}