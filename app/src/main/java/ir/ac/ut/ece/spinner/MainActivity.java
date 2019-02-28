package ir.ac.ut.ece.spinner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
    }

    public void onGyroscopeSelect(View view) {
        Intent intent = new Intent(this, GyroscopeActivity.class);
        startActivity(intent);
    }

    public void onGravitySelect(View view) {
        Intent intent = new Intent(this, GravityActivity.class);
        startActivity(intent);
    }
}
