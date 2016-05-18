package com.tecnilogica.pluva.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.tecnilogica.pluva.Preferences;
import com.tecnilogica.pluva.R;
import com.tecnilogica.pluva.Utils;
import com.tecnilogica.pluva.net.ApiCall;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.RetrofitError;

/**
 * Created by Lucia on 11/5/16.
 */
public class ConfigurationActivity extends AppCompatActivity implements ApiCall.ApiCallListener {

    @Bind(R.id.config_userId)
    EditText configUserId;
    @Bind(R.id.config_location)
    EditText configLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);
        ButterKnife.bind(this);
    }


    public void onClickNext(View v) {
        String strUserId = Utils.getEditTextContent(configUserId);
        String strLocation = Utils.getEditTextContent(configLocation);

        if (Utils.isValidString(strUserId) && Utils.isValidString(strLocation)) {
            Preferences preferences = Preferences.getInstance(getApplicationContext());
            preferences.putString(Preferences.PREF_USER_ID, strUserId);
            preferences.putString(Preferences.PREF_LOCATION, strLocation);

            ApiCall apiCall = new ApiCall(getApplicationContext(), this);
            apiCall.sendValues();
        } else {
            String message;
            if (!Utils.isValidString(strUserId)) {
                message = getResources().getString(R.string.error_user);
            } else {
                message = getResources().getString(R.string.error_location);
            }

            new AlertDialog.Builder(this)
                    .setTitle(getResources().getString(R.string.error))
                    .setMessage(message)
                    .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
        }
    }


    @Override
    public void onDataError() {
        new AlertDialog.Builder(this)
                .setMessage(getResources().getString(R.string.error_location_api))
                .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }

    @Override
    public void onDataSentOk() {
        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
        finish();
    }

    @Override
    public void onDataSentError(RetrofitError error) {
        new AlertDialog.Builder(this)
                .setMessage(getResources().getString(R.string.data_error))
                .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }
}
