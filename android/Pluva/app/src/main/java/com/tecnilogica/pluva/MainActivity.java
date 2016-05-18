package com.tecnilogica.pluva;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.tecnilogica.pluva.view.ConfigurationActivity;
import com.tecnilogica.pluva.view.HomeActivity;

/**
 * Created by Lucia on 11/5/16.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*
        Class nextClass;
        if (Preferences.getInstance(getApplicationContext()).isConfigurated()) {
            nextClass = HomeActivity.class;
        } else { //First use, needs to configurate userId and location
            nextClass = ConfigurationActivity.class;
        }
        */

        //TODO check if configurated in preferences for next class

        startActivity(new Intent(getApplicationContext(), ConfigurationActivity.class));
        finish();
    }
}
