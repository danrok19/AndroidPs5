package com.example.sensorapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

public class SensorDetailsActivity extends AppCompatActivity implements SensorEventListener {
    public static final String KEY_EXTRA_SENSOR = "KEY_EXTRA_SENSOR";
    private SensorManager sensorManager;
    private Sensor sensorLight;
    private TextView sensorLightTextView;
    private TextView sensorLabelTextView;
    private TextView sensorDetailsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_details);
        int id = Integer.parseInt(String.valueOf(getIntent().getExtras().get(KEY_EXTRA_SENSOR)));

        sensorLightTextView = findViewById(R.id.sensor_Light_label);
        sensorLabelTextView = findViewById(R.id.sensor_label);
        sensorDetailsTextView = findViewById(R.id.sensor_details_label);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorLight = sensorManager.getSensorList(Sensor.TYPE_ALL).get(id);

        if (sensorLight == null){
            sensorLightTextView.setText(R.string.missing_sensor);
        }
        else {
            sensorLightTextView.setText(sensorLight.getName());
        }
    }

    @Override
    protected void onStart(){
        super.onStart();
        if(sensorLight != null){
            sensorManager.registerListener(this,sensorLight, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onStop(){
        super.onStop();
        sensorManager.unregisterListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        int sensorType = sensorEvent.sensor.getType();
        float currentValue = sensorEvent.values[0];

        sensorDetailsTextView.setText(String.valueOf(currentValue));

        switch (sensorType){
            case Sensor.TYPE_PRESSURE:
                sensorLabelTextView.setText(R.string.pressure_sensor_label);
                break;
            case Sensor.TYPE_AMBIENT_TEMPERATURE:
                sensorLabelTextView.setText(R.string.temperature_sensor_label);
                break;
            default:
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}