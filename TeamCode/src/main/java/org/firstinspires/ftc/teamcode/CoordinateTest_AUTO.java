package org.firstinspires.ftc.teamcode;

import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;

@Autonomous(name = "coord test", group = "Autonomous")
public class CoordinateTest_AUTO extends OpMode {
    private TelemetryManager panelsTelemetry; // Panels Telemetry instance
    public static Follower follower; // Pedro Pathing follower instance
    private int pathState; // Current autonomous path state (state machine)
    private Paths paths; // Paths defined in the Paths class
    private Timer pathTimer, actionTimer, opmodeTimer;
    private static ElapsedTime spinUpTimer = new ElapsedTime();
    private static boolean redSide = false;
    private static boolean delay = false;

    private static final Pose start1 = new Pose(-3.8, 146.75, Math.toRadians(143));
    private static final Pose shoot = new Pose(12, -8, Math.toRadians(143));

    @Override
    public void init(){
        pathTimer = new Timer();
        opmodeTimer = new Timer();
        opmodeTimer.resetTimer();

        panelsTelemetry = PanelsTelemetry.INSTANCE.getTelemetry();

        follower = Constants.createFollower(hardwareMap);

        panelsTelemetry.debug("Status", "Initialized");
        panelsTelemetry.update(telemetry);

    }

    @Override
    public void start(){
        follower.setStartingPose(start1);
        paths =new Paths(follower);
    }
    @Override
    public void loop(){
        follower.update(); // Update Pedro Pathing
        pathState = autonomousPathUpdate(); // Update autonomous state machine

        // Log values to Panels and Driver Station
        panelsTelemetry.debug("Path State", pathState);
        panelsTelemetry.debug("X", follower.getPose().getX());
        panelsTelemetry.debug("Y", follower.getPose().getY());
        panelsTelemetry.debug("Heading", follower.getPose().getHeading());
        panelsTelemetry.update(telemetry);
    }
    public static class Paths{
        public Pose start, end;
        public PathChain test;

        public Paths(Follower follower){
            start = start1;
            end = shoot;

            test = follower.pathBuilder().addPath(new BezierLine(start, end))
                    .setConstantHeadingInterpolation(start.getHeading())
                    .build();
        }

    }
    public int autonomousPathUpdate(){
        switch(pathState){
            case 0:
                if(!follower.isBusy()){
                    follower.followPath(paths.test, true);
                    setPathState(-1);
                }
        }
        return pathState;
    }

    public void setPathState(int state){
        pathState = state;
        pathTimer.resetTimer();
    }
}

