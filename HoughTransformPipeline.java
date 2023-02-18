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
public class HoughTransformPipeline extends OpenCvPipeline {

    //enum of the corresponding positions to signal images
    public enum Signal {
        LEFT, //green
        CENTER, //black
        RIGHT, //magenta
        NULL, //nothing
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
    //initializes the type of colorspace
    public static ColorSpace colorSpace = ColorSpace.YCrCb;

    //upper and lower scalars
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

    //color boundary Scalars
    private static Scalar magenta_l = new Scalar(125, 125, 125);
    private static Scalar magenta_u = new Scalar(168, 168, 168);
    private static Scalar green_l = new Scalar(100, 70, 70);
    private static Scalar green_u = new Scalar(200, 130, 130);
    private static Scalar black_l = new Scalar(50, 125, 120);
    private static Scalar black_u = new Scalar(150, 130, 130);

    //dimension of region
    public static int REGION_WIDTH = 20;

    //points for rectangle
    private static Point anchor;
    private static Point pointB;
    public static int p1x = 65;
    public static int p1y = 125;

    //color constants
    private static final Scalar GREEN = new Scalar(0, 255, 0);
    private static final Scalar BLACK = new Scalar(0, 0, 0);
    private static final Scalar MAGENTA = new Scalar(255, 0, 255);

    //mat declarations
    private Mat binaryMat = new Mat();
    private Mat maskedInputMat = new Mat();
    private Mat greenMat = new Mat();
    private Mat blackMat = new Mat();
    private Mat magentaMat = new Mat();
    private Mat region = new Mat();
    private Mat regionMat = new Mat();

    private Telemetry telemetry;

    //blank constructor
    public HoughTransformPipeline(Telemetry t) {telemetry = t;}

    //channel switcher
    public static int channelSwitch;
    public static int regionThresholdSwitch;

    @Override
    public Mat processFrame(Mat input) {
        upper = new Scalar(u1, u2, u3);
        lower = new Scalar(l1, l2, l3);

        anchor = new Point(p1x, p1y);
        pointB = new Point(anchor.x + REGION_WIDTH, anchor.y + REGION_WIDTH);

        Imgproc.cvtColor(input, input, colorSpace.cvtCode);

        Core.inRange(input, lower, upper, binaryMat);
        Core.bitwise_and(input, input, maskedInputMat, binaryMat);

        region = input.submat(new Rect(anchor, pointB));

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

        //Imgproc.Canny();
        //

        telemetry.addData("black count: ", Core.countNonZero(blackMat));
        telemetry.addData("magenta count: ", Core.countNonZero(magentaMat));
        telemetry.addData("green count: ", Core.countNonZero(greenMat));
        telemetry.update();

        Imgproc.rectangle(input, anchor, pointB, BLACK, 2);
        Imgproc.rectangle(maskedInputMat, anchor, pointB, BLACK, 2);

        switch(channelSwitch) {
            case 1:
                region.release();
                greenMat.release();
                blackMat.release();
                magentaMat.release();
                binaryMat.release();
                return maskedInputMat;
            case 2:
                greenMat.release();
                blackMat.release();
                magentaMat.release();
                binaryMat.release();
                maskedInputMat.release();
                return region;
            case 3:
                region.release();
                blackMat.release();
                magentaMat.release();
                binaryMat.release();
                maskedInputMat.release();
                return greenMat;
            case 4:
                region.release();
                greenMat.release();
                magentaMat.release();
                binaryMat.release();
                maskedInputMat.release();
                return blackMat;
            case 5:
                region.release();
                greenMat.release();
                blackMat.release();
                binaryMat.release();
                maskedInputMat.release();
                return magentaMat;
            case 6:
                region.release();
                greenMat.release();
                blackMat.release();
                magentaMat.release();
                maskedInputMat.release();
                return binaryMat;
            case 0:
            default:
                region.release();
                greenMat.release();
                blackMat.release();
                magentaMat.release();
                maskedInputMat.release();
                binaryMat.release();
                return input;
        }
    }

    public Signal getAnalysis() {
        return sig;
    }
}
