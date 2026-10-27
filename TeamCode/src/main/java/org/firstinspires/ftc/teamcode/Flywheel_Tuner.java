package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.*;

@TeleOp // Registers this OpMode as a TeleOp.
public class Flywheel_Tuner extends OpMode {
    private DcMotorEx intake, leftMotor, rightMotor;

    public double highVelocity = 2140;
    public double lowVelocity = 1440;

    double curTargetVelocity = highVelocity;

    // Initial PIDF coefficients for tuning.
    public static double F = 29.21; // Feedforward gain to counteract constant forces like friction.
    public static double P = 140.5;    // Proportional gain to correct error based on how far off the velocity is.

    // Array of step sizes for making fine or coarse adjustments to P and F.
    public static double[] stepSizes = {10.0, 1.0, 0.1, 0.01, 0.001, 0.0001};
    // Index to select the current step size from the array.
    public static int stepIndex = 1;
    public static double intake_Power = .745;

    // --- DashBoard Telemetry --


    @Override
    public void init() {
        intake = hardwareMap.get(DcMotorEx.class, "c2");
        leftMotor = hardwareMap.get(DcMotorEx.class, "c0");
        rightMotor = hardwareMap.get(DcMotorEx.class, "c1");
        leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        leftMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        PIDFCoefficients pidfCoefficients = new PIDFCoefficients(P, 0, 0, F);
        leftMotor.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfCoefficients);
        rightMotor.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfCoefficients);
        telemetry.addLine("Init complete");

        telemetry.addLine("Initialized for FTC Dashboard");
    }

    @Override
    public void loop() {
        // --- Gamepad Controls for Tuning ---

        // 'Y' button toggles the target velocity between the high and low presets.
        if (gamepad1.yWasPressed()) {
            if (curTargetVelocity == highVelocity) {
                curTargetVelocity = lowVelocity;
            } else { curTargetVelocity = highVelocity; }
        }

        // 'B' button cycles through the different step sizes for tuning precision.
        if (gamepad1.bWasPressed()) {
            stepIndex = (stepIndex + 1) % stepSizes.length; // Modulo wraps the index back to 0.
        }

        // D-pad left/right adjusts the F (Feedforward) gain.
        if (gamepad1.dpadLeftWasPressed()) {
            F -= stepSizes[stepIndex];
        }
        if (gamepad1.dpadRightWasPressed()) {
            F += stepSizes[stepIndex];
        }

        // D-pad up/down adjusts the P (Proportional) gain.
        if (gamepad1.dpadUpWasPressed()) {
            P += stepSizes[stepIndex];
        }
        if (gamepad1.dpadDownWasPressed()) {
            P -= stepSizes[stepIndex];
        }
        if (gamepad1.left_trigger > 0.2) {
            intake.setPower(-intake_Power);
        }
        else {
            intake.setPower(0);
        }
//        leftServo.setPower(0);



        PIDFCoefficients pidfCoefficients = new PIDFCoefficients(P, 0, 0, F);
        // Apply the new coefficients to the motor in every loop iteration.
        leftMotor.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfCoefficients);
        rightMotor.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfCoefficients);

        // Command the motor to run at the current target velocity.
        leftMotor.setVelocity(curTargetVelocity);
        rightMotor.setVelocity(curTargetVelocity);

        // --- Telemetry Output ---

        double curVelocity = rightMotor.getVelocity();
        double error = curTargetVelocity - curVelocity;

        telemetry.addData("Target Velocity", curTargetVelocity);
        telemetry.addData("Current Velocity", "%.2f", curVelocity);
        telemetry.addData("Error", "%.2f", error);
        telemetry.addLine("-----------------------------");
        telemetry.addData("Tuning P", "%.4f (D-Pad U/D)", P);
        telemetry.addData("Tuning F", "%.4f (D-Pad L/R)", F);
        telemetry.addData("Step Size", "%.4f (B Button)", stepSizes[stepIndex]);
    }
}