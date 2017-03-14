package com.example.vesaf.vesafrijling_pset5poging3;

import android.app.*;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Dispatcher extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Class<?> activityClass;

        try {
            SharedPreferences prefs = getSharedPreferences("X", MODE_PRIVATE);
            activityClass = Class.forName(
                    prefs.getString("lastActivity", ListActivity.class.getName()));
        } catch(ClassNotFoundException ex) {
            activityClass = ListActivity.class;
        }

        startActivity(new Intent(this, activityClass));
        finish();
    }
}
