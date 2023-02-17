package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@SuppressWarnings({"unused", "FieldCanBeLocal"})
@TeleOp
public class LinearSlideTest extends LinearOpMode {

    private DcMotor slide;
    
    @Override
    public void runOpMode() {
        slide = hardwareMap.get(DcMotor.class, "slide");
        slide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slide.setDirection(DcMotor.Direction.REVERSE);

        telemetry.setMsTransmissionInterval(75);

        waitForStart();
        while(opModeIsActive()) {
            if (gamepad1.left_bumper) {
                slide.setTargetPosition(4000);
                slide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                slide.setPower(0.6);
            } else if (gamepad1.right_bumper) {
                slide.setTargetPosition(0);
                slide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                slide.setPower(-0.6);
            } else if (gamepad1.left_trigger > 0) {
                slide.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                slide.setPower(0.6);
            } else if (gamepad1.right_trigger > 0) {
                slide.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                slide.setPower(-0.6);
            }

            if (gamepad1.left_trigger == 0 && gamepad1.right_trigger == 0 && slide.getMode() == DcMotor.RunMode.RUN_WITHOUT_ENCODER) {
                slide.setPower(0);
            }

            telemetry.addData("Slide position: ", slide.getCurrentPosition());
            telemetry.update();
        }
    }
}