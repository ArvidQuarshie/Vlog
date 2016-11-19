package com.amusoft.vlog;

import android.app.Application;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by sophiebot on 11/15/16.
 */

public class VlogApplication extends Application {
    FirebaseDatabase myFirebaseRef;

    @Override
    public void onCreate(){
        super.onCreate();

        if (!FirebaseApp.getApps(this).isEmpty()) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }



    }
}