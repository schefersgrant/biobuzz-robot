package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.seattlesolvers.solverslib.util.TelemetryData;

@TeleOp(name = "Shooter_Test", group = "Robot")
public class Shooter_test extends OpMode {
    private DcMotorEx main_L, main_R;
    TelemetryData telemetryData;

    @Override
    public void init(){
        main_L = hardwareMap.get(DcMotorEx.class, "c0");
        main_L.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        main_L.setDirection(DcMotorEx.Direction.FORWARD);
        main_L.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        main_R = hardwareMap.get(DcMotorEx.class, "c1");
        main_R.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        main_R.setDirection(DcMotorEx.Direction.REVERSE);
        main_R.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);

        telemetryData = new TelemetryData(telemetry);
    }
    public void loop(){
        main_L.setPower(gamepad1.left_trigger);
        main_R.setPower(gamepad1.left_trigger);

        telemetryData.addData("Velocity: ", main_L.getVelocity());
        telemetryData.addData("Velocity: ", main_R.getVelocity());
    }
}
