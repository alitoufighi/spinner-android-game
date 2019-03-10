package ir.ac.ut.ece.spinner;

import android.content.Context;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import java.util.Objects;

public class GyroscopeActivity
        extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor sensor;
    private static final float NS2S = 1.0f / 1000000000.0f;
    private float timestamp;
    private ImageView ballImage;
    private SpinnerPhysicsAssistantGyroscope spinnerPhysicsAssitant;

    @Override
    protected void onStart() {
        super.onStart();
        if(sensor != null){
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST);
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
        if(sensor == null)
            System.out.println("NULL SENSOR");
        setContentView(R.layout.activity_gyroscope);

        ballImage = findViewById(R.id.ballImage);
        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);
        ballImage.setX(size.x / 2f);
        ballImage.setY(size.y / 2f);

        float xMax = size.x - ballImage.getWidth();
        float yMax = size.y - ballImage.getHeight();
        spinnerPhysicsAssitant = new SpinnerPhysicsAssistantGyroscope(xMax, yMax);
    }

    @Override
    protected void onStop(){
        sensorManager.unregisterListener(this);
        super.onStop();
    }

    @Override
    public void onAccuracyChanged(Sensor arg0, int arg1) {
    }

    @Override
    public void onSensorChanged(SensorEvent event){
        if (event.accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE)
            return;
        float yW = -event.values[0];
        float xW = -event.values[1];

        final float dT = (event.timestamp - timestamp) * NS2S;
        spinnerPhysicsAssitant.updateBall(dT, xW, yW);

        ballImage.setX(spinnerPhysicsAssitant.getX());
        ballImage.setY(spinnerPhysicsAssitant.getY());
        timestamp = event.timestamp;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        spinnerPhysicsAssitant.resetValues();
        return super.onTouchEvent(event);
    }

}
