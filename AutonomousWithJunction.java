package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.dashboard.FtcDashboard;

@Autonomous
@Config
@SuppressWarnings({"unused", "FieldCanBeLocal", "SameParameterValue"})
public class AutonomousWithJunction extends LinearOpMode {
    //actuator declarations
    private DcMotor fl;
    private DcMotor fr;
    private DcMotor rl;
    private DcMotor rr;
    private DcMotor slide;
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
        slide = hardwareMap.get(DcMotor.class, "slide");
        grabber = hardwareMap.get(Servo.class, "grabber");

        rr.setDirection(DcMotor.Direction.REVERSE);
        grabber.scaleRange(0, 1);

        while(!isStarted() && !isStopRequested()) {
            telemetry.addData("Analysis: ", pipeline.getAnalysis());
            telemetry.update();
            sleep(1000);
        }

        telemetry.addData("Final analysis: ", pipeline.getAnalysis());
        telemetry.update();

        //begin motion code
        grabber.setPosition(0);
        //strafe left briefly to center the robot in the middle lane
        strafeLeft(0.5, 300);
        //forward (or backward, i guess) down the center lane to reach the middle junction
        allPower(0.5, forwardTime);
        allPower(0, 1500);

        //rotation towards the middle junction
        rotateRight(rotationPower, rotationTime);
        allPower(0, 1000);

        //raise slide

        //retract slide

        rotateLeft(rotationPower, rotationTime);
        allPower(0, 1000);

        switch(sig) {
            case LEFT:
                strafeRight(strafePower, strafeTime);
                allPower(0);
                break;
            case CENTER:
                break;
            case RIGHT:
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
        fl.setPower(power);
        rl.setPower(power);
        fr.setPower(-power);
        rr.setPower(-power);
        sleep(sleepTime);
    }

    private void rotateLeft(double power, int sleepTime) {
        fl.setPower(-power);
        rl.setPower(-power);
        fr.setPower(power);
        rr.setPower(power);
        sleep(sleepTime);
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
}
