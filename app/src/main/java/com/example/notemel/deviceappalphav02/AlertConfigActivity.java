package com.example.notemel.deviceappalphav02;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.util.HashMap;

import system.AlertConfigHandler;

/**
 * Created by Melchior_S on 2016-10-19.
 */

public class AlertConfigActivity extends AppCompatActivity {

    private String mSoundAlertEnable;
    private String mNotificationAlertEnable;
    private String mTemperatureEnable;
    private String mHumidityEnable;
    private String mOxygenEnable;
    private String mCOEnable;

    private String mTemperature_Level;
    private String mHumidity_Level;
    private String mOxygen_Level;
    private String mCO_Level;

    private String mTooHighString;
    private String mTooLowString;
    private String mWrongString;

    private CheckBox cbx_SoundAlert;
    private CheckBox cbx_NotificationAlert;
    private CheckBox cbx_Temperature;
    private CheckBox cbx_Humidity;
    private CheckBox cbx_Oxygen;
    private CheckBox cbx_CO;

    private EditText edt_TemperatureValue;
    private EditText edt_HumidityValue;
    private EditText edt_OxygenValue;
    private EditText edt_COValue;


    private AlertConfigHandler mAlertConfigHandler = AlertConfigHandler.getInstance();

    private HashMap<String, Boolean> mAlertOptionHash = new HashMap<>();
    private HashMap<String, Float> mAlertDataHash = new HashMap<>();

    private final float mTemperatureMax = 40.0f;
    private final float mTemperatureMin = -10.0f;

    private final float mHumidityMax = 80.0f;
    private final float mHumidityMin = 0.0f;

    private final float mOxygenMax = 21.0f;
    private final float mOxygenMin = -10.0f;

    private final float mCOMax = 150.0f;
    private final float mCOMin = 0.0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        mAlertOptionHash = mAlertConfigHandler.getAlertOptionHash();
        mAlertDataHash = mAlertConfigHandler.getAlertDataHash();


        //init UI
        cbx_SoundAlert = (CheckBox) findViewById(R.id.cbx_sound_alert);
        cbx_NotificationAlert = (CheckBox) findViewById(R.id.cbx_notification_alert);
        cbx_Temperature = (CheckBox) findViewById(R.id.cbx_temperature);
        cbx_Humidity = (CheckBox) findViewById(R.id.cbx_humidity);
        cbx_Oxygen = (CheckBox) findViewById(R.id.cbx_oxygen);
        cbx_CO = (CheckBox) findViewById(R.id.cbx_co);

        edt_TemperatureValue = (EditText) findViewById(R.id.edt_temperature_value);
        edt_HumidityValue = (EditText) findViewById(R.id.edt_humidity_value);
        edt_OxygenValue = (EditText) findViewById(R.id.edt_oxygen_value);
        edt_COValue = (EditText) findViewById(R.id.edt_co_value);

        mSoundAlertEnable = this.getString(R.string.SoundAlertEnable);
        mNotificationAlertEnable = this.getString(R.string.NotificationAlertEnable);
        mTemperatureEnable = this.getString(R.string.TemperatureEnable);
        mHumidityEnable = this.getString(R.string.HumidityEnable);
        mOxygenEnable = this.getString(R.string.OxygenEnable);
        mCOEnable = this.getString(R.string.COEnable);


        mTemperature_Level = this.getString(R.string.Temperature_Level);
        mHumidity_Level = this.getString(R.string.Humidity_Level);
        mOxygen_Level = this.getString(R.string.Oxygen_Level);
        mCO_Level = this.getString(R.string.CO_Level);

        mTooHighString = this.getString(R.string.Input_Error_Too_HIGH_String);
        mTooLowString = this.getString(R.string.Input_Error_Too_LOW_String);
        mWrongString = this.getString(R.string.Input_Error_Wrong_input_String);

