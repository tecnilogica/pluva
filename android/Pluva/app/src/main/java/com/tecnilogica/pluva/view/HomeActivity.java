package com.tecnilogica.pluva.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tecnilogica.pluva.Preferences;
import com.tecnilogica.pluva.R;
import com.tecnilogica.pluva.Utils;
import com.tecnilogica.pluva.net.ApiCall;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.RetrofitError;

public class HomeActivity extends AppCompatActivity implements ApiCall.ApiCallListener {

    @Bind(R.id.location_text)
    TextView locationText;
    @Bind(R.id.today_text)
    TextView todayText;
    @Bind(R.id.tomorrow_text)
    TextView tomorrowText;
    @Bind(R.id.location_change)
    TextView locationChange;
    @Bind(R.id.changeUserId)
    TextView changeUserId;

    private Preferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        setStyles();

        //TODO initialize global ApiCall

        //Load location and selected day from preferences
        preferences = Preferences.getInstance(getApplicationContext());
        locationText.setText(preferences.getString(Preferences.PREF_LOCATION));

        String selectedDay = preferences.getString(Preferences.PREF_DAY);
        if (Utils.isValidString(selectedDay) && selectedDay.equals(Preferences.PREF_DAY_TOMORROW)) {
            onClickTomorrow(tomorrowText);
        } else {
            onClickToday(todayText);
        }
    }


    //ACTION METHODS
    public void onClickChangeLocation(View v) {
        int horizontalMargin = (int) getResources().getDimension(R.dimen.activity_horizontal_margin);
        int verticalMargin = (int) getResources().getDimension(R.dimen.activity_vertical_margin);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.location));
        final LinearLayout container = new LinearLayout(this);
        container.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        LinearLayout.LayoutParams editParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        editParams.setMargins(horizontalMargin, verticalMargin, horizontalMargin, verticalMargin);
        input.setLayoutParams(editParams);
        container.addView(input);

        builder.setView(container)
                .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (input.getText() != null) {
                            String strLocation = input.getText().toString();
                            if (Utils.isValidString(strLocation)) {
                                preferences.putString(Preferences.PREF_LOCATION, strLocation);
                                locationText.setText(strLocation);

                                //TODO Call to API mehtod
                            }
                        }
                    }
                })
                .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).show();
    }

    public void onClickToday(View v) {
        preferences.putString(Preferences.PREF_DAY, Preferences.PREF_DAY_TODAY);
        setSelectedDay(todayText, tomorrowText);

        //TODO Call to API mehtod
    }

    public void onClickTomorrow(View v) {
        preferences.putString(Preferences.PREF_DAY, Preferences.PREF_DAY_TOMORROW);
        setSelectedDay(tomorrowText, todayText);

        //TODO Call to API mehtod
    }

    public void onClickChangeUserId(View v) {
        int horizontalMargin = (int) getResources().getDimension(R.dimen.activity_horizontal_margin);
        int verticalMargin = (int) getResources().getDimension(R.dimen.activity_vertical_margin);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.changeUserId));
        final LinearLayout container = new LinearLayout(this);
        container.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        container.setOrientation(LinearLayout.VERTICAL);

        final TextView current = new TextView(this);
        current.setText(String.format(" %s: %s", getResources().getString(R.string.currentUserId), preferences.getString(Preferences.PREF_USER_ID)));
        LinearLayout.LayoutParams currentParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        currentParams.setMargins(horizontalMargin, 0, horizontalMargin, 0);
        current.setLayoutParams(currentParams);
        container.addView(current);

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        LinearLayout.LayoutParams editParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        editParams.setMargins(horizontalMargin, verticalMargin, horizontalMargin, verticalMargin);
        input.setLayoutParams(editParams);
        container.addView(input);

        builder.setView(container)
                .setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (input.getText() != null) {
                            String strUserId = input.getText().toString();
                            if (Utils.isValidString(strUserId)) {
                                preferences.putString(Preferences.PREF_USER_ID, strUserId);

                                //TODO Call to API method
                            }
                        }
                    }
                })
                .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).show();
    }

    //AUX METHODS
    private void setSelectedDay(TextView selected, TextView unselected) {
        if (selected != null && unselected != null) {
            selected.setBackground(getDrawable(R.drawable.day_selected));
            selected.setTextColor(getResources().getColor(R.color.background));

            unselected.setBackground(getDrawable(R.drawable.day_unselected));
            unselected.setTextColor(getResources().getColor(R.color.colorPrimary));
        }
    }

    private void setStyles () {
        SpannableString changeLocationContent = new SpannableString(getResources().getString(R.string.change));
        changeLocationContent.setSpan(new UnderlineSpan(), 0, changeLocationContent.length(), 0);
        locationChange.setText(changeLocationContent);

        SpannableString changeUserIdContent = new SpannableString(getResources().getString(R.string.changeUserId));
        changeUserIdContent.setSpan(new UnderlineSpan(), 0, changeUserIdContent.length(), 0);
        changeUserId.setText(changeUserIdContent);
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
