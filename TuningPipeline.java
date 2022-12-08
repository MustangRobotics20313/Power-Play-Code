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

@Config
public class TuningPipeline extends OpenCvPipeline {

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

        public int cvtCode;
        ColorSpace(int cvtCode) {this.cvtCode = cvtCode;}
    }
    //initializes the type of colorspace
    public static ColorSpace colorSpace = ColorSpace.HSV;

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

    //dimension of region
    public static int REGION_WIDTH = 100;

    //points for rectangle
    private static Point anchor;
    private static Point pointB;
    public static int p1x = 0;
    public static int p1y = 0;

    //color constants
    private static final Scalar GREEN = new Scalar(0, 255, 0);
    private static final Scalar BLACK = new Scalar(0, 0, 0);
    private static final Scalar MAGENTA = new Scalar(255, 0, 255);

    //percentage declarations
    private double greenPercent;
    private double blackPercent;
    private double magentaPercent;
    private double maximum;

    //mat declarations
    private Mat binaryMat = new Mat();
    private Mat maskedInputMat = new Mat();
    private Mat greenMat = new Mat();
    private Mat blackMat = new Mat();
    private Mat magentaMat = new Mat();
    private Mat region = new Mat();

    private Telemetry telemetry;

    //blank constructor
    public TuningPipeline(Telemetry t) {telemetry = t;}

    //channel switcher
    public static int channelSwitch;

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

        Core.inRange(region, lower, upper, magentaMat);
        Core.inRange(region, lower, upper, greenMat);
        Core.inRange(region, lower, upper, blackMat);



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
}
