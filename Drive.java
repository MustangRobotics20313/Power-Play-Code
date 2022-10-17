package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

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

        fl.setDirection(DcMotor.Direction.REVERSE);
        rr.setDirection(DcMotor.Direction.REVERSE);

        waitForStart();
        while(opModeIsActive()) {
            //left stick: general driving, includes strafing
            fl.setPower(gamepad1.left_stick_y);
            fr.setPower(gamepad1.left_stick_y);
            rr.setPower(-gamepad1.left_stick_y);
            rl.setPower(-gamepad1.left_stick_y);

            fl.setPower(-gamepad1.left_stick_x);
            fr.setPower(gamepad1.left_stick_x);
            rr.setPower(-gamepad1.left_stick_x):
            rl.setPower(gamepad1.left_stick_x);

            //right stick: robot rotation
            fl.setPower(-gamepad1.right_stick_x);
            fr.setPower(gamepad1.right_stick_x);
            rr.setPower(gamepad1.right_stick_x);
            rl.setPower(-gamepad1.right_stick_x);
        }
    }
}