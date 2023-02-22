package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.IMU;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;

@Config
@SuppressWarnings({"unused", "FieldCanBeLocal"})
@TeleOp
public class IMUTesting extends LinearOpMode {

    private IMU imu;
    private YawPitchRollAngles robotOrientation;


    @Override
    public void runOpMode() {
        FtcDashboard dashboard = FtcDashboard.getInstance();
        telemetry = new MultipleTelemetry(telemetry, dashboard.getTelemetry());

        telemetry.setMsTransmissionInterval(100);

        imu = hardwareMap.get(IMU.class, "imu");

        IMU.Parameters myImuParameters = new IMU.Parameters (
            new RevHubOrientationOnRobot (
                    new Orientation(
                    AxesReference.INTRINSIC,
                    AxesOrder.XYZ,
                    AngleUnit.DEGREES,
                    0,
                    45,
                    -90,
                    0
                )
            )
        );

        imu.initialize(myImuParameters);

        waitForStart();
        while(opModeIsActive()) {
            robotOrientation = imu.getRobotYawPitchRollAngles();

            telemetry.addData("Yaw\t:", robotOrientation.getYaw(AngleUnit.DEGREES)); //vertical axis of robot
            telemetry.addData("Pitch\t:", robotOrientation.getPitch(AngleUnit.DEGREES)); //front-back axis of robot
            telemetry.addData("Roll\t:", robotOrientation.getRoll(AngleUnit.DEGREES)); // left-right axis of robot

            telemetry.update();
            sleep(100);
        }


    }
}
