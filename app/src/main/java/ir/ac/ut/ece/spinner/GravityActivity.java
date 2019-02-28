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

public class GravityActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor sensor;
    private double ballWeight = 0.01;
    private double samplingRate = 0.04;
    private float xPos, xAccel, xVel, xAngle, xN = 0.0f;
    private float yPos, yAccel, yVel, yAngle, yN = 0.0f;
    private float xMax, yMax;
    private ImageView ballImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        setContentView(R.layout.activity_gravity);
        ballImage = findViewById(R.id.ballImage);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        ballImage.setX(size.x / 2);
        ballImage.setY(size.y / 2);
        xMax = (float) size.x - 250;
        yMax = (float) size.y - 250;
    }
    @Override
    protected void onResume()
    {
        super.onResume();
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY),SensorManager.SENSOR_DELAY_GAME);
    }

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
        {
            return;
        }
        xAccel = event.values[0];
        yAccel = -event.values[1];
        updateBall();
        ballImage.setX(xPos);
        ballImage.setY(yPos);
    }
    private void updateBall() {
        float frameTime = 0.25f;
        float gravity = 9.81f;
        xAngle = (float) Math.asin(xAccel / gravity);
        System.out.println("X ANGLE:" + Float.toString(xAngle));
        yAngle = (float) Math.asin(yAccel / gravity);
        System.out.println("Y ANGLE:" + Float.toString(yAngle));
        xN = (float) (ballWeight * gravity * Math.cos(xAngle));
        yN = (float) (ballWeight * gravity * Math.cos(yAngle));
        if(xVel == 0) {
            if(Math.abs(ballWeight * xAccel) >= Math.abs(xN * 0.15f)) {
                xVel += (xAccel * frameTime);
                float xS = (float) (xAccel * Math.pow(frameTime, 2) / 2 + xVel * frameTime);
                xPos -= xS;
            } else {
                xVel = 0;
            }
        } else {
            xVel += (xAccel * frameTime);
            float xS = (float) ((xAccel - ballWeight / (0.1f * xN))  * Math.pow(frameTime, 2) / 2 + xVel * frameTime);
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
            } else {
                yVel = 0;
            }
        } else {
            yVel += (yAccel * frameTime);
            float yS = (float) ((yAccel - ballWeight / (0.1f * yN))  * Math.pow(frameTime, 2) / 2 + yVel * frameTime);
            yPos -= yS;
        }

        if (xPos >= xMax) {
            xPos = xMax;
            xVel = 0;
        } else if (xPos <= 0) {
            xPos = 0;
            xVel = 0;
        }
    }
}
