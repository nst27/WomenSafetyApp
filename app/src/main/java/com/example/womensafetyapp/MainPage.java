package com.example.womensafetyapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.protobuf.DescriptorProtos;


public class MainPage extends AppCompatActivity implements SensorEventListener{
    TextView username,phoneno,sendme;
    private  SensorManager sensorManager;
    private  Sensor accelerometerSensor;
    private boolean isAccelerometerSensorAvailable,isNotFirstTime=false;
    private float currentX,currentY,currentZ,lastX,lastY,lastZ;
    private float xDiff,yDiff,zDiff;
    private float shakeThreshold=5f;
    private Vibrator vibrator;
    private int count;
    private FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        username = findViewById(R.id.musername);
        phoneno = findViewById(R.id.phoneno);
        sendme = findViewById(R.id.sendwala);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        if(sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)!=null){
            accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            isAccelerometerSensorAvailable=true;
        }else{
            username.setText("Accelerometer sensor not available");
            isAccelerometerSensorAvailable=false;
        }
        //showData();
    }




    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        Intent intent = getIntent();
        String user_name = intent.getStringExtra("fullName");
        final String phone_no = intent.getStringExtra("emergencyphone");

        currentX = sensorEvent.values[0];
        currentY = sensorEvent.values[1];
        currentZ = sensorEvent.values[2];

        if(isNotFirstTime) {
            xDiff = Math.abs(lastX - currentX);
            yDiff = Math.abs(lastY - currentY);
            zDiff = Math.abs(lastZ - currentZ);

            if ((xDiff > shakeThreshold && yDiff > shakeThreshold) || (yDiff > shakeThreshold && zDiff > shakeThreshold) || (xDiff > shakeThreshold && zDiff > shakeThreshold)) {

                if(getApplicationContext().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                    //getlocation
                    fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if(location !=null){
                                Double lat = location.getLatitude();
                                Double lon = location.getLongitude();

                                username.setText(""+lat);
                                phoneno.setText(""+lon);
                                sendme.setText(""+phone_no);


                                if(getApplicationContext().checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED){
                                    try{
                                        SmsManager smsManager = SmsManager.getDefault();
                                        smsManager.sendTextMessage("+91"+phone_no,null,"Nishant here",null,null);
                                        Toast.makeText(getApplicationContext(),"Pass",Toast.LENGTH_SHORT).show();

                                    }catch (Exception e){
                                        Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_SHORT).show();
                                    }

                                }
                                else{
                                    requestPermissions(new String[]{Manifest.permission.SEND_SMS},1);
                                }

                            }
                        }
                    });
                }else{
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
                }
            }


            }else{
            }
        lastX=currentX;
        lastY=currentY;
        lastZ=currentZ;
        isNotFirstTime=true;
        }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    @Override
    protected void onResume(){
        super.onResume();
        if(isAccelerometerSensorAvailable){
            sensorManager.registerListener(this,accelerometerSensor,SensorManager.SENSOR_DELAY_NORMAL);
        }
    }
    @Override
    protected void onPause(){
        super.onPause();
        if(isAccelerometerSensorAvailable){
            sensorManager.unregisterListener(this);
        }
    }
}