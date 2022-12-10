package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.dashboard.FtcDashboard;

@Autonomous
public class AutonomousCamera extends LinearOpMode {
    //camera declarations
    private OpenCvCamera webcam;
    private TuningPipeline pipeline;
    TuningPipeline.Signal sig = TuningPipeline.Signal.NULL;

    private DcMotor fl;
    private DcMotor fr;
    private DcMotor rl;
    private DcMotor rr;

    @Override
    public void runOpMode() {
        FtcDashboard dashboard = FtcDashboard.getInstance();
        telemetry = new MultipleTelemetry(telemetry, dashboard.getTelemetry());

        //camera initializations
        WebcamName webcamName = hardwareMap.get(WebcamName.class, "webcam");
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId","id", hardwareMap.appContext.getPackageName());
        webcam = OpenCvCameraFactory.getInstance().createWebcam(webcamName, cameraMonitorViewId);
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

        //motor initializations
        fl = hardwareMap.get(DcMotor.class, "fl");
        fr = hardwareMap.get(DcMotor.class, "fr");
        rl = hardwareMap.get(DcMotor.class, "rl");
        rr = hardwareMap.get(DcMotor.class, "rr");
        fl.setDirection(DcMotor.Direction.REVERSE);
        rr.setDirection(DcMotor.Direction.REVERSE);

        while(!isStarted() && !isStopRequested()) {
            telemetry.addData("Analysis: ", pipeline.getAnalysis());
            telemetry.update();
            sleep(1000);
        }

        sig = pipeline.getAnalysis();
        telemetry.addData("Final analysis: ", sig);
        telemetry.update();

        switch(sig) {
            case LEFT:
                //code to strafe right
                fl.setPower(0.5);
                fr.setPower(-0.5);
                rl.setPower(0.5);
                rr.setPower(-0.5);
                sleep(1650);
                allPower(-0.5);
                sleep(1600);
                allPower(0);
                break;
            case CENTER:
                fl.setPower(0.5);
                fr.setPower(-0.5);
                rl.setPower(0.5);
                rr.setPower(-0.5);
                sleep(300);
                allPower(-0.5);
                sleep(1600);
                allPower(0);
                break;
            case RIGHT:
                //code to strafe left
                fl.setPower(-0.5);
                rr.setPower(0.5);
                rl.setPower(-0.5);
                fr.setPower(0.5);

                sleep(1500);
                allPower(-0.5);
                sleep(1600);
                allPower(0);
                break;
        }
    }

    private void allPower(double power) {
        fl.setPower(power);
        fr.setPower(power);
        rl.setPower(-power);
        rr.setPower(-power);
    }

}
