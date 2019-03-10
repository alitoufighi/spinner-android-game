package ir.ac.ut.ece.spinner;

import android.content.Context;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import java.util.Objects;

public class GravityActivity
        extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor sensor;
    private static final float NS2S = 1.0f / 1000000000.0f;
    private float timestamp;
    private ImageView ballImage;
    private SpinnerPhysicsAssistant spinnerPhysicsAssitant;

    @Override
    protected void onStart() {
        super.onStart();
        if(sensor != null){
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME);
        } else {
            System.out.println("--SENSOR IS NULL");
        }
    }

//    private void getSensorList() {
//        SensorManager sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
//
//        List<Sensor> sensors = sensorManager.getSensorList(Sensor.TYPE_ALL);
//
//        StringBuilder strLog = new StringBuilder();
//        int iIndex = 1;
//        for (Sensor item : sensors) {
//            strLog.append(iIndex).append(".");
//            strLog.append(" Sensor Type - ").append(item.getType()).append("\r\n");
//            strLog.append(" Sensor Name - ").append(item.getName()).append("\r\n");
//            strLog.append(" Sensor Version - ").append(item.getVersion()).append("\r\n");
//            strLog.append(" Sensor Vendor - ").append(item.getVendor()).append("\r\n");
//            strLog.append(" Maximum Range - ").append(item.getMaximumRange()).append("\r\n");
//            strLog.append(" Minimum Delay - ").append(item.getMinDelay()).append("\r\n");
//            strLog.append(" Power - ").append(item.getPower()).append("\r\n");
//            strLog.append(" Resolution - ").append(item.getResolution()).append("\r\n");
//            strLog.append("\r\n");
//            iIndex++;
//        }
//        System.out.println(strLog.toString());
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(getSupportActionBar()).hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        if(sensor == null)
            System.out.println("NULL SENSOR");
        setContentView(R.layout.activity_gravity);

        ballImage = findViewById(R.id.ballImage);
        Point size = new Point();
        getWindowManager().getDefaultDisplay().getSize(size);
        ballImage.setX(0);
        ballImage.setY(0);

        float xMax = size.x - 250;
        float yMax = size.y - 250;
        spinnerPhysicsAssitant = new SpinnerPhysicsAssistant(xMax, yMax);
    }

    @Override
    protected void onStop(){
        sensorManager.unregisterListener(this);
        super.onStop();
    }

    @Override
    public void onAccuracyChanged(Sensor arg0, int arg1){
    }

    @Override
    public void onSensorChanged(SensorEvent event)
    {
        if (event.accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE)
            return;

        float xAccel = event.values[0];
        float yAccel = -event.values[1];
        final float dT = (event.timestamp - timestamp) * NS2S;
        spinnerPhysicsAssitant.updateBall(dT, xAccel, yAccel);
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
