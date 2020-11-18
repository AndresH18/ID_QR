package com.example.id_qr.beforeStart;

import android.app.Application;
import android.content.ContentProvider;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.FirebaseDatabase;

public class FireBaseStart extends Application {
    private static final String TAG = "FireBaseStart";

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        Log.d("HEHEHEHEHEHEHEHEHHEHEHE", "sjdksjdksjdksjdksjdkjskdjskdjksjdskjksjksjdksjdksjdksjdksjdks");
    }
}
