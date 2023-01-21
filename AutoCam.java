package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

@SuppressWarnings({"unused", "FieldCanBeLocal"})
@Autonomous
public class AutoCam extends LinearOpMode {
    //camera declarations
    private OpenCvCamera webcam;
    private TuningPipeline pipeline;
    //state variable that stores the state of the signal sleeve
    TuningPipeline.Signal sig = TuningPipeline.Signal.NULL;

    //actuator declarations
    private DcMotor fl;
    private DcMotor fr;
    private DcMotor rl;
    private DcMotor rr;
    private DcMotor slide;
    private Servo grabber;

    @Override
    public void runOpMode() {
        //allows telemetry to be sent to FTC Dashboard
        FtcDashboard dashboard = FtcDashboard.getInstance();
        telemetry = new MultipleTelemetry(telemetry, dashboard.getTelemetry());

        //camera initialization
        WebcamName webcamName = hardwareMap.get(WebcamName.class, "webcam");
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId","id", hardwareMap.appContext.getPackageName());
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

        //actuator configurations
        rr.setDirection(DcMotor.Direction.REVERSE);
        grabber.scaleRange(0, 1);

        //telemetry loop while waiting to be started
        while(!isStarted() && !isStopRequested()) {
            telemetry.addData("Analysis: ", pipeline.getAnalysis());
            telemetry.update();
            sleep(1000);
        }

        //stores final state of the pipeline's analysis
        sig = pipeline.getAnalysis();
        telemetry.addData("Final analysis: ", sig);
        telemetry.update();

        //grips the cone with the servo
        switch(sig) {
            case LEFT:
                //code to strafe right relative to robot
                fl.setPower(-0.5);
                fr.setPower(0.5);
                rl.setPower(0.5);
                rr.setPower(-0.5);
                sleep(1325);
                //sets all power forward
                allPower(0.5);
                sleep(1450);
                allPower(0);
                break;
            case CENTER:
                //strafes slightly right relative to robot to avoid hitting ground junction
                fl.setPower(-0.5);
                fr.setPower(0.5);
                rl.setPower(0.5);
                rr.setPower(-0.5);
                sleep(300);
                //sets all power forward
                allPower(0.5);
                sleep(1450);
                allPower(0);
                break;
            case RIGHT:
                //code to strafe left relative to robot
                fl.setPower(0.5);
                rr.setPower(0.5);
                rl.setPower(-0.5);
                fr.setPower(-0.5);
                sleep(900);
                //sets all power forward
                allPower(0.5);
                sleep(1200);
                allPower(0);
                break;
        }
    }

    //helper method that sets all motors to same power to avoid repetitive code
    private void allPower(double power) {
        fl.setPower(power);
        fr.setPower(power);
        rl.setPower(power);
        rr.setPower(power);
    }
}