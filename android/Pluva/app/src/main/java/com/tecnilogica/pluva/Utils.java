package com.tecnilogica.pluva;

import android.widget.EditText;

/**
 * Created by Lucia on 11/5/16.
 */
public class Utils {

    public static boolean isValidString (String string) {
        return (string != null && !string.equals(""));
    }

    public static String getEditTextContent (EditText editText) {
        if (editText != null && editText.getText() != null) {
            String strContent = editText.getText().toString();
            if (Utils.isValidString(strContent)) {
                return strContent;
            }
        }
        return null;
    }
}
