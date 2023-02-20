package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;


@Autonomous(name = "Location1")
@Deprecated
public class Location1 extends LinearOpMode {

    private DcMotor rr;
    private DcMotor fr;
    private DcMotor rl;
    private DcMotor fl;

    @Override
    public void runOpMode() { 
    rr = hardwareMap.get(DcMotor.class, "rr");
    fr = hardwareMap.get(DcMotor.class, "fr");
    rl = hardwareMap.get(DcMotor.class, "rl");
    fl = hardwareMap.get(DcMotor.class, "fl");
    

    waitForStart();

    rr.setPower(-0.5);
    fr.setPower(0.5);
    rl.setPower(0.5);
    fl.setPower(0.5);

    sleep(2000);

    rr.setPower(0.5);
    fr.setPower(-0.5);
    rl.setPower(-0.5);
    fl.setPower(0.5);

    sleep(2000);


    rr.setPower(0);
    fr.setPower(0);
    rl.setPower(0);
    fl.setPower(0);


    }
}
