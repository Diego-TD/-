package bank.ooptest;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONException;

import java.io.Serializable;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private Bank bank;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);


        bank = Bank.getInstance();
        bank.initializeBank(getApplicationContext());

        initializeCounters();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageButton imageButtonNewClient = findViewById(R.id.imageButtonNewClient);
        ImageButton imageButtonDeleteClient = findViewById(R.id.imageButtonDeleteClient);
        ImageButton imageButtonSeeAllClients = findViewById(R.id.imageButtonSeeAllClients);

        ImageButton imageButtonNewAccount = findViewById(R.id.imageButtonNewAccount);
        ImageButton imageButtonSeeClientAccounts = findViewById(R.id.imageButtonSeeClientAccounts);
        ImageButton imageButtonDeleteAccount = findViewById(R.id.imageButtonDeleteAccount);
        //ImageButton imageButtonSeeAllAccounts = findViewById(R.id.imageButtonSeeAllAccounts);

        ImageButton imageButtonSeeAllRequests = findViewById(R.id.imageButtonSeeAllRequests);

        ImageButton imageButtonDeposit = findViewById(R.id.imageButtonDeposit);
        //ImageButton imageButtonSeeClientsDeposits = findViewById(R.id.imageButtonSeeClientsDeposits);
        //ImageButton imageButtonSeeAllDeposits = findViewById(R.id.imageButtonSeeAllDeposits);

        ImageButton imageButtonWithdraw = findViewById(R.id.imageButtonWithdraw);
       // ImageButton imageButtonSeeClientsWithdraws = findViewById(R.id.imageButtonSeeClientsWithdraws);
        //ImageButton imageButtonSeeAllWithdraws = findViewById(R.id.imageButtonSeeAllWithdraws);

        ImageButton imageButtonMoveMillionaires = findViewById(R.id.imageButtonMoveMillionaires);
        ImageButton imageButtonSeeMillionaires = findViewById(R.id.imageButtonSeeMillionaires);

        Button exitButton = findViewById(R.id.buttonExit);


        imageButtonNewClient.setOnClickListener(v -> {
            System.out.println("New client");
            Intent intent = new Intent(MainActivity.this,NewClient.class);
            intent.putExtra("clientCounter", CounterManager.getNextClientId());
            intent.putExtra("accountCounter", CounterManager.getNextAccountId());

            startActivity(intent);
        });


        imageButtonDeleteClient.setOnClickListener(v -> {
            System.out.println("Delete client");
            Intent intent = new Intent(MainActivity.this,DeleteClient.class);
            startActivity(intent);
        });

        imageButtonSeeAllClients.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this,SeeAllClients.class);
            startActivity(intent);
        });

        imageButtonNewAccount.setOnClickListener(v -> {
            System.out.println("New account");
            Intent intent = new Intent(MainActivity.this,NewAccount.class);
            intent.putExtra("accountCounter", CounterManager.getNextAccountId());

            startActivity(intent);
        });

        imageButtonSeeClientAccounts.setOnClickListener(v -> {
            System.out.println("See Client Accounts");
            Intent intent = new Intent(MainActivity.this,SeeClientAccounts.class);
            startActivity(intent);
        });

        imageButtonDeleteAccount.setOnClickListener(v -> {
            System.out.println("Delete account");
            Intent intent = new Intent(MainActivity.this,DeleteAccount.class);
            startActivity(intent);
        });
        /*
        imageButtonSeeAllAccounts.setOnClickListener(view -> {

        });
        */
        imageButtonSeeAllRequests.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this,SeeAllRequests.class);
            startActivity(intent);
        });

        imageButtonDeposit.setOnClickListener(v -> {
            System.out.println("Deposit");
            Intent intent = new Intent(MainActivity.this,MakeDeposit.class);
            intent.putExtra("depositCounter", CounterManager.getNextDepositId());

            startActivity(intent);
        });
        /*
        imageButtonSeeClientsDeposits.setOnClickListener(v -> {
            System.out.println("See Client Deposits");
        });

        imageButtonSeeAllDeposits.setOnClickListener(v -> {
            System.out.println("See All Deposits");
        });
        */
        imageButtonWithdraw.setOnClickListener(v -> {
            System.out.println("Withdraw");

            Intent intent = new Intent(MainActivity.this,MakeWithdraw.class);
            intent.putExtra("withdrawCounter", CounterManager.getNextWithdrawId());

            startActivity(intent);
        });
        /*
        imageButtonSeeClientsWithdraws.setOnClickListener(v -> {
            System.out.println("See Client Withdraws");
        });

        imageButtonSeeAllWithdraws.setOnClickListener(v -> {
            System.out.println("See All Withdraws");

            System.out.println();
        });

         */

        exitButton.setOnClickListener(v -> {
            finishAffinity();
            System.exit(0);
        });

        imageButtonMoveMillionaires.setOnClickListener(view -> {
            if (bank.moveMillionaires()){
                try {
                    bank.saveData(getApplicationContext());
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                Toast.makeText(MainActivity.this, "Millionaires moved successfully.", Toast.LENGTH_SHORT).show();
            } else{
                Toast.makeText(MainActivity.this, "There are no millionaires to move.", Toast.LENGTH_SHORT).show();
            }
        });

        imageButtonSeeMillionaires.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this,SeeMillionaires.class);
            startActivity(intent);
        });
    }

    private void initializeCounters() {
        CounterManager.loadCounters();
    }


}