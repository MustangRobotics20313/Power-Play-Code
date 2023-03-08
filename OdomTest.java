package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "name")
public class OdomTest extends OpMode {

    private DcMotorEx fl;
    private DcMotorEx fr;
    private DcMotor rl;
    private DcMotor rr;
    private double startxpos = 0.00;
    private double startypos = 0.00;
    private double startrot = 0.00;
    private double lEncDis;
    private double rEncDis;
    private double WheelRadis = 90.00;
    private double ticksInRot = 8192.00;
    private double encoderConstent =(WheelRadis*6.28)/ticksInRot;

    int oldEncR;
    int oldEncL;
    int newEncR;
    int newEncL;
    TwoWheelOdom robot = new TwoWheelOdom(startxpos, startypos, startrot, rEncDis, lEncDis);
    @Override
    public void init() {
        fl = hardwareMap.get(DcMotorEx.class, "fl"); //HAS LEFT SIDE DEADWHEEL
        fr = hardwareMap.get(DcMotorEx.class, "fr"); //HAS RIGHT SIDE DEADWHEEL
        rl = hardwareMap.get(DcMotor.class, "rl");
        rr = hardwareMap.get(DcMotor.class, "rr");
        newEncR = fr.getCurrentPosition();
        newEncL = fl.getCurrentPosition();

    }

    @Override
    public void loop() {
        oldEncR = newEncR;
        oldEncL = newEncL;
        newEncR = fr.getCurrentPosition();
        newEncL = fl.getCurrentPosition();
        double changeEncR = (newEncR-oldEncR)/encoderConstent;
        double changeEncL = (newEncL-oldEncL)/encoderConstent;
        robot.updatePos(changeEncR, changeEncL);
        double x = robot.getxpos();
        double y = robot.getypos();
        double r = robot.getrotation();

        telemetry.addLine(String.format("x pos="+fr.getCurrentPosition(),x));
        telemetry.addLine(String.format("y pos="+y,y));
        telemetry.addLine(String.format("rotation="+r,r));

    }
}
