package ir.ac.ut.ece.spinner;

import android.content.Context;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
//import android.util.Log;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
//import android.widget.TextView;

import java.util.Objects;

public class GyroscopeActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor sensor;
    private static final double ballWeight = 0.01;
//    private double samplingRate = 0.04;
    private float xPos, xAccel, xVel = 0.0f;
    private float yPos, yAccel, yVel = 0.0f;
    private float xMax, yMax;
    private float xW, yW;
    private static final float uS = 0.15f, uK = 0.10f;
    private static final float NS2S = 1.0f / 1000000000.0f;
    private float timestamp;
    private float xTheta, yTheta;
    private ImageView ballImage;
//    private TextView tv;



    @Override
    protected void onStart() {
        super.onStart();
        if(sensor != null){
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME);
        } else {
            System.out.println("--SENSOR IS NULL");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getSupportActionBar()).hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        if(sensor == null){
            System.out.println("NULL SENSOR");
        }
        setContentView(R.layout.activity_gyroscope);
        ballImage = findViewById(R.id.ballImage);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        ballImage.setX(size.x / 2f);
        ballImage.setY(size.y / 2f);
        xMax = (float) size.x - 250;
        yMax = (float) size.y - 250;
        Log.d("MAX", xMax + " " + yMax);
        xTheta = 0;
        yTheta = 0;
    }

//    @Override
//    protected void onResume()
//    {
//        super.onResume();
        /*register the sensor listener to listen to the gyroscope sensor, use the
        callbacks defined in this class, and gather the sensor information as quick
        as possible*/
//        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
//        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME);
//    }

    //When this Activity isn't visible anymore
    @Override
    protected void onStop()
    {
        sensorManager.unregisterListener(this);
        super.onStop();
    }

    @Override
    public void onAccuracyChanged(Sensor arg0, int arg1)
    {
        //Do nothing.
    }

    @Override
    public void onSensorChanged(SensorEvent event)
    {
        if (event.accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE)
            return;
        yW = -event.values[0];
        xW = -event.values[1];

//        if(Math.abs(xW) > 0.004 && Math.abs(yW) > 0.004){
            final float dT = (event.timestamp - timestamp) * NS2S;
            updateBall(dT);
            timestamp = event.timestamp;
//        }
        ballImage.setX(xPos);
        ballImage.setY(yPos);
    }

    private void resetValues() {
        xPos = 0.0f;
        xAccel = 0.0f;
        xVel = 0.0f;
        yPos = 0.0f;
        yAccel = 0.0f;
        yVel = 0.0f;
        ballImage.setX(xPos);
        ballImage.setY(yPos);
        xTheta = 0;
        yTheta = 0;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.resetValues();
        return super.onTouchEvent(event);
    }

    private void updateBall(float dT) {
        if(timestamp == 0) {
            System.out.println("!!!!!!!!!!!!!!!!!TIMESTAMP ZERO!!!!!!!!!!!!!!!!!!");
            return;
//            return;
//            timestamp =
        }

//        float frameTime = 0.25f;
        float gravity = SensorManager.STANDARD_GRAVITY;
        float xDeltaTheta = xW * dT;
        float yDeltaTheta = yW * dT;

//        if(Math.abs(xW) > 0.004 && Math.abs(yW) > 0.004){
            xTheta += xDeltaTheta;
            yTheta += yDeltaTheta;
//        }

        xAccel = gravity * (float) Math.sin(xTheta);
        yAccel = gravity * (float) Math.sin(yTheta);
//        System.out.println(
//                "- XTHETA: " + Math.toDegrees(xTheta) + "    - YTHETA: " + Math.toDegrees(yTheta)
//        );
        System.out.println("----------------------------");
        float xN = (float) (ballWeight * gravity * Math.cos(xTheta));
        float yN = (float) (ballWeight * gravity * Math.cos(yTheta));
        Log.d("FRICTION", "----XN: " + xN + " YN: " + yN);
        Log.d("ACCEL", "---x Accel: " + xAccel + " y Accel: " + yAccel);
        Log.d("ANGLE", "----x ANGLE: " + Math.toDegrees(xTheta) +
                                    " y ANGLE: " + Math.toDegrees(yTheta));
//        Log.d("TS", "--- Timestamp: " + dT);
//        xVel += (xAccel * frameTime);
//        yVel += (yAccel * frameTime);

//        float xS = (float) (xAccel * Math.pow(dT, 2) / 2 + xVel * dT);
//        float yS = (float) (yAccel * Math.pow(dT, 2) / 2 + yVel * dT);
//
//        xPos -= xS;
//        if (xPos >= xMax) {
//            xPos = xMax;
//            xVel = 0;
//        } else if (xPos <= 0) {
//            xPos = 0;
//            xVel = 0;
//        }
//
//        yPos -= yS;
//        if (yPos >= yMax) {
//            yPos = yMax;
//            yVel = 0;
//        } else if (yPos <= 0) {
//            yPos = 0;
//            yVel = 0;
//        }

        if(xVel == 0) {
            if(Math.abs(ballWeight * xAccel) >= Math.abs(xN * uS)) {
                xVel += (xAccel * dT);
                float xS = (float) (xAccel * Math.pow(dT, 2) / 2 + xVel * dT);
                xPos -= xS;
            }
        } else {
            if(xVel > 0)
                xAccel -= ballWeight / (uK * xN);
            else
                xAccel += ballWeight / (uK * xN);
            xVel += (xAccel * dT);
            float xS = (float) (xAccel * Math.pow(dT, 2) / 2 + xVel * dT);
            xPos -= xS;
        }

        if (xPos >= xMax) {
            xPos = xMax;
            xVel = 0;
        } else if (xPos <= 0) {
            xPos = 0;
            xVel = 0;
        }

        if(yVel == 0) {
            if(Math.abs(ballWeight * yAccel) >= Math.abs(yN * uS)) {
                yVel += (yAccel * dT);
                float yS = (float) (yAccel * Math.pow(dT, 2) / 2 + yVel * dT);
                yPos -= yS;
            }
        } else {
            if(yVel > 0)
                yAccel -= ballWeight / (uK * yN);
            else
                yAccel += ballWeight / (uK * yN);
            yVel += (yAccel * dT);
            float yS = (float) (yAccel * Math.pow(dT, 2) / 2 + yVel * dT);
            yPos -= yS;
        }

        Log.d("POS", "----XPOS: " + xPos + " YPOS: " + yPos);
        if (yPos >= yMax) {
            yPos = yMax;
            yVel = 0;
        } else if (yPos <= 0) {
            yPos = 0;
            yVel = 0;
        }

    }
}
