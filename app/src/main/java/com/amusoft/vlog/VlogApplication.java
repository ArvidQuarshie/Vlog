package com.amusoft.vlog;

import android.app.Application;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by sophiebot on 11/15/16.
 */


/**
 * Creating a global application state where we instantiate elements that
 * require to be globally declared within the application.
 */


public class VlogApplication extends Application {
    FirebaseDatabase myFirebaseRef;

    @Override
    public void onCreate() {
        super.onCreate();

        if (!FirebaseApp.getApps(this).isEmpty()) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }


    }
}