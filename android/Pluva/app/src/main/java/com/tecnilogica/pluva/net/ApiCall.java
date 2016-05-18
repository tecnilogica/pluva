package com.tecnilogica.pluva.net;

import android.content.Context;

import com.tecnilogica.pluva.Preferences;
import com.tecnilogica.pluva.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.ConversionException;
import retrofit.converter.Converter;
import retrofit.mime.TypedInput;
import retrofit.mime.TypedOutput;

/**
 * Created by Lucia on 11/5/16.
 */
public class ApiCall {

    private final String BASE_URL = "https://pluva.tecnilogica.com";
    private final String ERROR_CODE = "10";

    private Preferences preferences;
    private ApiCallListener listener;

    public interface ApiCallListener {
        void onDataError ();
        void onDataSentOk ();
        void onDataSentError (RetrofitError error);
    }

    public ApiCall (Context context, ApiCallListener listener) {
        super();
        this.listener = listener;
        preferences = Preferences.getInstance(context);
    }

    private RetrofitInterface getRetrofitInterface () {
        return new RestAdapter.Builder()
                .setConverter(new StringConverter())
                .setEndpoint(BASE_URL)
                .build()
                .create(RetrofitInterface.class);
    }

    private boolean isOk (String strResponse) {
        if (Utils.isValidString(strResponse)) {
            String strPattern = "^<forecast>(.*?)</forecast>";
            Pattern pattern = Pattern.compile(strPattern);
            Matcher matcher = pattern.matcher(strResponse);
            if (matcher.find()) {
                String code = matcher.group(1);
                return (Utils.isValidString(code) && !code.equals(ERROR_CODE));
            }
        }
        return false;
    }


    public void sendValues () {
        String userId = preferences.getString(Preferences.PREF_USER_ID);
        String location = preferences.getString(Preferences.PREF_LOCATION);
        boolean today = Preferences.PREF_DAY_TODAY.equals(preferences.getString(Preferences.PREF_DAY));

        RetrofitInterface retrofitInterface = getRetrofitInterface();
        Callback<String> callback = new Callback<String>() {
            @Override
            public void success(String s, Response response) {
                if (listener != null) {
                    if (isOk(s)) {
                        listener.onDataSentOk();
                    } else {
                        listener.onDataError();
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if (listener != null) {
                    listener.onDataSentError(error);
                }
            }
        };

        if (today) {
            retrofitInterface.sendValuesForToday(location, userId, callback);
        } else {
            retrofitInterface.sendValuesForTomorrow(location, userId, true, callback);
        }
    }


    static class StringConverter implements Converter {

        @Override
        public Object fromBody(TypedInput typedInput, Type type) throws ConversionException {
            String text = null;
            try {
                text = fromStream(typedInput.in());
            } catch (IOException ignored) {/*NOP*/ }

            return text;
        }

        @Override
        public TypedOutput toBody(Object o) {
            return null;
        }

        public static String fromStream(InputStream in) throws IOException {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder out = new StringBuilder();
            String newLine = System.getProperty("line.separator");
            String line;
            while ((line = reader.readLine()) != null) {
                out.append(line);
                out.append(newLine);
            }
            return out.toString();
        }
    }
}
