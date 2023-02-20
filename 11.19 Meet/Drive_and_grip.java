package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@TeleOp
@Deprecated
public class Drive_and_grip extends LinearOpMode {

   private DcMotor fl;
    private DcMotor fr;
    private DcMotor rl;
    private DcMotor rr;
    private DcMotor slide;
    private DcMotor intake;
    private DcMotor four;
    
    @Override
    public void runOpMode() {
        fl = hardwareMap.get(DcMotor.class, "fl");
        fr = hardwareMap.get(DcMotor.class, "fr");
        rl = hardwareMap.get(DcMotor.class, "rl");
        rr = hardwareMap.get(DcMotor.class, "rr");
        slide = hardwareMap.get(DcMotor.class, "slide");
        intake = hardwareMap.get(DcMotor.class, "intake");
        four = hardwareMap.get(DcMotor.class, "four");
        
        
        fl.setDirection(DcMotor.Direction.REVERSE);
        fr.setDirection(DcMotor.Direction.REVERSE);
        rr.setDirection(DcMotor.Direction.REVERSE);
        
        waitForStart();
        while(opModeIsActive()) {
            //Left Stick: Strafing driving
            fl.setPower(gamepad1.left_stick_y);
            fr.setPower(gamepad1.left_stick_y);
            rr.setPower(-gamepad1.left_stick_y);
            rl.setPower(-gamepad1.left_stick_y);
            
            fl.setPower(-gamepad1.left_stick_x);
            fr.setPower(gamepad1.left_stick_x);
            rr.setPower(gamepad1.left_stick_x);
            rl.setPower(-gamepad1.left_stick_x);
            
            //Right Stick: Turning
            fl.setPower(-gamepad1.right_stick_x);
            fr.setPower(gamepad1.right_stick_x);
            rr.setPower(-gamepad1.right_stick_x);
            rl.setPower(gamepad1.right_stick_x);
            
            //intake
            if (gamepad1.left_trigger > 0) {
                intake.setPower(.5);
            } else if (gamepad1.right_trigger > 0) {
                intake.setPower(-.5);
            } else if (gamepad1.right_bumper) {
                intake.setPower(0);
            }
            
            //slide
            if (gamepad1.x) {
                slide.setPower(1);
            } else if (gamepad1.y) {
                slide.setPower(-1);
            } else {
                slide.setPower(0);
            }
            
            //4bar
            if (gamepad1.a) {
                four.setPower(2);
            } else if (gamepad1.b) {
                four.setPower(-2);
            } else {
                four.setPower(-.4);
            }
        }
    }
}
