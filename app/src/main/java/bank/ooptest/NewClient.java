package bank.ooptest;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;

import org.json.JSONException;

public class NewClient extends AppCompatActivity {
    private Bank bank;
    private NestedScrollView scrollView;
    private EditText editTextIdentityKey, editTextFirstName, editTextLastNames, editTextNumberAge, editTextNumberDecimalBalance, editTextNationality, editTextAddress ;
    private RadioGroup radioGroupSex, radioGroupForeign;
    private TextView textViewNationality, textViewAddress;
    private View focusedView;
    private int clientCounter, accountCounter;
    private char sex;
    private boolean isForeign;
    private Button buttonCreateClient, buttonCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_client);
        bank = Bank.getInstance();
        bank.initializeBank(getApplicationContext());

        clientCounter = getIntent().getIntExtra("clientCounter", 0);
        accountCounter = getIntent().getIntExtra("accountCounter", 0);


        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        editTextNationality = findViewById(R.id.editTextNationality);
        editTextAddress = findViewById(R.id.editTextAddress);
        radioGroupForeign = findViewById(R.id.radioGroupForeign);
        textViewNationality = findViewById(R.id.textViewNationality);
        textViewAddress = findViewById(R.id.textViewAddress);


        scrollView = findViewById(R.id.newClientScrollView);
        editTextIdentityKey = findViewById(R.id.editTextIdentityKey);
        editTextFirstName = findViewById(R.id.editTextFirstName);
        editTextLastNames = findViewById(R.id.editTextLastNames);
        editTextNumberAge = findViewById(R.id.editTextNumberAge);
        editTextNumberDecimalBalance = findViewById(R.id.editTextNumberDecimalBalance);
        radioGroupSex = findViewById(R.id.radioGroupSex);
        buttonCreateClient = findViewById(R.id.buttonCreateClient);
        buttonCancel = findViewById(R.id.buttonCreateClientCancel);

        // Add listener to scroll to focused EditText
        setupUI(findViewById(R.id.main));

        buttonCreateClient.setOnClickListener(v -> {
            if (validateFields()) {
                try {
                    createClient();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        buttonCancel.setOnClickListener(v -> {
            finish();
        });

        buttonCreateClient.setOnClickListener(v -> {
            if (validateFields()) {
                try {
                    createClient();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        radioGroupSex.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioButtonMan) {
                sex = 'M';
            } else if (checkedId == R.id.radioButtonWoman) {
                sex = 'W';
            }
        });

        radioGroupForeign.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioButtonForeignTrue) {
                textViewNationality.setVisibility(View.VISIBLE);
                editTextNationality.setVisibility(View.VISIBLE);
                textViewAddress.setVisibility(View.VISIBLE);
                editTextAddress.setVisibility(View.VISIBLE);

                isForeign = true;
            } else if (checkedId == R.id.radioButtonForeignFalse) {
                textViewNationality.setVisibility(View.GONE);
                editTextNationality.setVisibility(View.GONE);
                textViewAddress.setVisibility(View.GONE);
                editTextAddress.setVisibility(View.GONE);



                isForeign = false;
            }
        });

    }

    private boolean validateFields() {
        if (editTextIdentityKey.getText().toString().isEmpty() ||
                editTextFirstName.getText().toString().isEmpty() ||
                editTextLastNames.getText().toString().isEmpty() ||
                editTextNumberAge.getText().toString().isEmpty() ||
                editTextNumberDecimalBalance.getText().toString().isEmpty() ||
                radioGroupSex.getCheckedRadioButtonId() == -1 ||
                radioGroupForeign.getCheckedRadioButtonId() == -1
        ) {
            Toast.makeText(NewClient.this, "Please fill out all fields correctly.", Toast.LENGTH_SHORT).show();

            return false;
        }
        if (isForeign && editTextNationality.getText().toString().isEmpty() && editTextAddress.getText().toString().isEmpty()){
            Toast.makeText(NewClient.this, "Please fill out all fields correctly.", Toast.LENGTH_SHORT).show();
        }

        int age = Integer.parseInt(editTextNumberAge.getText().toString());
        double balance = Double.parseDouble(editTextNumberDecimalBalance.getText().toString());

        if (age < 13 || age > 250) {
            Toast.makeText(NewClient.this, "Age should be between 13 and 250.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (balance <= 1) {
            Toast.makeText(NewClient.this, "Opening balance should be greater than 1.", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (bank.verifyClient(editTextIdentityKey.getText().toString())){
            Toast.makeText(NewClient.this, "Client already registered.", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }


    private void createClient() throws JSONException {
        boolean flagMillionaire = false;
        if (Double.parseDouble(editTextNumberDecimalBalance.getText().toString()) > 1000000){
            flagMillionaire = true;
        }

        Account newAccount = new Account(accountCounter,editTextIdentityKey.getText().toString(), Double.parseDouble(editTextNumberDecimalBalance.getText().toString()), "MXN");
        ListSED<Integer> clientAccountListIDs = new ListSED<>();
        ListSED<Integer> millionairesAccountsIDS = new ListSED<>();
        if (flagMillionaire){
            millionairesAccountsIDS.add(newAccount.getId());
        } else{
            clientAccountListIDs.add(newAccount.getId());
        }

        if (isForeign){
            ForeignClient newClient = new ForeignClient (clientCounter,editTextIdentityKey.getText().toString(), editTextFirstName.getText().toString(), editTextLastNames.getText().toString(),sex, Integer.parseInt(editTextNumberAge.getText().toString()),clientAccountListIDs, millionairesAccountsIDS, editTextNationality.getText().toString(), editTextAddress.getText().toString());

            bank.addClient(newClient);
            bank.addAccount(newAccount.getId(), newAccount);
            CounterManager.saveCounters(getApplicationContext());
            bank.saveData(getApplicationContext());
            Toast.makeText(NewClient.this, "Foreign client and initial account created :)", Toast.LENGTH_SHORT).show();

            finish();
        } else {
            LocalClient newClient = new LocalClient (clientCounter,editTextIdentityKey.getText().toString(), editTextFirstName.getText().toString(), editTextLastNames.getText().toString(),sex, Integer.parseInt(editTextNumberAge.getText().toString()),clientAccountListIDs, millionairesAccountsIDS);

            bank.addClient(newClient);
            bank.addAccount(newAccount.getId(), newAccount);
            CounterManager.saveCounters(getApplicationContext());
            bank.saveData(getApplicationContext());
            Toast.makeText(NewClient.this, "Local client and initial account created :)", Toast.LENGTH_SHORT).show();

            finish();
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    private void setupUI(View view) {
        // Set up touch listener for non-text box views to hide keyboard
        if (!(view instanceof EditText)) {
            view.setOnTouchListener((v, event) -> {
                hideKeyboard();
                return false;
            });
        }

        // If a layout container, iterate over children and seed recursion
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }

        // Add focus change listener to scroll to focused EditText
        if (view instanceof EditText) {
            view.setOnFocusChangeListener((v, hasFocus) -> {
                if (hasFocus) {
                    focusedView = v;
                    scrollToView(focusedView);
                }
            });
        }
    }

    private void scrollToView(View view) {
        if (view instanceof EditText) {
            int index = ((ViewGroup) view.getParent()).indexOfChild(view);
            if (index > 0) {
                View viewAbove = ((ViewGroup) view.getParent()).getChildAt(index - 1);
                scrollView.post(() -> scrollView.scrollTo(0, viewAbove.getTop()));
            }
        }
    }


    private void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = getCurrentFocus();
        if (view == null) {
            view = new View(this);
        }
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
