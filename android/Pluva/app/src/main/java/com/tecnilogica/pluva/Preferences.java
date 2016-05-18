package com.tecnilogica.pluva;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Lucia on 11/5/16.
 */
public class Preferences {

    private static Preferences instance;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    private final String preferencesName = "UserPreferences";

    public static String PREF_USER_ID = "PREF_USER_ID";
    public static String PREF_LOCATION = "PREF_LOCATION";
    public static String PREF_DAY = "PREF_DAY";

    public static final String PREF_DAY_TODAY = "PREF_DAY_TODAY";
    public static final String PREF_DAY_TOMORROW = "PREF_DAY_TOMORROW";


    private Preferences (Context context) {
        super();
        preferences = context.getSharedPreferences(preferencesName, 0);
        editor = preferences.edit();
    }

    public static Preferences getInstance (Context context) {
        if (instance == null) {
            instance = new Preferences(context);
        }
        return instance;
    }

    //TODO isConfigurated method (location and user id)

    //TODO get and put string methods


}
