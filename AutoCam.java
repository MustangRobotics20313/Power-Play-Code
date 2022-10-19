package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvWebcam;
import org.openftc.easyopencv.OpenCvInternalCamera;
import org.firstinspires.ftc.robotcore.external.Telemetry;

@Autonomous
public class AutoCam extends LinearOpMode {
    //camera declarations
    private OpenCvCamera webcam;
    private Pipeline pipeline;
    Pipeline.Signal snapshotAnalysis = Pipeline.Signal.GREEN;
    
    
    //motor declarations
    private DcMotor fl;
    private DcMotor fr;
    private DcMotor rl;
    private DcMotor rr;
    
    @Override
    public void runOpMode() {
        //camera initializations
        WebcamName webcamName = hardwareMap.get(WebcamName.class, "webcam");
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        webcam = OpenCvCameraFactory.getInstance().createWebcam(webcamName, cameraMonitorViewId);
        pipeline = new Pipeline(telemetry);
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
        //setting motor directions
        fl.setDirection(DcMotor.Direction.REVERSE);
        rr.setDirection(DcMotor.Direction.REVERSE);
        
        while(!isStarted() && !isStopRequested()) {}
        
        snapshotAnalysis = pipeline.getAnalysis();
        telemetry.addData("Final analysis: ", snapshotAnalysis);
        telemetry.update();
            
        switch(snapshotAnalysis) {
            case GREEN:
                fl.setPower(-0.5);
                rr.setPower(0.5);
                rl.setPower(-0.5);
                fr.setPower(0.5);
                sleep(1325);
                allPower(fl, fr, rl, rr, 0.5);
                sleep(1600);
                allPower(fl, fr, rl, rr, 0);
                break;
            case BLACK:
                fl.setPower(0.5);
                fr.setPower(0.5);
                rl.setPower(-0.5);
                rr.setPower(-0.5);
                sleep(1600);
                allPower(fl, fr, rl, rr, 0);
                break;
            case PURPLE:
                fl.setPower(0.5);
                rr.setPower(-0.5);
                rl.setPower(0.5);
                fr.setPower(-0.5);
                sleep(1325);
                allPower(fl, fr, rl, rr, 0.5);
                sleep(1600);
                allPower(fl, fr, rl, rr, 0);
                break;
        }
        
    }
    
    private void allPower(DcMotor fl, DcMotor fr, DcMotor rl, DcMotor rr, double p) {
        fl.setPower(p);
        fr.setPower(p);
        rl.setPower(-p);
        rr.setPower(-p);
    }
}