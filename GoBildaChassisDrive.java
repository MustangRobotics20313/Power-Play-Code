package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

@SuppressWarnings({"unused", "FieldCanBeLocal"})
@TeleOp
public class GoBildaChassisDrive extends LinearOpMode {

    private DcMotor fl;
    private DcMotor fr;
    private DcMotor rl;
    private DcMotor rr;
    private DcMotorEx slide;
    public Servo grabber;

    @Override
    public void runOpMode() {
        fl = hardwareMap.get(DcMotor.class, "fl");
        fr = hardwareMap.get(DcMotor.class, "fr");
        rl = hardwareMap.get(DcMotor.class, "rl");
        rr = hardwareMap.get(DcMotor.class, "rr");
        slide = hardwareMap.get(DcMotorEx.class, "slide");
        grabber = hardwareMap.get(Servo.class, "grabber");

        fl.setDirection(DcMotor.Direction.REVERSE);
        rl.setDirection(DcMotor.Direction.REVERSE);
        grabber.scaleRange(0, 1);

        //slide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        waitForStart();
        while(opModeIsActive()) {
            //left stick: translational motion
            fl.setPower(gamepad1.left_stick_y);
            fr.setPower(gamepad1.left_stick_y);
            rl.setPower(gamepad1.left_stick_y);
            rr.setPower(gamepad1.left_stick_y);

            //left stick: rotation
            fl.setPower(gamepad1.left_stick_x);
            fr.setPower(-gamepad1.left_stick_x);
            rl.setPower(gamepad1.left_stick_x);
            rr.setPower(-gamepad1.left_stick_x);

            //right stick: strafing
            fl.setPower(gamepad1.right_stick_x);
            fr.setPower(-gamepad1.right_stick_x);
            rl.setPower(-gamepad1.right_stick_x);
            rr.setPower(gamepad1.right_stick_x);
            /*
            if (gamepad1.left_bumper) {
                slide.setTargetPosition(3750);
                slide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                slide.setPower(0.6);
            } else if (gamepad1.right_bumper) {
                slide.setTargetPosition(0);
                slide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                slide.setPower(-0.6);
            } else if (gamepad1.left_trigger > 0) {
                slide.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                slide.setPower(0.7);
            } else if (gamepad1.right_trigger > 0) {
                slide.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                if (slide.getCurrentPosition() > 0) {
                    slide.setPower(-0.4);
                }
            }

            if (gamepad1.left_trigger == 0 && gamepad1.right_trigger == 0 && slide.getMode() == DcMotor.RunMode.RUN_WITHOUT_ENCODER) {
                slide.setPower(0);
            }*/

            //telemetry.addData("Slide position: ", slide.getCurrentPosition());

            if (gamepad1.left_trigger > 0) {
                slide.setPower(1);
            } else if (gamepad1.right_trigger > 0) {
                slide.setPower(-1);
            } else {
                slide.setPower(0);
            }

            if (gamepad1.left_stick_button) {
                grabber.setPosition(0);
            } else if (gamepad1.right_stick_button) {
                grabber.setPosition(0.5);
            }

            if (gamepad1.a) {
                grabber.setPosition(0);
            } else if (gamepad1.b) {
                grabber.setPosition(0.5);
            }

            telemetry.addData("Grabber position: ", grabber.getPosition());
            telemetry.update();
        }
    }
}
