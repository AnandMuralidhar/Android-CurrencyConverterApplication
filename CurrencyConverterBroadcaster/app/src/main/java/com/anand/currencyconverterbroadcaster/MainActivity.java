package com.anand.currencyconverterbroadcaster;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.content.ComponentName;
import android.content.Intent;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import java.util.List;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private String convertTo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<String> currencies = new ArrayList<String>();
        currencies.add("Indian Rupee");
        currencies.add("Euro");
        currencies.add("British Pound");

        Spinner currencyDropdown = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, currencies);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        currencyDropdown.setAdapter(adapter);
        currencyDropdown.setOnItemSelectedListener(this);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View v, int position, long id)
    {
        convertTo = parent.getItemAtPosition(position).toString();
    }

    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();

        if (intent != null) {
            String convertTo = intent.getStringExtra("convertTo");
            String currencyAmount = intent.getStringExtra("amount");
            String resultAmount = intent.getStringExtra("convertedAmount");

            if (resultAmount != null)
            {
                TextView textView = findViewById(R.id.resultTextView);
                textView.setText("Dollar converted price is :" + resultAmount);
            }
        }
    }

    public void convertCurrency(View view) {
        String senderString = "com.anand.currencyconverterbroadcaster.sender";
        Intent broadcast = new Intent(senderString);
        EditText editText = findViewById(R.id.amountEditText);
        String amount = editText.getText().toString();
        broadcast.addFlags(Intent.FLAG_EXCLUDE_STOPPED_PACKAGES);
        broadcast.setComponent(new ComponentName("com.anand.currencyconverterreceiver", "com.anand.currencyconverterreceiver.CurrencyExchange"));
        broadcast.putExtra("amount", amount);
        broadcast.putExtra("convertTo", convertTo);
        sendBroadcast(broadcast);
    }

    public void closeApplication(View view) {
        MainActivity.this.finish();
    }
}



