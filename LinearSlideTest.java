package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@SuppressWarnings({"unused", "FieldCanBeLocal"})
@TeleOp
public class LinearSlideTest extends LinearOpMode {

    DcMotor arm;
    
    @Override
    public void runOpMode() {
        arm = hardwareMap.get(DcMotor.class, "arm");
        
        waitForStart();
        while(opModeIsActive()) {
            if (gamepad1.left_trigger > 0) {
                arm.setPower(1.5);
            } else if (gamepad1.right_trigger > 0) {
                arm.setPower(-0.5);
            }
        }
    }
}