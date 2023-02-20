package org.firstinspires.ftc.teamcode.ModuleTesting;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@TeleOp
@Config
@SuppressWarnings({"unused", "FieldCanBeLocal"})
public class EncoderTesting extends LinearOpMode {

    private DcMotorEx slide;

    public static int ticks = 700;
    public static double extendPower = 0.5;
    public static double retractPower = 0.2;

    @Override
    public void runOpMode() {
        slide = hardwareMap.get(DcMotorEx.class, "slide");
        telemetry = new MultipleTelemetry(FtcDashboard.getInstance().getTelemetry(), telemetry);

        slide.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        waitForStart();

        while(opModeIsActive()) {
            if (gamepad1.a) {
                slide.setTargetPosition(ticks);
                slide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                slide.setPower(extendPower);
                while (slide.getCurrentPosition() < slide.getTargetPosition()) {
                    idle();
                }
            } else if (gamepad1.b) {
                slide.setPower(0);
                slide.setTargetPosition(0);
                slide.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                slide.setPower(-retractPower);
                while(slide.getCurrentPosition() > slide.getTargetPosition()) {
                    idle();
                }
            }
            telemetry.addData("Position: ", slide.getCurrentPosition());
            telemetry.update();
        }
    }
}
