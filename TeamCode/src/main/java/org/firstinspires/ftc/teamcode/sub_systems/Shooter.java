package org.firstinspires.ftc.teamcode.sub_systems;

import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.util.ElapsedTime;

public class Shooter {

    public double p = 140.5, f = 29.21;
    PIDFCoefficients coefficients = new PIDFCoefficients(p, 0, 0, f);
    //public static double main_p_far = 300, main_f_far = 5.2, counter_p_far = 125, counter_f_far = 6;
    public final DcMotorEx main_left, main_right;
    public final Servo gate, hood;
    public double GATE_OPEN = .68, GATE_CLOSE = .63;
    public double velocity, distance, x, y, target_x, target_y, distance_x, distance_y, distanceIN;
    public enum ShootState {IDLE, GATE_OPENING, FEEDING}
    public ShootState shootState;
    ElapsedTime gateTimer = new ElapsedTime(), shootTimer = new ElapsedTime();
    Intake intake;
    //Sensors sensors;





    public Shooter(HardwareMap hardwareMap, Intake intake){
        main_left = hardwareMap.get(DcMotorEx.class, "e0");
        main_right = hardwareMap.get(DcMotorEx.class, "e1");
        gate = hardwareMap.get(Servo.class, "cs0");
        hood = hardwareMap.get(Servo.class, "");

        //this.sensors =  sensors;
        this.intake = intake;

        main_left.setDirection(DcMotorEx.Direction.REVERSE);
        main_left.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        main_left.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        main_left.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, coefficients);

        main_right.setDirection(DcMotorEx.Direction.FORWARD);
        main_right.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        main_right.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        main_right.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, coefficients);
    }

    public void shootStart(){
        main_left.setVelocity(1040);
        main_right.setVelocity(1040);
    }
    public void shootStop(){
        main_left.setVelocity(0);
        main_right.setVelocity(0);
    }

    //will need change
    public double velocity_calc_MAIN(Pose pose, boolean redside){
        if(distance_calc(pose, redside) > 310){
            velocity = 1470;
        } else {
            velocity = 136585300 +
                    (957.0336 - 136585300) /
                            (1 + Math.pow(distance_calc(pose, redside) / 11757.85, 3.363418));
        }
        return velocity;
    }

    public void set_velocity(double v){
        main_left.setVelocity(v);
        main_right.setVelocity(v);
    }
    public double distance_calc(Pose pose, boolean redside){
        x = pose.getX();
        y = pose.getY();

        if(redside){
            target_x = 14.5;
            target_y = 128.5;
        }
        else{
            target_x = 14.5;
            target_y = 15.5;
        }

        distance_x = target_x - x;
        distance_y = target_y - y;
        distanceIN = Math.sqrt((distance_x * distance_x) + (distance_y * distance_y));
        distance = distanceIN * 2.54;
        return distance;
    }

    public void startShootSequence(){          // call once
        if (shootState == ShootState.IDLE) {
            gate_open();
            shootTimer.reset();
            shootState = ShootState.GATE_OPENING;
        }
    }
    public void updateShootSequence(){         // call every loop
        switch (shootState) {
            case GATE_OPENING:
                if (shootTimer.seconds() > 0.5) {
                    intake.shoot();
                    shootTimer.reset();
                    shootState = ShootState.FEEDING;
                }
                break;
            case FEEDING:
                if (shootTimer.seconds() > 1.25) {
                    intake.off();
                    gate_close();
                    shootState = ShootState.IDLE;
                }
                break;
        }
    }

    public double getVelocity(){
        return main_left.getVelocity();
    }
    public double getGatePose(){
        return gate.getPosition();
    }

    public void gate_open(){
        gate.setPosition(GATE_OPEN);
        //gateLight();
    }
    public void gate_close(){
        gate.setPosition(GATE_CLOSE);
        //gateLight();
    }

    public void gateLight(){
        if(getGatePose() == 1){
            //sensors.set_light(0.44);
        }
        else if(getGatePose() == 0){
            //sensors.set_light(0.33);
        }
    }
}
