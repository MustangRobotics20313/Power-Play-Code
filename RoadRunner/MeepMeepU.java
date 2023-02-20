package org.firstinspires.ftc.teamcode.RoadRunner;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepU {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);

        //parkLeft trajectory
        RoadRunnerBotEntity leftBot = new DefaultBotBuilder(meepMeep)
                .setConstraints(45, 60, Math.toRadians(180), Math.toRadians(180), 12)
                .setDimensions(14, 14)
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(new Pose2d(28, -62, Math.toRadians(90)))
                                .strafeLeft(17)
                                .forward(28)
                                .turn(Math.toRadians(42))
                                .forward(3)
                                .back(3)
                                .turn(Math.toRadians(-42))
                                .build()
                );

        //parkCenter trajectory
        RoadRunnerBotEntity centerBot = new DefaultBotBuilder(meepMeep)
                .setConstraints(45, 60, Math.toRadians(180), Math.toRadians(180), 12)
                .setDimensions(14, 14)
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(new Pose2d(28, -62, Math.toRadians(90)))
                                .strafeLeft(17)
                                .forward(28)
                                .turn(Math.toRadians(45))
                                .forward(3)
                                .back(3)
                                .turn(Math.toRadians(-45))
                                .strafeRight(25)
                                .build()
                );

        RoadRunnerBotEntity rightBot = new DefaultBotBuilder(meepMeep)
                .setConstraints(45, 60, Math.toRadians(180), Math.toRadians(180), 12)
                .setDimensions(14, 14)
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(new Pose2d(28, -62, Math.toRadians(90)))
                                .strafeLeft(17)
                                .forward(28)
                                .turn(Math.toRadians(45))
                                .forward(3)
                                .back(3)
                                .turn(Math.toRadians(-45))
                                .strafeRight(47)
                                .build()
                );


        meepMeep.setBackground(MeepMeep.Background.FIELD_POWERPLAY_KAI_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(leftBot)
                .addEntity(centerBot)
                .addEntity(rightBot)
                .start();
    }
}