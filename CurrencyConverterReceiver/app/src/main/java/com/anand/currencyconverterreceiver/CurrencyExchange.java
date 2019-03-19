package com.anand.currencyconverterreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class CurrencyExchange extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String convertTo = intent.getStringExtra("convertTo");
        String amountStr = intent.getStringExtra("amount");
        Intent activityIntent = new Intent(context, MainActivity.class);

        activityIntent.putExtra("amount", amountStr);
        activityIntent.putExtra("convertTo", convertTo);
        activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(activityIntent);
    }
}