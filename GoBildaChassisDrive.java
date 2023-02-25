package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

@SuppressWarnings({"unused", "FieldCanBeLocal"})
@TeleOp
public class GoBildaChassisDrive extends LinearOpMode {

    private enum SlideState {
        RETRACTED,
        EXTENDED
    }

    private DcMotor fl;
    private DcMotor fr;
    private DcMotor rl;
    private DcMotor rr;
    private DcMotorEx slide;
    private Servo grabber;

    private TouchSensor slideSensor;
    private IMU imu;
    private YawPitchRollAngles robotOrientation;

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
        SlideState state = SlideState.RETRACTED;

        fl = hardwareMap.get(DcMotor.class, "fl"); //HAS RIGHT SIDE DEADWHEEL
        fr = hardwareMap.get(DcMotor.class, "fr"); //HAS REAR DEADWHEEL
        rl = hardwareMap.get(DcMotor.class, "rl"); //HAS LEFT SIDE DEADWHEEL
        rr = hardwareMap.get(DcMotor.class, "rr");
        slide = hardwareMap.get(DcMotorEx.class, "slide");
        grabber = hardwareMap.get(Servo.class, "grabber");
        slideSensor = hardwareMap.get(TouchSensor.class, "slideSensor");

        slide.setDirection(DcMotor.Direction.REVERSE);
        grabber.scaleRange(0, 1);

        telemetry.setMsTransmissionInterval(75);

        slide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        imu = hardwareMap.get(IMU.class, "imu");

        IMU.Parameters myImuParameters = new IMU.Parameters (
                new RevHubOrientationOnRobot(
                        new Orientation(
                                AxesReference.INTRINSIC,
                                AxesOrder.ZYX,
                                AngleUnit.DEGREES,
                                -90,
                                45,
                                0,
                                0
                        )
                )
        );

        imu.initialize(myImuParameters);

        waitForStart();
        while(opModeIsActive()) {

            slide();
            grabber();

            switch(state) {
                case RETRACTED:
                    fastMecanum();
                    if (slide.getCurrentPosition() > 300) state = SlideState.EXTENDED;
                    break;
                case EXTENDED:
                    slowMecanum();
                    if (slide.getCurrentPosition() < 300) state = SlideState.RETRACTED;
                    break;
            }

            telemetry.addData("Front left power\t: ", fl.getPower());
            telemetry.addData("Front right power\t: ", fl.getPower());
            telemetry.addData("Rear left power\t: ", fl.getPower());
            telemetry.addData("Front left power\t: ", fl.getPower());
            telemetry.addData("Slide power\t: ", slide.getPower());
            telemetry.addData("Slide position\t: ", slide.getCurrentPosition());
            telemetry.addData("Grabber position: ", grabber.getPosition());

            robotOrientation = imu.getRobotYawPitchRollAngles();

            telemetry.addData("Yaw\t:", robotOrientation.getYaw(AngleUnit.DEGREES));
            telemetry.addData("Pitch\t:", robotOrientation.getPitch(AngleUnit.DEGREES));
            telemetry.addData("Roll\t:", robotOrientation.getRoll(AngleUnit.DEGREES));

            telemetry.update();

            if (gamepad1.options) {
                imu.resetYaw();
            }
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
            grabber.setPosition(0.2);
        } else if (gamepad1.right_stick_button) {
            grabber.setPosition(0.45);
        }
    }

    private void fastMecanum() {
        double drive = -gamepad1.left_stick_x;
        double strafe = -gamepad1.right_stick_x;
        double twist = -gamepad1.left_stick_y;

        double v1 = drive + strafe - twist;
        double v2 = drive - strafe + twist;
        double v3 = drive - strafe - twist;
        double v4 = drive + strafe + twist;

        fl.setPower(v1 * 0.7);
        fr.setPower(v2 * 0.7);
        rl.setPower(v3 * 0.7);
        rr.setPower(v4 * 0.7);
    }

    private void slowMecanum() {
        //left stick: translational motion
        fl.setPower(gamepad1.left_stick_y);
        fr.setPower(-gamepad1.left_stick_y);
        rl.setPower(gamepad1.left_stick_y);
        rr.setPower(-gamepad1.left_stick_y);

        //left stick: rotation
        fl.setPower(-gamepad1.left_stick_x);
        fr.setPower(-gamepad1.left_stick_x);
        rl.setPower(-gamepad1.left_stick_x);
        rr.setPower(-gamepad1.left_stick_x);

        //right stick: strafing
        fl.setPower(-gamepad1.right_stick_x);
        fr.setPower(gamepad1.right_stick_x);
        rl.setPower(gamepad1.right_stick_x);
        rr.setPower(-gamepad1.right_stick_x);
    }
}