package ir.ac.ut.ece.spinner;

class SpinnerPhysicsAssistantGyroscope extends SpinnerPhysicsAssistant {

    SpinnerPhysicsAssistantGyroscope(float xMax, float yMax) {
        super(xMax, yMax);
    }

    public void updateBall(float dT, float xW, float yW) {
        float xDeltaTheta = xW * dT;
        float yDeltaTheta = yW * dT;

        if(Math.abs(xW) > EPSILON){
            xTheta += xDeltaTheta;
        }
        if(Math.abs(yW) > EPSILON) {
            yTheta += yDeltaTheta;
        }

        float xAccel = gravity * (float) Math.sin(xTheta);
        float yAccel = gravity * (float) Math.sin(yTheta);

//        System.out.println(
//                "- XTHETA: " + Math.toDegrees(xTheta) + "    - YTHETA: " + Math.toDegrees(yTheta)
//        );
//        System.out.println("----------------------------");
        float xN = (float) (ballMass * gravity * Math.cos(xTheta));
        float yN = (float) (ballMass * gravity * Math.cos(yTheta));
//        Log.d("FRICTION", "----XN: " + xN + " YN: " + yN);
//        Log.d("ACCEL", "---x Accel: " + xAccel + " y Accel: " + yAccel);
//        Log.d("ANGLE", "----x ANGLE: " + Math.toDegrees(xTheta) +
//                                    " y ANGLE: " + Math.toDegrees(yTheta));
//        Log.d("TS", "--- Timestamp: " + dT);

        if(xVel == 0) {
            if(Math.abs(ballMass * xAccel) >= Math.abs(xN * uS)) {
                xVel += (xAccel * dT);
                float xS =  ((float) (xAccel * Math.pow(dT, 2) / 2 + xVel * dT)) * 500;
                xPos -= xS;
            }
        } else {
            if(xVel > 0)
                xAccel -= ballMass / (uK * xN);
            else
                xAccel += ballMass / (uK * xN);
            xVel += (xAccel * dT);
            float xS = ((float) (xAccel * Math.pow(dT, 2) / 2 + xVel * dT)) * 500;
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
                float yS = ((float) (yAccel * Math.pow(dT, 2) / 2 + yVel * dT)) * 500;
                yPos -= yS;
            }
        } else {
            if(yVel > 0)
                yAccel -= ballMass / (uK * yN);
            else
                yAccel += ballMass / (uK * yN);
            yVel += (yAccel * dT);
            float yS = ((float) (yAccel * Math.pow(dT, 2) / 2 + yVel * dT)) * 500;
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
