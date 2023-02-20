package org.firstinspires.ftc.teamcode.ModuleTesting;

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

@TeleOp
@Config
@SuppressWarnings({"unused", "FieldCanBeLocal"})
public class IMUTesting extends LinearOpMode {

    private IMU imu;

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
                    AxesOrder.ZYX,
                    AngleUnit.DEGREES,
                    -90,
                    45,
                    0,
                    0
                )
            )
        );

        imu.initialize(myImuParameters);

        Orientation robotOrientation = imu.getRobotOrientation(
                AxesReference.INTRINSIC,
                AxesOrder.XYZ,
                AngleUnit.DEGREES
        );

        waitForStart();
        while(opModeIsActive()) {



            telemetry.update();


            sleep(100);
        }


    }
}
