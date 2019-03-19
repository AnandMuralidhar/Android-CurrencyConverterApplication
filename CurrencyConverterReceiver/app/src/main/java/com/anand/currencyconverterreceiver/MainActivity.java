package com.anand.currencyconverterreceiver;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.content.ComponentName;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class MainActivity extends AppCompatActivity {

    public static double usdInr = 0;
    public static double usdEuro = 0;
    public static double usdPound = 0;

    private String convertedValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        if (null != intent) {
            renderUI(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        if (null != intent) {
            renderUI(intent);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Intent intent = getIntent();
        if (null != intent) {
            renderUI(intent);
        }
    }

    public void renderUI(Intent intent) {
        String convertTo = intent.getStringExtra("convertTo");
        String amountStr = intent.getStringExtra("amount");

        TextView textView = findViewById(R.id.textView2);
        textView.setText("Amount in USD : $" + amountStr);

        TextView textView2 = findViewById(R.id.textView);
        textView2.setText("Convert To : " + convertTo);

        convert(convertTo, amountStr);
    }

    public void convert(String convertTo, String amountStr) {
        if (convertTo != null && amountStr != null) {
            BigDecimal amount = new BigDecimal(amountStr);
            BigDecimal convertedAmount = null;
            new GenerateRates().execute();
            switch (convertTo) {
                case "Indian Rupee":
                    convertedAmount = amount.multiply(new BigDecimal(usdInr));
                    break;
                case "Euro":
                    convertedAmount = amount.multiply(new BigDecimal(usdEuro));
                    break;
                case "British Pound":
                    convertedAmount = amount.multiply(new BigDecimal(usdPound));
                    break;

                default:
                    break;
            }
            convertedAmount = convertedAmount.setScale(4, BigDecimal.ROUND_DOWN);
            convertedValue = convertedAmount.toString();
        }
    }

    public void currencyConvert(View view) {
        String senderString = "com.anand.currencyconverterreceiver.sender";
        Intent broadcastIntent = new Intent(senderString);
        broadcastIntent.addFlags(Intent.FLAG_EXCLUDE_STOPPED_PACKAGES);
        broadcastIntent.setComponent(new ComponentName("com.anand.currencyconverterbroadcaster", "com.anand.currencyconverterbroadcaster.ConvertedCurrencyValue"));
        broadcastIntent.putExtra("convertedAmount", convertedValue);
        MainActivity.this.finish();
        sendBroadcast(broadcastIntent);
    }

    public class GenerateRates extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            String response = "";
            try {
                URL url = new URL("https://api.exchangeratesapi.io/latest?base=USD");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                BufferedReader read = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line = read.readLine();
                while(line!=null) {
                    response += line;
                    line = read.readLine();
                }

                JSONObject obj = new JSONObject(response.toString());
                MainActivity.usdInr = Double.parseDouble(obj.getJSONObject("rates").getString("INR"));
                MainActivity.usdEuro  = Double.parseDouble(obj.getJSONObject("rates").getString("EUR"));
                MainActivity.usdPound  = Double.parseDouble(obj.getJSONObject("rates").getString("GBP"));

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void a) {
            super.onPostExecute(a);
        }
    }


    public void closeApp(View view)
    {
        MainActivity.this.finish();
    }
}
