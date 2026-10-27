package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.seattlesolvers.solverslib.util.TelemetryData;

@TeleOp(name = "Encoder_test", group = "Robot")
public class Encoder_test extends OpMode {
    private DigitalChannel magnet;
    TelemetryData telemetryData;

    @Override
    public void init() {
        magnet = hardwareMap.get(DigitalChannel.class, "di1");
        telemetryData = new TelemetryData(telemetry);
    }

    @Override
    public void loop(){
        telemetryData.addData("voltage: ", magnet.getState());
    }
}