package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;

@TeleOp
public class Drive extends LinearOpMode {

    private DcMotor fl;
    private DcMotor fr;
    private DcMotor rl;
    private DcMotor rr;

    @Override
    public void runOpMode() {
        fl = hardwareMap.get(DcMotor.class, "fl");
        fr = hardwareMap.get(DcMotor.class, "fr");
        rl = hardwareMap.get(DcMotor.class, "rl");
        rr = hardwareMap.get(DcMotor.class, "rr");

        fr.setDirection(DcMotor.Direction.REVERSE);
        rl.setDirection(DcMotor.Direction.REVERSE);

        waitForStart();
        while(opModeIsActive()) {
            // D-pad: linear motion
            if (gamepad1.dpad_up) { //forward
                allPower(-0.8);
            } else if (gamepad1.dpad_down) { //backward
                allPower(0.8);
            } else if (gamepad1.dpad_left) { //strafe left
                fl.setPower(0.8);
                rl.setPower(0.8);
                fr.setPower(-0.8);
                rr.setPower(-0.8);
            } else if (gamepad1.dpad_right) { // strafe right
                fr.setPower(0.8);
                rr.setPower(0.8);
                fl.setPower(-0.8);
                rl.setPower(-0.8);
            }

            //right stick: rotation
            fl.setPower(-gamepad1.right_stick_x);
            fr.setPower(gamepad1.right_stick_x);
            rr.setPower(-gamepad1.right_stick_x);
            rl.setPower(gamepad1.right_stick_x);

            //left stick: translational motion
            allPower(gamepad1.left_stick_y);

            //left stick: strafing
            fl.setPower(-gamepad1.left_stick_x);
            fr.setPower(gamepad1.left_stick_x);
            rr.setPower(gamepad1.left_stick_x);
            rl.setPower(-gamepad1.left_stick_x);
        }
    }

    private void allPower(double power) {
        fl.setPower(power);
        fr.setPower(power);
        rl.setPower(-power);
        rr.setPower(-power);
    }
}