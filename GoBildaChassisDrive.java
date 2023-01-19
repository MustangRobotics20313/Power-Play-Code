package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

@SuppressWarnings({"unused", "FieldCanBeLocal"})
@TeleOp
public class GoBildaChassisDrive extends LinearOpMode {

    private DcMotor fl;
    private DcMotor fr;
    private DcMotor rl;
    private DcMotor rr;
    private DcMotor slide;
    private Servo grabber;

    @Override
    public void runOpMode() {
        fl = hardwareMap.get(DcMotor.class, "fl");
        fr = hardwareMap.get(DcMotor.class, "fr");
        rl = hardwareMap.get(DcMotor.class, "rl");
        rr = hardwareMap.get(DcMotor.class, "rr");
        slide = hardwareMap.get(DcMotor.class, "slide");
        grabber = hardwareMap.get(Servo.class, "grabber");

        rr.setDirection(DcMotor.Direction.REVERSE);

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
            fr.setPower(gamepad1.right_stick_x);
            rl.setPower(gamepad1.right_stick_x);
            rr.setPower(-gamepad1.right_stick_x);

            if (gamepad1.left_bumper) {
                slide.setPower(0.7);
            } else if (gamepad1.right_bumper) {
                slide.setPower(-0.2);
            } else {
                slide.setPower(0);
            }

            if (gamepad1.left_trigger > 0) {
                grabber.setPosition(0);
            } else if (gamepad1.right_trigger > 0) {
                grabber.setPosition(0.5);
            }

            telemetry.update();
        }
    }
}
