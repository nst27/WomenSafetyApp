package com.example.womensafetyapp;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.os.Vibrator;
import android.widget.Toast;

import javax.annotation.Nullable;

public class service extends Service implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor accelerometerSensor;
    private boolean isAccelerometerSensorAvailable,isNotFirstTime=false;
    private float currentX,currentY,currentZ,lastX,lastY,lastZ;
    private float xDiff,yDiff,zDiff;
    private float shakeThreshold=5f;
    private Vibrator vibrator;
    private int count;

    @Nullable
    @Override
    public IBinder onBind(Intent intent){
        return  null;
    }

    public int onStartCommand(Intent intent,int flags,int startId){
        return super.onStartCommand(intent,flags,startId);
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if(sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)!=null){
            accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            isAccelerometerSensorAvailable=true;
        }else{
            isAccelerometerSensorAvailable=false;
        }

        currentX = sensorEvent.values[0];
        currentY = sensorEvent.values[1];
        currentZ = sensorEvent.values[2];

        if(isNotFirstTime) {
            xDiff = Math.abs(lastX - currentX);
            yDiff = Math.abs(lastY - currentY);
            zDiff = Math.abs(lastZ - currentZ);

            if ((xDiff > shakeThreshold && yDiff > shakeThreshold) || (yDiff > shakeThreshold && zDiff > shakeThreshold) || (xDiff > shakeThreshold && zDiff > shakeThreshold)) {
                count=count+1;
                Toast.makeText(this, "shake detected" + count, Toast.LENGTH_SHORT).show();
            }else{
            }
        }



        lastX=currentX;
        lastY=currentY;
        lastZ=currentZ;
        isNotFirstTime=true;

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }





}