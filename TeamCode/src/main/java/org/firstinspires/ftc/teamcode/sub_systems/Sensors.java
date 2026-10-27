package org.firstinspires.ftc.teamcode.sub_systems;

import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Prism.GoBildaPrismDriver;

public class Sensors {
    GoBildaPrismDriver prism;
    private Servo gate_light;
    private AnalogInput sensor1, sensor2, sensor3, sensor4;
    double v_total;
    Shooter shooter;

    public Sensors(HardwareMap hardwareMap){
        sensor1 = hardwareMap.get(AnalogInput.class, "ca0");
        sensor2 = hardwareMap.get(AnalogInput.class, "ca1");
        sensor3 = hardwareMap.get(AnalogInput.class, "ca2");
        sensor4 = hardwareMap.get(AnalogInput.class, "ca3");
        prism = hardwareMap.get(GoBildaPrismDriver.class, "prism");
        gate_light = hardwareMap.get(Servo.class, "es2");
    }

    public double getVoltage(){
        v_total = sensor1.getVoltage() + sensor2.getVoltage() + sensor3.getVoltage() + sensor4.getVoltage();
        return v_total;
    }

    public void artBoard(double voltage){
        if(voltage < 1){
            prism.loadAnimationsFromArtboard(GoBildaPrismDriver.Artboard.ARTBOARD_6);
        }
        else if(voltage > 1 && voltage < 2){
            prism.loadAnimationsFromArtboard(GoBildaPrismDriver.Artboard.ARTBOARD_5);
        }
        else if(voltage > 2 && voltage < 3.5){
            prism.loadAnimationsFromArtboard(GoBildaPrismDriver.Artboard.ARTBOARD_4);
        }
        else if(voltage > 3){
            prism.loadAnimationsFromArtboard(GoBildaPrismDriver.Artboard.ARTBOARD_7);
        }
    }

    public void set_light(double val){
        gate_light.setPosition(val);
    }
}
