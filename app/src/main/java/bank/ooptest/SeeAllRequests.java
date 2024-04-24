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

public class SeeAllRequests extends AppCompatActivity {
    private static Bank bank;
    private static TextView textViewSeeAllRequests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_see_all_requests);

        bank = Bank.getInstance();
        bank.initializeBank(getApplicationContext());

        textViewSeeAllRequests = findViewById(R.id.textViewSeeAllRequests);
        Button buttonSeeAllRequestsCancel = findViewById(R.id.buttonSeeAllRequestsCancel);

        buttonSeeAllRequestsCancel.setOnClickListener(view -> {
            finish();
        });

        seeAllRequests();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    public static void seeAllRequests(){
        StringBuilder requests = new StringBuilder("Requests: \n");
        ListSED<Request> requestList = bank.getRequests();

        for (Request request: requestList) {
            if (request == null){
                continue;
            }
            if (request instanceof Deposit){
                requests.append("\n\n\nDeposit: \n").append("ID: ").append(request.getId()).append(" \nIdentity Key: ").append(request.getIdentityKey()).append("\nAccount id: ").append(request.getIdAccount()).append(" \nAmount: ").append(request.getAmount()).append(" \nName sender: ").append(((Deposit) request).getNameSender()).append("\n");
            } else if (request instanceof Withdraw){
                requests.append("\n\n\nWithdraw: \n").append("ID: ").append(request.getId()).append(" \nIdentity Key: ").append(request.getIdentityKey()).append("\nAccount id: ").append(request.getIdAccount()).append(" \nAmount: ").append(request.getAmount()).append(" \nFirst Name: ").append(((Withdraw) request).getFirstName()).append("\n Last names: ").append(((Withdraw) request).getLastName()).append("\nSex: ").append(((Withdraw) request).getSex());
                if (bank.getClient(request.getIdentityKey()) instanceof ForeignClient){
                    requests.append("\nNationality: ").append(((Withdraw) request).getNationality()).append("\nAddress: ").append(((Withdraw) request).getAddress());
                }
            }
        }

        textViewSeeAllRequests.setText(requests.toString());
    }
}