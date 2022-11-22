package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Gamepad;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

@Autonomous
public class DashboardTesting extends LinearOpMode {

    private static void logGamepad(Telemetry telemetry, Gamepad gamepad, String prefix) {
        telemetry.addData(prefix + "Synthetic", gamepad.getGamepadId() == Gamepad.ID_UNASSOCIATED);
        for(Field field : gamepad.getClass().getFields()) {
            if(Modifier.isStatic(field.getModifiers())) continue;

            try {
                telemetry.addData(prefix + field.getName(), field.get(gamepad));
            }
            catch (IllegalAccessException ex) {

            }
        }

    }

    @Override
    public void runOpMode() throws InterruptedException {
        FtcDashboard dashboard = FtcDashboard.getInstance();
        telemetry = new MultipleTelemetry(telemetry, dashboard.getTelemetry());

        waitForStart();

        if(isStopRequested()) return;

        while(opModeIsActive()) {
            logGamepad(telemetry, gamepad1, "gamepad1");
            telemetry.update();

            sleep(100);
        }
    }

}
