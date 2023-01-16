package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraRotation;

@SuppressWarnings({"unused", "FieldCanBeLocal"})
@Config
@Autonomous
public class AutonomousTuning extends LinearOpMode {
    private DcMotor fl;
    private DcMotor fr;
    private DcMotor rl;
    private DcMotor rr;
    private DcMotor slide;
    private Servo grabber;

    private OpenCvCamera webcam;
    private TuningPipeline pipeline;
    TuningPipeline.Signal sig = TuningPipeline.Signal.NULL;

    //tuning variables
    public static int strafingTime;
    public static int strafingPower;
    public static int forwardTime;
    public static int forwardPower;
    public static int goForward;

    public void runOpMode() {
        FtcDashboard dashboard = FtcDashboard.getInstance();
        telemetry = new MultipleTelemetry(telemetry, dashboard.getTelemetry());

        WebcamName webcamName = hardwareMap.get(WebcamName.class, "webcam");
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        pipeline = new TuningPipeline(telemetry);
        webcam.setPipeline(pipeline);
        webcam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                webcam.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT);
            }
            @Override
            public void onError(int errorCode) {}
        });

        fl = hardwareMap.get(DcMotor.class, "fl");
        fr = hardwareMap.get(DcMotor.class, "fr");
        rl = hardwareMap.get(DcMotor.class, "rl");
        rr = hardwareMap.get(DcMotor.class, "rr");
        slide = hardwareMap.get(DcMotor.class, "slide");
        grabber = hardwareMap.get(Servo.class, "grabber");

        grabber.scaleRange(0, 1);
        rr.setDirection(DcMotor.Direction.REVERSE);

        while(!isStarted() && !isStopRequested()) {
            telemetry.addData("Analysis: ", pipeline.getAnalysis());
            telemetry.update();
            sleep(1000);
        }

        sig = pipeline.getAnalysis();
        telemetry.addData("Final analysis: ", sig);
        telemetry.update();

        //insert code for putting preloaded cone on high junction

        switch(sig) {
            case LEFT:
                //fl & rr positive, fr & rl negative
                fl.setPower(strafingPower);
                fr.setPower(-strafingPower);
                rl.setPower(-strafingPower);
                rr.setPower(strafingPower);
                sleep(strafingTime);

                if (goForward != 0) {
                    allPower(forwardPower);
                    sleep(forwardTime);
                }

                allPower(0);
                break;
            case CENTER:
                allPower(forwardPower);
                sleep(forwardTime);
                allPower(0);
                break;
            case RIGHT:
                fl.setPower(-strafingPower);
                fr.setPower(strafingPower);
                rl.setPower(strafingPower);
                rr.setPower(-strafingPower);
                sleep(strafingTime);

                if (goForward != 0) {
                    allPower(forwardPower);
                    sleep(forwardTime);
                }

                allPower(0);
                break;
        }
    }

    private void allPower(double power) {
        fl.setPower(power);
        fr.setPower(power);
        rl.setPower(power);
        rr.setPower(power);
    }
}