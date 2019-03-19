package com.anand.currencyconverterbroadcaster;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ConvertedCurrencyValue extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String convertedAmount = intent.getStringExtra("convertedAmount");
        Intent mainIntent = new Intent(context, MainActivity.class);
        mainIntent.putExtra("convertedAmount", convertedAmount);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(mainIntent);
    }
}