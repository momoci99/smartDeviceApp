package com.example.notemel.deviceappalphav02;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CheckBox;
import android.widget.EditText;

/**
 * Created by Melchior_S on 2016-10-19.
 */

public class AlertConfigActivity extends AppCompatActivity {

    CheckBox cbx_SoundAlert;
    CheckBox cbx_NotificationAlert;
    CheckBox cbx_Temperature;
    CheckBox cbx_Humidity;
    CheckBox cbx_Oxygen;
    CheckBox cbx_CO;

    EditText edt_TemperatureValue;
    EditText edt_HumidityValue;
    EditText edt_OxygenValue;
    EditText edt_COValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

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




    }
}
