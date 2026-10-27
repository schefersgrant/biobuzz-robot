package org.firstinspires.ftc.teamcode.sub_systems;


import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.BRAKE;

import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Drivetrain {
    public DcMotor frontL, frontR, backL, backR;
    public Drivetrain(HardwareMap hardwareMap){
        frontL = hardwareMap.get(DcMotor.class, "e2");
        frontR = hardwareMap.get(DcMotor.class, "c1");
        backL = hardwareMap.get(DcMotor.class, "e3");
        backR = hardwareMap.get(DcMotor.class, "c2");

        frontL.setDirection(DcMotor.Direction.FORWARD);
        frontR.setDirection(DcMotor.Direction.REVERSE);
        backL.setDirection(DcMotor.Direction.FORWARD);
        backR.setDirection(DcMotor.Direction.REVERSE);

        frontL.setZeroPowerBehavior(BRAKE);
        frontR.setZeroPowerBehavior(BRAKE);
        backL.setZeroPowerBehavior(BRAKE);
        backR.setZeroPowerBehavior(BRAKE);
    }

    public double angle_Calc(Pose pose, boolean redSide){
        double x = pose.getX();
        double y = pose.getY();
        double currentHeading = pose.getHeading();   // radians in most libs

        double tx = 11.0;   // target X in SAME units/frame as pose (often inches)
        double ty = 11.0;   // target Y

        if(!redSide) {
            tx = 11.0;   // target X in SAME units/frame as pose (often inches)
            ty = 11.0;   // target Y
        }
        else{
            tx = 11.0;
            ty = 128.5;
        }
        double desiredHeading = headingToTarget(x, y, tx, ty);
        double error = headingError2(desiredHeading, currentHeading);
        return error;
    }

    private static double headingToTarget(double x, double y, double tx, double ty) {
        double dx = tx - x;
        double dy = ty - y;
        return Math.atan2(dy, dx);
    }

    private static double headingError2(double desired, double current) {
        return wrapRad(desired - current);
    }

    private static double wrapRad(double a) {
        while (a >= Math.PI) a -= 2.0 * Math.PI;
        while (a < -Math.PI) a += 2.0 * Math.PI;
        return a;
    }
}
