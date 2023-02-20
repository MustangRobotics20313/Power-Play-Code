package org.firstinspires.ftc.teamcode.Autonomous;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.TuningPipeline;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.dashboard.FtcDashboard;

@Autonomous
@Config
@Deprecated
@SuppressWarnings({"unused", "FieldCanBeLocal", "SameParameterValue"})
public class AutonomousWithJunction extends LinearOpMode {
    //actuator declarations
    private DcMotor fl;
    private DcMotor fr;
    private DcMotor rl;
    private DcMotor rr;
    private DcMotorEx slide;
    private Servo grabber;

    //camera declarations
    private OpenCvCamera webcam;
    private TuningPipeline pipeline;
    TuningPipeline.Signal sig = TuningPipeline.Signal.NULL;


    //tuning variables
    public static int forwardTime;
    public static double rotationPower;
    public static int rotationTime;
    public static int strafePower;
    public static int strafeTime;

    @Override
    public void runOpMode() {
        //dashboard and telemetry setup
        FtcDashboard dashboard = FtcDashboard.getInstance();
        telemetry = new MultipleTelemetry(telemetry, dashboard.getTelemetry());

        //camera initializations
        WebcamName webcamName = hardwareMap.get(WebcamName.class, "webcam");
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        webcam = OpenCvCameraFactory.getInstance().createWebcam(webcamName, cameraMonitorViewId);
        pipeline = new TuningPipeline(telemetry);
        webcam.setPipeline(pipeline);
        webcam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                webcam.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT);
                dashboard.startCameraStream(webcam, 0);
            }

            @Override
            public void onError(int errorCode) {}
        });

        //actuator initializations
        fl = hardwareMap.get(DcMotor.class, "fl");
        fr = hardwareMap.get(DcMotor.class, "fr");
        rl = hardwareMap.get(DcMotor.class, "rl");
        rr = hardwareMap.get(DcMotor.class, "rr");
        slide = hardwareMap.get(DcMotorEx.class, "slide");
        grabber = hardwareMap.get(Servo.class, "grabber");

        //actuator configurations
        rr.setDirection(DcMotor.Direction.REVERSE);
        grabber.scaleRange(0, 1);
        slide.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);

        //continually updates the camera analysis while waiting to be started
        while(!isStarted() && !isStopRequested()) {
            telemetry.addData("Analysis: ", pipeline.getAnalysis());
            telemetry.addData("Slide position: ", slide.getCurrentPosition());
            telemetry.update();
            sleep(1000);
        }

        telemetry.addData("Final analysis: ", pipeline.getAnalysis());
        telemetry.update();

        //begin motion code
        //grab preload cone
        grabber.setPosition(0);
        while(grabber.getPosition() != 0) { idle(); }

        //barely lift the slide
        slide.setTargetPosition(100);
        slide.setPower(0.5);

        //strafe left briefly to center the robot in the middle lane
        strafeLeft(0.5, 300);
        //forward (or backward, i guess) down the center lane to reach the middle junction
        allPower(0.5, forwardTime);
        allPower(0, 1500);

        //rotation towards the middle junction
        rotateRight(rotationPower, rotationTime);
        allPower(0, 1000);

        //raise slide
        configureSlide();
        slide.setPower(0.7);
        while(slide.getCurrentPosition() < slide.getTargetPosition()) {
            telemetry.addData("Slide position: ", slide.getCurrentPosition());
            telemetry.update();
            sleep(100);
        }

        //inch forward
        allPower(-0.2, 400);
        allPower(0, 1500);

        //open servo
        grabber.setPosition(0.5);
        //retract slide
        allPower(0.2, 400);
        slide.setMode(DcMotorEx.RunMode.RUN_WITHOUT_ENCODER);
        slide.setPower(-0.3);
        while(slide.getCurrentPosition() > 0) { idle(); }
        rotateLeft(rotationPower, rotationTime);
        allPower(0, 2000);

        switch(sig) {
            case LEFT:
                //strafes to the right to get into the left section, as robot is still facing backwards
                strafeRight(strafePower, strafeTime);
                allPower(0);
                break;
            case CENTER:
                break;
            case RIGHT:
                //strafes to the left to get into the right section, as robot is still facing backwards
                strafeLeft(strafePower, strafeTime);
                break;
        }
    }

    private void strafeLeft(double power, int sleepTime) {
        fl.setPower(-power);
        fr.setPower(power);
        rl.setPower(power);
        rr.setPower(-power);
        sleep(sleepTime);
    }

    private void strafeRight(double power, int sleepTime) {
        fl.setPower(power);
        fr.setPower(-power);
        rl.setPower(-power);
        rr.setPower(-power);
        sleep(sleepTime);
    }

    private void rotateRight(double power, int sleepTime) {
        fl.setPower(-power);
        rl.setPower(-power);
        fr.setPower(power);
        rr.setPower(power);
        sleep(sleepTime);
    }

    private void rotateLeft(double power, int sleepTime) {
        rotateRight(-power, sleepTime);
    }

    private void allPower(double power, int sleepTime) {
        allPower(power);
        sleep(sleepTime);
    }

    private void allPower(double power) {
        fl.setPower(power);
        fr.setPower(power);
        rl.setPower(power);
        rr.setPower(power);
    }

    private void configureSlide() {
        slide.setTargetPosition(4136);
        slide.setTargetPositionTolerance(50);
        slide.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);

    }
}