        initCheckBox();
        initEditText();
        initCheckBoxEvent();
        initEditTextEvent();
    }

    private void initCheckBox() {
        cbx_SoundAlert.setChecked(mAlertOptionHash.get(mSoundAlertEnable));
        cbx_NotificationAlert.setChecked(mAlertOptionHash.get(mNotificationAlertEnable));

        cbx_Temperature.setChecked(mAlertOptionHash.get(mTemperatureEnable));
        cbx_Humidity.setChecked(mAlertOptionHash.get(mHumidityEnable));
        cbx_Oxygen.setChecked(mAlertOptionHash.get(mOxygenEnable));
        cbx_CO.setChecked(mAlertOptionHash.get(mCOEnable));
    }

    private void initEditText() {
        edt_TemperatureValue.setText(String.valueOf(mAlertDataHash.get(mTemperature_Level)));
        edt_HumidityValue.setText(String.valueOf(mAlertDataHash.get(mHumidity_Level)));
        edt_OxygenValue.setText(String.valueOf(mAlertDataHash.get(mOxygen_Level)));
        edt_COValue.setText(String.valueOf(mAlertDataHash.get(mCO_Level)));
    }

    private void initCheckBoxEvent() {
        cbx_SoundAlert.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mAlertConfigHandler.updateAlertOptionHash(mSoundAlertEnable, isChecked);
            }
        });

        cbx_NotificationAlert.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mAlertConfigHandler.updateAlertOptionHash(mNotificationAlertEnable, isChecked);
            }
        });
        cbx_Temperature.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mAlertConfigHandler.updateAlertOptionHash(mTemperatureEnable, isChecked);
            }
        });
        cbx_Humidity.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mAlertConfigHandler.updateAlertOptionHash(mHumidityEnable, isChecked);
            }
        });
        cbx_Oxygen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mAlertConfigHandler.updateAlertOptionHash(mOxygenEnable, isChecked);
            }
        });
        cbx_CO.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mAlertConfigHandler.updateAlertOptionHash(mCOEnable, isChecked);
            }
        });


    }

    private void initEditTextEvent() {

        edt_TemperatureValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(inputFilter(s.toString(), edt_TemperatureValue, mTemperatureMax, mTemperatureMin))
                {
                    mAlertConfigHandler.updateAlertDataHash(mTemperature_Level,Float.parseFloat(s.toString()));
                }
            }
        });
        edt_HumidityValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(inputFilter(s.toString(), edt_HumidityValue, mHumidityMax, mHumidityMin))
                {
                    mAlertConfigHandler.updateAlertDataHash(mHumidity_Level,Float.parseFloat(s.toString()));
                }
            }
        });


        edt_OxygenValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(inputFilter(s.toString(), edt_OxygenValue, mOxygenMax, mOxygenMin))
                {
                    mAlertConfigHandler.updateAlertDataHash(mOxygen_Level,Float.parseFloat(s.toString()));
                }
            }
        });


        edt_COValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(inputFilter(s.toString(), edt_COValue, mCOMax, mCOMin))
                {
                    mAlertConfigHandler.updateAlertDataHash(mCO_Level,Float.parseFloat(s.toString()));
                }

            }
        });
        ;
    }

    private boolean inputFilter(String inputValueText, EditText editTextObj, float max, float min) {
        boolean isCorrectValue = false;
        try {
            if (!inputValueText.equals("") && !inputValueText.equals(".") && !inputValueText.equals("-")) {
                float inputValue = Float.parseFloat(inputValueText);

                if (inputValue > max) {
                    editTextObj.setText(String.valueOf(max));
                    showAlertTextInputError(mTooHighString);
                } else if (inputValue < min) {
                    editTextObj.setText(String.valueOf(min));
                    showAlertTextInputError(mTooLowString);
                }
                else
                {
                    isCorrectValue = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlertTextInputError(mWrongString);
            edt_TemperatureValue.setText(String.valueOf(""));
        }
        return isCorrectValue;
    }

    public void showAlertTextInputError(String text) {
        AlertDialog.Builder alert = new AlertDialog.Builder(AlertConfigActivity.this);
        alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alert.setMessage(text);
        alert.show();

    }
}
