package bank.ooptest;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class CounterManager {
    private static int clientCounter = 0;
    private static int accountCounter = 0;
    private static int depositCounter = 0;
    private static int withdrawCounter = 0;

    private static final String COUNTER_FILE_PATH = "counter.json";

    static {
        loadCounters();
    }

    public static int getNextClientId() {
        return ++clientCounter;
    }

    public static int getNextAccountId() {
        return ++accountCounter;
    }

    public static int getNextDepositId() {
        return ++depositCounter;
    }
    public static int getNextWithdrawId() {
        return ++withdrawCounter;
    }

    static void loadCounters() {
        try (FileReader reader = new FileReader(COUNTER_FILE_PATH)) {
            StringBuilder stringBuilder = new StringBuilder();
            int character;
            while ((character = reader.read()) != -1) {
                stringBuilder.append((char) character);
            }
            String jsonString = stringBuilder.toString();
            JSONObject jsonObject = new JSONObject(new JSONTokener(jsonString));
            clientCounter = jsonObject.optInt("clientCounter", 0);
            accountCounter = jsonObject.optInt("accountCounter", 0);
            depositCounter = jsonObject.optInt("depositCounter", 0);
            withdrawCounter = jsonObject.optInt("withdrawCounter", 0);
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    public static void saveCounters(Context context) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("clientCounter", clientCounter);
        jsonObject.put("accountCounter", accountCounter);
        jsonObject.put("depositCounter", depositCounter);
        jsonObject.put("withdrawCounter", withdrawCounter);


        try (FileOutputStream fos = context.openFileOutput(COUNTER_FILE_PATH, Context.MODE_PRIVATE)) {
            fos.write(jsonObject.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
