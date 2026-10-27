package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name = "Gate_poses", group = "Robot")
public class Gate_poses extends OpMode {
    ElapsedTime timer = new ElapsedTime();
    double position = 0.5;

    private Servo gate = null;

    @Override
    public void init() {
        gate = hardwareMap.get(Servo.class, "cs0");
    }


    @Override
    public void loop() {
        if(gamepad1.dpad_up && timer.seconds() > 0.3) {
            position += 0.05;
            gate.setPosition(position);
            timer.reset();
        }
        if(gamepad1.dpad_down && timer.seconds() > 0.3) {
            position -= 0.05;
            gate.setPosition(position);
            timer.reset();
        }
        telemetry.addData("Gate Position:", position);
    }

}