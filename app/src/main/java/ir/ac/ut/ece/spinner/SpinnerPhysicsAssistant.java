package ir.ac.ut.ece.spinner;

import android.hardware.SensorManager;
import android.util.Log;

public class SpinnerPhysicsAssistant {
    static final float ballMass = 0.01f;
    static final float EPSILON = 0.004f;
    static final float uS = 0.15f, uK = 0.10f;
    static final float gravity = SensorManager.STANDARD_GRAVITY;

    float xMax, yMax;
    float xPos, xVel = 0.0f;
    float yPos, yVel = 0.0f;
    float xTheta, yTheta;

    private float metersToPixels(float m) {
        return 0f;
//        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM,
//                m * 10, getResources().getDisplayMetrics());
    }

    SpinnerPhysicsAssistant(float xMax, float yMax){
        this.xMax = xMax;
        this.yMax = yMax;
        resetValues();
    }

    float getX() { return xPos; }

    float getY() { return yPos; }

    void resetValues() {
        xPos = 0.0f;
        xVel = 0.0f;
        xTheta = 0.0f;

        yPos = 0.0f;
        yVel = 0.0f;
        yTheta = 0.0f;
    }

    public void updateBall(float dT, float xAccel, float yAccel){

        float xN = (float) (ballMass * gravity * Math.cos(xTheta));
        float yN = (float) (ballMass * gravity * Math.cos(yTheta));

        if(xVel == 0) {
            if(Math.abs(ballMass * xAccel) >= Math.abs(xN * uS)) {
                xVel += (xAccel * dT);
                float xS = (float) (xAccel * Math.pow(dT, 2) / 2 + xVel * dT) * 250;
                xPos -= xS;
            }
        } else {
            if(xVel > 0)
                xAccel -= ballMass / (uK * xN);
            else
                xAccel += ballMass / (uK * xN);
            xVel += (xAccel * dT);
            float xS = (float) (xAccel * Math.pow(dT, 2) / 2 + xVel * dT) * 250;
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
            if(Math.abs(ballMass * yAccel) >= Math.abs(yN * uS)) {
                yVel += (yAccel * dT);
                float yS = (float) (yAccel * Math.pow(dT, 2) / 2 + yVel * dT) * 250;
                yPos -= yS;
            }
        } else {
            if(yVel > 0)
                yAccel -= ballMass / (uK * yN);
            else
                yAccel += ballMass / (uK * yN);
            yVel += (yAccel * dT);
            float yS = (float) (yAccel * Math.pow(dT, 2) / 2 + yVel * dT) * 250;
            yPos -= yS;
        }
//        Log.d("ACC", "----xAcc: " + xAccel + "\t\tyAcc: " + yAccel);
//        Log.d("POS", "----XPOS: " + xPos + "\t\tYPOS: " + yPos);
//        Log.d("VEL", "----xVel " + xVel + "\t\tyVel: " + yVel);
//        Log.d("ANG", "----xANG: " + Math.toDegrees(xTheta) + "\t\tyANG: " + Math.toDegrees(yTheta));

        if (yPos >= yMax) {
            yPos = yMax;
            yVel = 0;
        } else if (yPos <= 0) {
            yPos = 0;
            yVel = 0;
        }

    }
}
