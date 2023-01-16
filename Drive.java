package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@SuppressWarnings({"unused", "FieldCanBeLocal", "CommentedOutCode", "SpellCheckingInspection"})
@TeleOp
public class Drive extends LinearOpMode {

    private DcMotor fl;
    private DcMotor fr;
    private DcMotor rl;
    private DcMotor rr;
    private DcMotor slide;
    private DcMotor slide2;
    private DcMotor fourbar;
    private DcMotor intake;

    @Override
    public void runOpMode() {
        fl = hardwareMap.get(DcMotor.class, "fl");
        fr = hardwareMap.get(DcMotor.class, "fr");
        rl = hardwareMap.get(DcMotor.class, "rl");
        rr = hardwareMap.get(DcMotor.class, "rr");
        slide = hardwareMap.get(DcMotor.class, "slide");
        slide2 = hardwareMap.get(DcMotor.class, "slide2");
        fourbar = hardwareMap.get(DcMotor.class, "4bar");
        intake = hardwareMap.get(DcMotor.class, "intake");


        fr.setDirection(DcMotor.Direction.REVERSE);
        rl.setDirection(DcMotor.Direction.REVERSE);

        waitForStart();
        while(opModeIsActive()) {
            // D-pad: linear motion
            /*if (gamepad1.dpad_up) { //forward
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
            }*/

            //left stick: translational motion
            fl.setPower(-gamepad1.left_stick_y);
            fr.setPower(gamepad1.left_stick_y);
            rl.setPower(gamepad1.left_stick_y);
            rr.setPower(gamepad1.left_stick_y);

            //left stick


            if (gamepad2.a) {
                slide.setPower(0.9);
            } else if (gamepad2.b) {
                slide.setPower(-0.5);
            } else {
                slide.setPower(0);
            }
            if (gamepad2.x) {
                slide2.setPower(-0.8);
            } else if (gamepad2.y) {
                slide2.setPower(0.8);
            } else {
                slide2.setPower(0);
            }

            if (gamepad2.left_trigger > 0) {
                intake.setPower(0.8);
            } else if (gamepad2.right_trigger > 0) {
                intake.setPower(-0.8);
            }
            if (gamepad2.left_bumper) {
                intake.setPower(0);
            } if (gamepad2.right_bumper) {
                intake.setPower(0);
            }

            if (gamepad2.dpad_up) {
                fourbar.setPower(0.8);
            } else if (gamepad2.dpad_down) {
                fourbar.setPower(-0.4);
            } else {
                fourbar.setPower(0);
            }

            //left stick: rotation
            fl.setPower(gamepad1.left_stick_x);
            fr.setPower(gamepad1.left_stick_x);
            rl.setPower(-gamepad1.left_stick_x);
            rr.setPower(gamepad1.left_stick_x);


            //right stick: strafing
            fl.setPower(-gamepad1.right_stick_x);
            fr.setPower(-gamepad1.right_stick_x);
            rr.setPower(gamepad1.right_stick_x);
            rl.setPower(-gamepad1.right_stick_x);
        }
    }

    /*private void allPower(double power) {
        fl.setPower(power);
        fr.setPower(power);
        rl.setPower(-power);
        rr.setPower(-power);
    }*/
}