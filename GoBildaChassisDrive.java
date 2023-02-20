package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;

@SuppressWarnings({"unused", "FieldCanBeLocal"})
@TeleOp
public class GoBildaChassisDrive extends LinearOpMode {

    private DcMotor fl;
    private DcMotor fr;
    private DcMotor rl;
    private DcMotor rr;
    private DcMotorEx slide;
    private Servo grabber;
    private TouchSensor slideSensor;

    //units of ticks
    private final int RETRACTED_POSITION = 0;
    private final int LOW_POSITION = 1600;
    private final int MIDDLE_POSITION = 2875;
    private final int HIGH_POSITION = 4000;

    private final int STACK_FIVE_POSITION = 500;
    private final int STACK_FOUR_POSITION = 390;
    private final int STACK_THREE_POSITION = 230;
    private final int STACK_TWO_POSITION = 90;

    private final double SLIDE_POWER = 0.9;

    @Override
    public void runOpMode() {
        fl = hardwareMap.get(DcMotor.class, "fl");
        fr = hardwareMap.get(DcMotor.class, "fr");
        rl = hardwareMap.get(DcMotor.class, "rl");
        rr = hardwareMap.get(DcMotor.class, "rr");
        slide = hardwareMap.get(DcMotorEx.class, "slide");
        grabber = hardwareMap.get(Servo.class, "grabber");
        slideSensor = hardwareMap.get(TouchSensor.class, "slideSensor");

        fr.setDirection(DcMotor.Direction.REVERSE);
        rr.setDirection(DcMotor.Direction.REVERSE);
        slide.setDirection(DcMotor.Direction.REVERSE);
        grabber.scaleRange(0, 1);

        telemetry.setMsTransmissionInterval(75);

        slide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        waitForStart();
        while(opModeIsActive()) {
            //left stick: translational motion
            fl.setPower(gamepad1.left_stick_y);
            fr.setPower(gamepad1.left_stick_y);
            rl.setPower(gamepad1.left_stick_y);
            rr.setPower(gamepad1.left_stick_y);

            //left stick: rotation
            fl.setPower(-gamepad1.left_stick_x);
            fr.setPower(gamepad1.left_stick_x);
            rl.setPower(-gamepad1.left_stick_x);
            rr.setPower(gamepad1.left_stick_x);

            //right stick: strafing
            fl.setPower(-gamepad1.right_stick_x);
            fr.setPower(-gamepad1.right_stick_x);
            rl.setPower(gamepad1.right_stick_x);
            rr.setPower(gamepad1.right_stick_x);

            //diagonal strafing
            if (gamepad1.left_stick_y > 0) {
                if (gamepad1.right_stick_x < 0) {
                    fl.setPower(0);
                    fr.setPower(gamepad1.right_stick_x);
                    rl.setPower(gamepad1.right_stick_x);
                    rr.setPower(0);
                } else if (gamepad1.right_stick_x > 0) {
                    fl.setPower(gamepad1.right_stick_x);
                    fr.setPower(0);
                    rl.setPower(0);
                    rr.setPower(gamepad1.right_stick_x);
                }
            } else if (gamepad1.right_stick_y < 0) {
                if (gamepad1.right_stick_x < 0) {
                    fl.setPower(0);
                    fr.setPower(-gamepad1.right_stick_x);
                    rl.setPower(-gamepad1.right_stick_x);
                    rr.setPower(0);
                } else if (gamepad1.right_stick_x > 0) {
                    fl.setPower(-gamepad1.right_stick_x);
                    fr.setPower(0);
                    rl.setPower(0);
                    rr.setPower(-gamepad1.right_stick_x);
                }
            }

            slide();

            grabber();

            telemetry.addData("Front left power\t: ", fl.getPower());
            telemetry.addData("Front right power\t: ", fl.getPower());
            telemetry.addData("Rear left power\t: ", fl.getPower());
            telemetry.addData("Front left power\t: ", fl.getPower());
            telemetry.addData("Slide power\t: ", slide.getPower());
            telemetry.addData("Slide position\t: ", slide.getCurrentPosition());
            telemetry.addData("Grabber position: ", grabber.getPosition());
            telemetry.update();
        }
    }

    private void slide() {
        if (gamepad1.left_bumper) { //full extension
            slide.setTargetPosition(HIGH_POSITION);
            slide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            slide.setPower(SLIDE_POWER);
        } else if (gamepad1.right_bumper) { //full retraction
            slide.setTargetPosition(RETRACTED_POSITION);
            slide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            slide.setPower(-SLIDE_POWER);
        } else if (gamepad1.left_trigger > 0) { //manual control
            slide.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            slide.setPower(SLIDE_POWER);
        } else if (gamepad1.right_trigger > 0) { //manual control
            slide.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            slide.setPower(-SLIDE_POWER);
        } else if (gamepad1.a) { //extend to low junction
            slide.setTargetPosition(LOW_POSITION);
            slide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            if (slide.getCurrentPosition() > LOW_POSITION) {
                slide.setPower(-SLIDE_POWER);
            } else {
                slide.setPower(SLIDE_POWER);
            }
        } else if (gamepad1.b) { //extend to medium junction
            slide.setTargetPosition(MIDDLE_POSITION);
            slide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            if (slide.getCurrentPosition() > MIDDLE_POSITION) {
                slide.setPower(-SLIDE_POWER);
            } else {
                slide.setPower(SLIDE_POWER);
            }
        } else if (gamepad2.a) {
            slide.setTargetPosition(STACK_FIVE_POSITION);
            slide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            if (slide.getCurrentPosition() > STACK_FIVE_POSITION) {
                slide.setPower(-SLIDE_POWER);
            } else {
                slide.setPower(SLIDE_POWER);
            }
        } else if (gamepad2.b) {
            slide.setTargetPosition(STACK_FOUR_POSITION);
            slide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            if (slide.getCurrentPosition() > STACK_FOUR_POSITION) {
                slide.setPower(-SLIDE_POWER);
            } else {
                slide.setPower(SLIDE_POWER);
            }
        } else if (gamepad2.x) {
            slide.setTargetPosition(STACK_THREE_POSITION);
            slide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            if (slide.getCurrentPosition() > STACK_THREE_POSITION) {
                slide.setPower(-SLIDE_POWER);
            } else {
                slide.setPower(SLIDE_POWER);
            }
        } else if (gamepad2.y) {
            slide.setTargetPosition(STACK_TWO_POSITION);
            slide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            if (slide.getCurrentPosition() > STACK_TWO_POSITION) {
                slide.setPower(-SLIDE_POWER);
            } else {
                slide.setPower(SLIDE_POWER);
            }
        }

        if (gamepad1.left_trigger == 0 && gamepad1.right_trigger == 0 && slide.getMode() == DcMotor.RunMode.RUN_WITHOUT_ENCODER) {
            slide.setPower(0);
        }

        if (slideSensor.isPressed()) {
            slide.setPower(0);
            slide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        }
    }

    private void grabber() {
        if (gamepad1.left_stick_button) {
            grabber.setPosition(0.24);
        } else if (gamepad1.right_stick_button) {
            grabber.setPosition(0.45);
        }
    }
}