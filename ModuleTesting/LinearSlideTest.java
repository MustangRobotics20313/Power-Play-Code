package org.firstinspires.ftc.teamcode.ModuleTesting;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.TouchSensor;

@SuppressWarnings({"unused", "FieldCanBeLocal"})
@TeleOp
public class LinearSlideTest extends LinearOpMode {

    private DcMotor slide;
    private TouchSensor slideSensor;
    
    @Override
    public void runOpMode() {
        slide = hardwareMap.get(DcMotor.class, "slide");
        slide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slide.setDirection(DcMotor.Direction.REVERSE);

        telemetry.setMsTransmissionInterval(75);

        slideSensor = hardwareMap.get(TouchSensor.class, "slideSensor");

        boolean toBeReset;

        waitForStart();
        while(opModeIsActive()) {
            if (gamepad1.left_bumper) {
                slide.setTargetPosition(4000);
                slide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                slide.setPower(0.8);
            } else if (gamepad1.right_bumper) {
                slide.setTargetPosition(0);
                slide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                slide.setPower(-0.8);
            } else if (gamepad1.left_trigger > 0) {
                slide.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                slide.setPower(0.8);
            } else if (gamepad1.right_trigger > 0) {
                slide.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                slide.setPower(-0.8);
            }

            if (gamepad1.left_trigger == 0 && gamepad1.right_trigger == 0 && slide.getMode() == DcMotor.RunMode.RUN_WITHOUT_ENCODER) {
                slide.setPower(0);
            }

            if (slideSensor.isPressed()) {
                slide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            }



            telemetry.addData("Slide position: ", slide.getCurrentPosition());
            telemetry.update();
        }
    }
}