package org.firstinspires.ftc.teamcode;




import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.sub_systems.Drivetrain;
import org.firstinspires.ftc.teamcode.sub_systems.Intake;
import org.firstinspires.ftc.teamcode.sub_systems.Sensors;
import org.firstinspires.ftc.teamcode.sub_systems.Shooter;
import org.firstinspires.ftc.teamcode.sub_systems.Turret;

import java.util.List;

import com.pedropathing.follower.Follower;
import com.pedropathing.drivetrain.DrivePowers;
import com.pedropathing.follower.ManualDrive;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import com.pedropathing.math.Pose; // check relocalizing pose

@TeleOp(name = "TeleOP", group = "Robot")
public class TeleOP extends OpMode{

    private Follower follower;

    private Limelight3A limelight3A;
    private IMU imu;
    private Shooter shooter;
    private Turret turret;
    private Sensors sensors;
    private Intake intake;
    private Drivetrain drivetrain;
    private boolean redSide, LLPoseSet = false, LLPoseFirstSet = false, manual;
    private double v_MAIN, v_COUNTER, angleToTarget = 0, distance, yawOffset;
    ElapsedTime LLPoseTimer = new ElapsedTime(), YawOffsetDelay = new ElapsedTime();

    @Override
    public void init(){
        //LL setup
        limelight3A = hardwareMap.get(Limelight3A.class, "limelight");
        limelight3A.pipelineSwitch(0);

        //IMU setup
        RevHubOrientationOnRobot RevOrientation = new RevHubOrientationOnRobot(
            RevHubOrientationOnRobot.LogoFacingDirection.LEFT,
            RevHubOrientationOnRobot.UsbFacingDirection.UP
        );
        imu = hardwareMap.get(IMU.class, "imu");
        imu.initialize(new IMU.Parameters(RevOrientation));


        //Subsystem setup
        turret = new Turret(hardwareMap);
        //sensors = new Sensors(hardwareMap);
        intake = new Intake(hardwareMap);
        drivetrain = new Drivetrain(hardwareMap);
        shooter = new Shooter(hardwareMap, intake);
        //follower setup
        follower = Constants.create(hardwareMap);

        telemetry.addData("Status", "Initialized");
    }
    @Override
    public void init_loop(){

    }
    @Override
    public void start(){
        follower.setPose(OpModeStorage.autonomousEndPose);
        follower.update();
    }
    @Override
    public void loop(){
        DrivePowers powers = ManualDrive.fieldCentric(
                -gamepad1.left_stick_y,
                gamepad1.left_stick_x,
                gamepad1.right_stick_x,
                follower.pose().heading()
        );
        follower.manual(powers);

        // relocalise button
        if (gamepad1.startWasPressed()) {
            Pose cornerPose = new Pose(10.5, 10.5, Math.toRadians(90));
            // On the fly Pose creation, we dont recommend this for Autonomous. Only accepts radians for heading
            follower.setPose(cornerPose); // overrides our pose
        }

        follower.update();
        Pose robotPose = follower.pose(); // returns a Pose object
        telemetry.addData("Robot X", robotPose.x());
        telemetry.addData("Robot Y", robotPose.y());
        telemetry.addData("Robot Heading", Math.toDegrees(robotPose.heading()));
        // Math.toDegrees() is a built-in java method

//        if(gamepad1.left_trigger > 0.1){
//            intake.intake();
//        }
//        else if(gamepad1.right_trigger > 0.1){
//            intake.outtake();
//        }
//        else{
//            intake.off();
//        }
//
//        if(gamepad1.dpad_up) {
//            shooter.gate_open();
//        }
//        if(gamepad1.dpad_down){
//            shooter.gate_close();
//        }
//
//        if (gamepad2.dpad_right && YawOffsetDelay.seconds() > 0.2) {
//            yawOffset-= 3;
//            YawOffsetDelay.reset();
//        }
//        if (gamepad2.dpad_left && YawOffsetDelay.seconds() > 0.2) {
//            yawOffset += 3;
//            YawOffsetDelay.reset();
//        }
//
//        //TURRET
//        turret.setYaw(-turret.inAllowedArcWithHysteresis(drivetrain.angle_Calc(follower.getPose(), redSide) + Math.toRadians(yawOffset), yawOffset));
//        turret.periodic();
//
//        if(gamepad2.a){
//            turret.on();
//        }
//        if(gamepad2.b){
//            turret.off();
//        }
//        if(gamepad2.dpad_up){
//            manual = false;
//        }
//        if(gamepad2.dpad_down){
//            manual = true;
//        }
//        if(manual){
//            turret.setYaw(gamepad2.left_stick_x*Math.PI);
//        }
//        else{
//            turret.automatic();
//        }
//        if(gamepad2.right_bumper){
//            turret.resetTurret();
//            yawOffset = 0;
//        }


    }
}
