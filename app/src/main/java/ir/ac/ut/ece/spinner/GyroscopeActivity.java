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
import android.widget.TextView;

public class GyroscopeActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor sensor;
    private double ballWeight = 0.01;
    private double samplingRate = 0.04;
    private float xPos, xAccel, xVel = 0.0f;
    private float yPos, yAccel, yVel = 0.0f;
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
        /*register the sensor listener to listen to the gyroscope sensor, use the
        callbacks defined in this class, and gather the sensor information as quick
        as possible*/
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE),SensorManager.SENSOR_DELAY_GAME);
    }

    //When this Activity isn't visible anymore
    @Override
    protected void onStop()
    {
        //unregister the sensor listener
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
        //if sensor is unreliable, return void
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
        xVel += (xAccel * frameTime);
        yVel += (yAccel * frameTime);

        float xS = (float) (xAccel * Math.pow(frameTime, 2) / 2 + xVel * frameTime);
        float yS = (float) (yAccel * Math.pow(frameTime, 2) / 2 + yVel * frameTime);

        xPos -= xS;
        if (xPos >= xMax) {
            xPos = xMax;
            xVel = 0;
        } else if (xPos <= 0) {
            xPos = 0;
            xVel = 0;
        }

        yPos -= yS;
        if (yPos >= yMax) {
            yPos = yMax;
            yVel = 0;
        } else if (yPos <= 0) {
            yPos = 0;
            yVel = 0;
        }
    }
}
