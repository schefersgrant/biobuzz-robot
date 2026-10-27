package org.firstinspires.ftc.teamcode.sub_systems;

import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.BRAKE;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Intake {

    public final DcMotor intake;
    public Intake(HardwareMap hardwareMap){
        intake = hardwareMap.get(DcMotor.class, "e3");
        intake.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        intake.setZeroPowerBehavior(BRAKE);
    }

    public void intake(){
        intake.setPower(1);
    }
    public void off(){
        intake.setPower(0);
    }
    public void outtake(){
        intake.setPower(-1);
    }
    public void shoot(){
        intake.setPower(-.7);
    }
}
