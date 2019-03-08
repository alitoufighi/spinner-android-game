package ir.ac.ut.ece.spinner;

import android.content.Context;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import java.util.List;

public class GravityActivity
        extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor sensor;
    private double samplingRate = 0.04;
    private static final double ballWeight = 0.01;
    private float xPos, xVel, xAccel;
    private float yPos, yVel, yAccel;
    private float xMax, yMax;
    private ImageView ballImage;

    @Override
    protected void onStart() {
        super.onStart();
        if(sensor != null){
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME);
        } else {
            System.out.println("--SENSOR IS NULL");
        }
    }

    private void getSensorList() {
        SensorManager sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);

        List<Sensor> sensors = sensorManager.getSensorList(Sensor.TYPE_ALL);

        StringBuilder strLog = new StringBuilder();
        int iIndex = 1;
        for (Sensor item : sensors) {
            strLog.append(iIndex).append(".");
            strLog.append(" Sensor Type - ").append(item.getType()).append("\r\n");
            strLog.append(" Sensor Name - ").append(item.getName()).append("\r\n");
            strLog.append(" Sensor Version - ").append(item.getVersion()).append("\r\n");
            strLog.append(" Sensor Vendor - ").append(item.getVendor()).append("\r\n");
            strLog.append(" Maximum Range - ").append(item.getMaximumRange()).append("\r\n");
            strLog.append(" Minimum Delay - ").append(item.getMinDelay()).append("\r\n");
            strLog.append(" Power - ").append(item.getPower()).append("\r\n");
            strLog.append(" Resolution - ").append(item.getResolution()).append("\r\n");
            strLog.append("\r\n");
            iIndex++;
        }
        System.out.println(strLog.toString());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSensorList();
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        if(sensor == null){
            System.out.println("NULL SENSOR");
        }
        setContentView(R.layout.activity_gravity);
        ballImage = findViewById(R.id.ballImage);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        ballImage.setX(size.x / 2f);
        ballImage.setY(size.y / 2f);
        xMax = (float) size.x - 250;
        yMax = (float) size.y - 250;
    }

//    @Override
//    protected void onResume()
//    {
//        super.onResume();
//        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY),SensorManager.SENSOR_DELAY_GAME);
//    }

    @Override
    protected void onStop()
    {
        sensorManager.unregisterListener(this);
        super.onStop();
    }

    @Override
    public void onAccuracyChanged(Sensor arg0, int arg1)
    {

    }

    @Override
    public void onSensorChanged(SensorEvent event)
    {
        if (event.accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE)
            return;
        System.out.println("SENSOR CHANGED");

        xAccel = event.values[0];
        yAccel = -event.values[1];
        updateBall();
        ballImage.setX(xPos);
        ballImage.setY(yPos);
    }


    private void updateBall() {
        float frameTime = 0.25f;
        float gravity = 9.81f;
        float xAngle = (float) Math.asin(xAccel / gravity);
        float yAngle = (float) Math.asin(yAccel / gravity);
        System.out.println("X ANGLE:" + Float.toString(xAngle) +
                "X VEL: " + Float.toString(xVel));
        System.out.println("Y ANGLE:" + Float.toString(yAngle) +
                "Y VEL: " + Float.toString(yVel));
        float xN = (float) (ballWeight * gravity * Math.cos(xAngle));
        float yN = (float) (ballWeight * gravity * Math.cos(yAngle));

        if(xVel == 0) {
            if(Math.abs(ballWeight * xAccel) >= Math.abs(xN * 0.15f)) {
                xVel += (xAccel * frameTime);
                float xS = (float) (xAccel * Math.pow(frameTime, 2) / 2 + xVel * frameTime);
                xPos -= xS;
            }
        } else {
            if(xVel > 0)
                xAccel -= ballWeight / (0.1f * xN);
            else
                xAccel += ballWeight / (0.1f * xN);
            xVel += (xAccel * frameTime);
            float xS = (float) (xAccel * Math.pow(frameTime, 2) / 2 + xVel * frameTime);
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
            if(Math.abs(ballWeight * yAccel) >= Math.abs(yN * 0.15f)) {
                yVel += (yAccel * frameTime);
                float yS = (float) (yAccel * Math.pow(frameTime, 2) / 2 + yVel * frameTime);
                yPos -= yS;
            }
        } else {
            if(yVel > 0)
                yAccel -= ballWeight / (0.1f * yN);
            else
                yAccel += ballWeight / (0.1f * yN);
            yVel += (yAccel * frameTime);
            float yS = (float) (yAccel * Math.pow(frameTime, 2) / 2 + yVel * frameTime);
            yPos -= yS;
        }

        if (yPos >= yMax) {
            yPos = yMax;
            yVel = 0;
        } else if (yPos <= 0) {
            yPos = 0;
            yVel = 0;
        }
    }
}
