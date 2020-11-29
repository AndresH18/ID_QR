package com.example.id_qr.beforeStart;

import android.app.Application;
import android.util.Log;

import com.google.firebase.database.FirebaseDatabase;

public class FireBaseStart extends Application {
    private static final String TAG_Firebase = "FireBaseStart";

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        Log.d(TAG_Firebase, "Firebase.getInstancce.setPersistance == true");
    }
}
