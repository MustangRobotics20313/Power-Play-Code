package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

@SuppressWarnings({"unused", "FieldCanBeLocal"})
@Autonomous
public class CameraTuning extends LinearOpMode {
    private OpenCvCamera webcam;
    private TuningPipeline pipe;

    @SuppressWarnings("StatementWithEmptyBody")
    public void runOpMode() {
        //creates a pointer to the FTC dashboard instance
        FtcDashboard dashboard = FtcDashboard.getInstance();

        //creates a telemetry object that can send telemetry to Driver Station and FTC dashboard
        telemetry = new MultipleTelemetry(telemetry, dashboard.getTelemetry());

        //instantiates pipeline
        pipe = new TuningPipeline(telemetry);

        //initializes the webcam
        WebcamName webcamName = hardwareMap.get(WebcamName.class, "webcam");
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        webcam = OpenCvCameraFactory.getInstance().createWebcam(webcamName, cameraMonitorViewId);
        webcam.setPipeline(pipe);

        //start streaming
        webcam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                webcam.startStreaming(320, 240, OpenCvCameraRotation.UPRIGHT);
                dashboard.startCameraStream(webcam, 0);
            }
            @Override
            public void onError(int errorCode) {}
        });

        while(!isStarted() && !isStopRequested()) {}

        telemetry.addData("Program is done", cameraMonitorViewId);
        telemetry.update();
        sleep(5000);

    }
}
