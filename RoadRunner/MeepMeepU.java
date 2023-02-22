package org.firstinspires.ftc.teamcode.RoadRunner;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepU {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);

        final double MAX_VEL = 30;
        final double MAX_ACC = 40;
        final double TRACK_WIDTH = 12;

        final double STARTING_X = 31.3;
        final double STARTING_Y = -64;
        final double INITIAL_STRAFE = 19;
        final double WALL_FORWARD = 30;
        final double ANGLE = 49; //degrees
        final double CONE_FORWARD = 5;

        final double STRAFE_TO_PARK_MIDDLE = 25;
        final double STRAFE_TO_PARK_RIGHT = 47;

        //parkLeft trajectory
        RoadRunnerBotEntity leftBot = new DefaultBotBuilder(meepMeep)
                .setConstraints(MAX_VEL, MAX_ACC, Math.toRadians(180), Math.toRadians(180), TRACK_WIDTH)
                .setDimensions(14, 16) //units in inches
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(new Pose2d(STARTING_X, STARTING_Y, Math.toRadians(90)))
                                .strafeLeft(INITIAL_STRAFE)
                                .forward(WALL_FORWARD)
                                .turn(Math.toRadians(ANGLE))
                                .forward(CONE_FORWARD)
                                .back(CONE_FORWARD)
                                .turn(Math.toRadians(-ANGLE))
                                .build()
                );

        //parkCenter trajectory
        RoadRunnerBotEntity centerBot = new DefaultBotBuilder(meepMeep)
                .setConstraints(MAX_VEL, MAX_ACC, Math.toRadians(180), Math.toRadians(180), TRACK_WIDTH)
                .setDimensions(14, 16)
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(new Pose2d(STARTING_X, STARTING_Y, Math.toRadians(90)))
                                .strafeLeft(INITIAL_STRAFE)
                                .forward(WALL_FORWARD)
                                .turn(Math.toRadians(ANGLE))
                                .forward(CONE_FORWARD)
                                .back(CONE_FORWARD)
                                .turn(Math.toRadians(-ANGLE))
                                .strafeRight(STRAFE_TO_PARK_MIDDLE)
                                .build()
                );

        RoadRunnerBotEntity rightBot = new DefaultBotBuilder(meepMeep)
                .setConstraints(MAX_VEL, MAX_ACC, Math.toRadians(180), Math.toRadians(180), TRACK_WIDTH)
                .setDimensions(14, 16)
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(new Pose2d(STARTING_X, STARTING_Y, Math.toRadians(90)))
                                .strafeLeft(INITIAL_STRAFE)
                                .forward(WALL_FORWARD)
                                .turn(Math.toRadians(ANGLE))
                                .forward(CONE_FORWARD)
                                .back(CONE_FORWARD)
                                .turn(Math.toRadians(-ANGLE))
                                .strafeRight(STRAFE_TO_PARK_RIGHT)
                                .build()
                );


        /*meepMeep.setBackground(MeepMeep.Background.FIELD_POWERPLAY_OFFICIAL)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(leftBot)
                .addEntity(centerBot)
                .addEntity(rightBot)
                .start();*/
    }
}