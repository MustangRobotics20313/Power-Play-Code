package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

@SuppressWarnings({"unused", "FieldCanBeLocal", "FieldMayBeFinal"})
@Config
public class TuningPipeline extends OpenCvPipeline {

    //enum of the corresponding positions to signal images
    public enum Signal {
        LEFT, //green
        CENTER, //black
        RIGHT, //magenta
        NULL, //empty, initial state of the sleeve
    }

    //initializes the signal variable
    private volatile Signal sig = Signal.NULL;

    public enum ColorSpace {
        RGB(Imgproc.COLOR_RGBA2RGB),
        HSV(Imgproc.COLOR_RGB2HSV),
        YCrCb(Imgproc.COLOR_RGB2YCrCb),
        Lab(Imgproc.COLOR_RGB2Lab);

        @SuppressWarnings("NonFinalFieldInEnum")
        public int cvtCode;
        ColorSpace(int cvtCode) {this.cvtCode = cvtCode;}
    }

    //initializes the type of color space
    public static ColorSpace colorSpace = ColorSpace.Lab;

    //upper and lower scalar boundaries for determining threshold values
    private static Scalar upper;
    private static Scalar lower;
    //upper config variables
    public static int u1 = 255;
    public static int u2 = 255;
    public static int u3 = 255;
    //lower config variables
    public static int l1 = 0;
    public static int l2 = 0;
    public static int l3 = 0;

    //color boundary Scalars that are used in detection
    private static Scalar magenta_l = new Scalar(70, 125, 100);
    private static Scalar magenta_u = new Scalar(135, 170, 130);
    private static Scalar green_l = new Scalar(105, 90, 120);
    private static Scalar green_u = new Scalar(185, 120, 180);
    private static Scalar black_l = new Scalar(30, 120, 130);
    private static Scalar black_u = new Scalar(140, 130, 150);

    //dimension of region
    public static int REGION_WIDTH = 20;

    //points for region square
    private static Point pointA;
    private static Point pointB;
    public static int p1x = 75;
    public static int p1y = 125;

    //color constants, used to draw rectangle on camera stream image
    private static final Scalar GREEN = new Scalar(0, 255, 0);
    private static final Scalar BLACK = new Scalar(0, 0, 0);
    private static final Scalar MAGENTA = new Scalar(255, 0, 255);

    //image declarations
    private Mat mask = new Mat();
    private Mat maskedInputMat = new Mat();
    private Mat greenMat = new Mat();
    private Mat blackMat = new Mat();
    private Mat magentaMat = new Mat();
    private Mat region = new Mat();

    private Telemetry telemetry;

    //constructor for sending telemetry to the Driver Hub & FTC Dashboard
    public TuningPipeline(Telemetry t) {telemetry = t;}

    //channel switcher
    public static int channelSwitch;
    //region threshold switcher, determines if colorMats use upper+lower bounds or preset bounds
    public static int regionThresholdSwitch;

    @Override
    public Mat processFrame(Mat input) {
        upper = new Scalar(u1, u2, u3);
        lower = new Scalar(l1, l2, l3);

        pointA = new Point(p1x, p1y);
        pointB = new Point(pointA.x + REGION_WIDTH, pointA.y + REGION_WIDTH);

        Imgproc.cvtColor(input, input, colorSpace.cvtCode);

        Core.inRange(input, lower, upper, mask);
        Core.bitwise_and(input, input, maskedInputMat, mask);

        region = input.submat(new Rect(pointA, pointB));

        switch(regionThresholdSwitch) {
            case 0:
                Core.inRange(region, magenta_l, magenta_u, magentaMat);
                Core.inRange(region, green_l, green_u, greenMat);
                Core.inRange(region, black_l, black_u, blackMat);
                break;
            case 1:
                Core.inRange(region, lower, upper, magentaMat);
                Core.inRange(region, lower, upper, greenMat);
                Core.inRange(region, lower, upper, blackMat);
                break;
        }


        if (Math.max(Core.countNonZero(greenMat), Math.max(Core.countNonZero(blackMat),
                Core.countNonZero(magentaMat))) == Core.countNonZero(greenMat)) {
            sig = TuningPipeline.Signal.LEFT;
            Imgproc.rectangle(input, pointA, pointB, GREEN);
            Imgproc.rectangle(maskedInputMat, pointA, pointB, GREEN);
        } else if (Math.max(Core.countNonZero(greenMat), Math.max(Core.countNonZero(blackMat),
                Core.countNonZero(magentaMat))) == Core.countNonZero(blackMat)) {
            sig = TuningPipeline.Signal.CENTER;
            Imgproc.rectangle(input, pointA, pointB, BLACK);
            Imgproc.rectangle(maskedInputMat, pointA, pointB, BLACK);
        } else if (Math.max(Core.countNonZero(greenMat), Math.max(Core.countNonZero(blackMat),
                Core.countNonZero(magentaMat))) == Core.countNonZero(magentaMat)) {
            sig = TuningPipeline.Signal.RIGHT;
            Imgproc.rectangle(input, pointA, pointB, MAGENTA);
            Imgproc.rectangle(maskedInputMat, pointA, pointB, MAGENTA);
        }

        telemetry.addData("black count: ", Core.countNonZero(blackMat));
        telemetry.addData("magenta count: ", Core.countNonZero(magentaMat));
        telemetry.addData("green count: ", Core.countNonZero(greenMat));
        telemetry.update();

        switch(channelSwitch) {
            case 1:
                region.release();
                greenMat.release();
                blackMat.release();
                magentaMat.release();
                mask.release();
                return maskedInputMat;
            case 2:
                greenMat.release();
                blackMat.release();
                magentaMat.release();
                mask.release();
                maskedInputMat.release();
                return region;
            case 3:
                region.release();
                blackMat.release();
                magentaMat.release();
                mask.release();
                maskedInputMat.release();
                return greenMat;
            case 4:
                region.release();
                greenMat.release();
                magentaMat.release();
                mask.release();
                maskedInputMat.release();
                return blackMat;
            case 5:
                region.release();
                greenMat.release();
                blackMat.release();
                mask.release();
                maskedInputMat.release();
                return magentaMat;
            case 6:
                region.release();
                greenMat.release();
                blackMat.release();
                magentaMat.release();
                maskedInputMat.release();
                return mask;
            case 0:
            default:
                region.release();
                greenMat.release();
                blackMat.release();
                magentaMat.release();
                maskedInputMat.release();
                mask.release();
                return input;
        }
    }

    public Signal getAnalysis() {
        return sig;
    }
}
