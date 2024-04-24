package bank.ooptest;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class JsonUtils {

    public static ListSED<Client> readClientsFromFile(Context context, String filename) {
        ListSED<Client> clients = new ListSED<>();

        try {
            FileInputStream fis = context.openFileInput(filename);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append('\n');
            }
            fis.close();

            JSONObject jsonObject = new JSONObject(stringBuilder.toString());
            JSONArray jsonArray = jsonObject.getJSONArray("clients");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject clientObject = jsonArray.getJSONObject(i);
                if (clientObject.has("nationality")){
                    int id = clientObject.getInt("id");
                    String identityKey = clientObject.getString("identityKey");
                    String firstName = clientObject.getString("firstName");
                    String lastNames = clientObject.getString("lastNames");
                    char sex = clientObject.getString("sex").charAt(0);
                    int age = clientObject.getInt("age");
                    JSONArray accountIdsArray = clientObject.getJSONArray("accountIds");
                    ListSED<Integer> accountListIDs = new ListSED<>();
                    for (int j = 0; j < accountIdsArray.length(); j++) {
                        accountListIDs.add(accountIdsArray.getInt(j));
                    }

                    JSONArray millionairesAccountsIDS = clientObject.getJSONArray("millionairesAccountsIDS");
                    ListSED<Integer> millionaireAccountListIDs = new ListSED<>();
                    for (int j = 0; j < millionairesAccountsIDS.length(); j++) {
                        millionaireAccountListIDs.add(millionairesAccountsIDS.getInt(j));
                    }

                    String nationality = clientObject.getString("nationality");
                    String address = clientObject.getString("address");

                    clients.add(new ForeignClient(id, identityKey, firstName, lastNames, sex, age, accountListIDs, millionaireAccountListIDs, nationality, address));
                } else {
                    int id = clientObject.getInt("id");
                    String identityKey = clientObject.getString("identityKey");
                    String firstName = clientObject.getString("firstName");
                    String lastNames = clientObject.getString("lastNames");
                    char sex = clientObject.getString("sex").charAt(0);
                    int age = clientObject.getInt("age");
                    JSONArray accountIdsArray = clientObject.getJSONArray("accountIds");
                    ListSED<Integer> accountListIDs = new ListSED<>();
                    for (int j = 0; j < accountIdsArray.length(); j++) {
                        accountListIDs.add(accountIdsArray.getInt(j));
                    }

                    JSONArray millionairesAccountsIDS = clientObject.getJSONArray("millionairesAccountsIDS");
                    ListSED<Integer> millionaireAccountListIDs = new ListSED<>();
                    for (int j = 0; j < millionairesAccountsIDS.length(); j++) {
                        millionaireAccountListIDs.add(millionairesAccountsIDS.getInt(j));
                    }

                    clients.add(new LocalClient(id, identityKey, firstName, lastNames, sex, age, accountListIDs, millionaireAccountListIDs));
                }
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return clients;
    }

    public static ListSED<Request> readRequestsFromFile(Context context, String filename) {
        ListSED<Request> requests = new ListSED<>();

        try {
            FileInputStream fis = context.openFileInput(filename);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append('\n');
            }
            fis.close();

            JSONObject jsonObject = new JSONObject(stringBuilder.toString());
            JSONArray jsonArray = jsonObject.getJSONArray("requests");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject request = jsonArray.getJSONObject(i);

                if (request.has("firstName")){//deposit
                    int id = request.getInt("id");
                    String identityKey = request.getString("identityKey");
                    int idAccount = request.getInt("idAccount");
                    double amount = request.getDouble("amount");
                    String firstName = request.getString("firstName");
                    String lastName = request.getString("lastName");
                    String a = request.getString("sex");
                    char sex = a.charAt(0);



                    if (request.has("nationality")) {
                        String nationality = request.getString("nationality");
                        String address = request.getString("address");
                        requests.add(new Withdraw(id,identityKey,idAccount,amount,firstName,lastName,sex, nationality, address));
                        continue;
                    }
                    requests.add(new Withdraw(id,identityKey,idAccount,amount,firstName,lastName,sex));

                } else { //deposit
                    int id = request.getInt("id");
                    String identityKey = request.getString("identityKey");
                    int idAccount = request.getInt("idAccount");
                    double amount = request.getDouble("amount");
                    String nameSender = request.getString("nameSender");

                    requests.add(new Deposit(id, identityKey, idAccount, amount, nameSender));
                }
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return requests;
    }

    public static HashMap<Integer, Account> readAccountsFromFile(Context context, String filename) {
        HashMap<Integer, Account> accounts = new HashMap<>();

        try {
            FileInputStream fis = context.openFileInput(filename);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line).append('\n');
            }
            fis.close();

            JSONObject jsonObject = new JSONObject(stringBuilder.toString());
            JSONObject accountsObject = jsonObject.getJSONObject("accounts");
            for (Iterator<String> it = accountsObject.keys(); it.hasNext(); ) {
                String accountIdStr = it.next();
                int accountId = Integer.parseInt(accountIdStr);
                JSONObject accountObject = accountsObject.getJSONObject(accountIdStr);
                String identityKey = accountObject.getString("identityKey");
                double balance = accountObject.getDouble("balance");
                String currency = accountObject.getString("currency");
                accounts.put(accountId, new Account(accountId, identityKey, balance, currency));
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return accounts;
    }

    public static void writeClientsToFile(Context context, String filename, ListSED<Client> clients) throws JSONException {
        JSONObject jsonObject = new JSONObject();

        JSONArray jsonArray = new JSONArray();
        for (Client client : clients) {
            if (client instanceof LocalClient){
                JSONObject clientObject = new JSONObject();
                clientObject.put("id", client.getId());
                clientObject.put("identityKey", client.getIdentityKey());
                clientObject.put("firstName", client.getFirstName());
                clientObject.put("lastNames", client.getLastNames());
                clientObject.put("sex", String.valueOf(client.getSex()));
                clientObject.put("age", client.getAge());
                JSONArray accountIdsArray = new JSONArray();
                for (Integer accountId : client.getAccountList()) {
                    accountIdsArray.put(accountId);
                }
                clientObject.put("accountIds", accountIdsArray);

                JSONArray millionairesAccountsIDS = new JSONArray();
                for (Integer accountId : client.getMillionairesAccountsIDS()) {
                    millionairesAccountsIDS.put(accountId);
                }
                clientObject.put("millionairesAccountsIDS", millionairesAccountsIDS);


                jsonArray.put(clientObject);
            } else if (client instanceof ForeignClient) {
                JSONObject clientObject = new JSONObject();
                clientObject.put("id", client.getId());
                clientObject.put("identityKey", client.getIdentityKey());
                clientObject.put("firstName", client.getFirstName());
                clientObject.put("lastNames", client.getLastNames());
                clientObject.put("sex", String.valueOf(client.getSex()));
                clientObject.put("age", client.getAge());
                JSONArray accountIdsArray = new JSONArray();
                for (Integer accountId : client.getAccountList()) {
                    accountIdsArray.put(accountId);
                }
                clientObject.put("accountIds", accountIdsArray);

                JSONArray millionairesAccountsIDS = new JSONArray();
                for (Integer accountId : client.getMillionairesAccountsIDS()) {
                    millionairesAccountsIDS.put(accountId);
                }
                clientObject.put("millionairesAccountsIDS", millionairesAccountsIDS);

                clientObject.put("nationality", ((ForeignClient) client).getNationality());
                clientObject.put("address", ((ForeignClient) client).getAddress());


                jsonArray.put(clientObject);
            }


        }
        jsonObject.put("clients", jsonArray);

        writeJsonToFile(context, filename, jsonObject);
    }

    public static void writeRequestsToFile(Context context, String filename, Bank bank, ListSED<Request> requests) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        int options = 0;

        for (Request request : requests) {
            if (request instanceof Deposit){
                JSONObject requestObject = new JSONObject();
                requestObject.put("id", request.getId());
                requestObject.put("identityKey", request.getIdentityKey());
                requestObject.put("idAccount", request.getIdAccount());
                requestObject.put("amount", request.getAmount());
                requestObject.put("nameSender", ((Deposit) request).getNameSender());
                jsonArray.put(requestObject);

            } else if (request instanceof Withdraw) {
                Client client =  bank.getClient(request.getIdentityKey());

                JSONObject requestObject = new JSONObject();
                requestObject.put("id", request.getId());
                requestObject.put("identityKey", request.getIdentityKey());
                requestObject.put("idAccount", request.getIdAccount());
                requestObject.put("amount", request.getAmount());
                requestObject.put("firstName", ((Withdraw) request).getFirstName());
                requestObject.put("lastName",((Withdraw) request).getLastName() );
                requestObject.put("sex",((Withdraw) request).getSex());

                if (client instanceof ForeignClient) {
                    requestObject.put("nationality", ((Withdraw) request).getNationality());
                    requestObject.put("address", ((Withdraw) request).getAddress());
                }

                jsonArray.put(requestObject);
            }

        }
        jsonObject.put("requests", jsonArray);

        writeJsonToFile(context, filename, jsonObject);
    }

    public static void writeAccountsToFile(Context context, String filename, HashMap<Integer, Account> accounts) throws JSONException {
        JSONObject jsonObject = new JSONObject();

        JSONObject accountsObject = new JSONObject();
        for (Map.Entry<Integer, Account> entry : accounts.entrySet()) {
            int accountId = entry.getKey();
            Account account = entry.getValue();
            JSONObject accountObject = new JSONObject();
            accountObject.put("id", account.getId());
            accountObject.put("identityKey", account.getIdentityKey());
            accountObject.put("balance", account.getBalance());
            accountObject.put("currency", account.getCurrency());
            accountsObject.put(String.valueOf(accountId), accountObject);
        }
        jsonObject.put("accounts", accountsObject);

        writeJsonToFile(context, filename, jsonObject);
    }

    private static void writeJsonToFile(Context context, String filename, JSONObject jsonObject) {
        try {
            FileOutputStream fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
            fos.write(jsonObject.toString().getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}