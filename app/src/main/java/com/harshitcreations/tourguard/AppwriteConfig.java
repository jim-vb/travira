package com.harshitcreations.tourguard;

import android.content.Context;

import io.appwrite.Client;

public class AppwriteConfig {
    public static Client getClient(Context context) {
        return new Client(context, "https://nyc.cloud.appwrite.io/v1");

    }
}

