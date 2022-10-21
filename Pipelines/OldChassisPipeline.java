package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

@Disabled
public class OldChassisPipeline extends OpenCvPipeline {

    //position enum
    public enum Signal
    {
        GREEN,
        BLACK,
        PURPLE
    }

    //colorspace enum
    private enum ColorSpace {
        /*
         * Define our "conversion codes" in the enum
         * so that we don't have to do a switch
         * statement in the processFrame method.
         */
        RGB(Imgproc.COLOR_RGBA2RGB),
        HSV(Imgproc.COLOR_RGB2HSV),
        YCrCb(Imgproc.COLOR_RGB2YCrCb),
        Lab(Imgproc.COLOR_RGB2Lab);

        //store cvtCode in a public var
        public int cvtCode = 0;

        //constructor to be used by enum declarations above
        ColorSpace(int cvtCode) {
            this.cvtCode = cvtCode;
        }
    }


    public Scalar upper = new Scalar(255, 255, 255);
    public Scalar lower = new Scalar(0, 0, 0);

    public ColorSpace colorSpace = ColorSpace.YCrCb;
    //color constants
    public final Scalar BLUE = new Scalar(0, 0, 255);
    public final Scalar GREEN = new Scalar(0, 255, 0);

    //top left points of each box
    //x is measured from left 0 to right 320, y is measured from top 0 to bottom 240
    static final Point ANCHOR_POINT = new Point(165,125);
    
    //box dimensions
    static final int REGION_WIDTH = 20;
    static final int REGION_HEIGHT = 20;

    //top left and bottom right points
    Point pointA = new Point(
            ANCHOR_POINT.x,
            ANCHOR_POINT.y);
    Point pointB = new Point(
            ANCHOR_POINT.x + REGION_WIDTH,
            ANCHOR_POINT.y + REGION_HEIGHT);

    //image objects and average ints
    Mat region_Cb;
    Mat YCrCb = new Mat();
    private Mat binaryMat = new Mat();
    private Mat maskedInputMat = new Mat();
    Mat Cb = new Mat();
    int sum;

    //position variable
    private volatile Signal sig = Signal.GREEN;

    //telemetry, allows telemetry in EOCVSim
    private Telemetry telemetry;

    //constructor for telemetry
    public OldChassisPipeline(Telemetry telemetry) {
        this.telemetry = telemetry;
    }

    //converts RGB to HSV, extracts the Cb channel to Cb variable
    void inputToCb(Mat input)
    {
        Imgproc.cvtColor(input, YCrCb, Imgproc.COLOR_RGB2HSV);
        Core.extractChannel(YCrCb, Cb, 2);
    }


    //initializes the image
    @Override
    public void init(Mat firstFrame)
    {
        //initializes the image
        inputToCb(firstFrame);

        //subimages of what's inside each box
        region_Cb = Cb.submat(new Rect(pointA, pointB));
    }


    //image processor
    @Override
    public Mat processFrame(Mat input)
    {
        //refreshes new input
        inputToCb(input);
        Core.inRange(YCrCb, lower, upper, binaryMat);

        maskedInputMat.release();

        Core.bitwise_and(input, input, maskedInputMat, binaryMat);
        //averages the box
        sum = (int) Core.sumElems(region_Cb).val[0];

        

        //draws rectangle for each box
        //region 1
        Imgproc.rectangle(
                maskedInputMat, //image to draw on
                pointA, //top left of the box
                pointB, //bottom right of the box
                BLUE, //rectangle color
                2); //thickness of rectangle


        //sets sig to whichever side of cone is used
        if(sum > 0 && sum < 30000)
        {
            sig = Signal.BLACK;
        }
        else if(sum >= 30000 && sum <= 115000)
        {
            sig = Signal.GREEN;

        }
        else if(sum > 200)
        {
            sig = Signal.PURPLE;
        }

        telemetry.addData("Pattern", sig);
        telemetry.addData("sum", sum);
        telemetry.update();

        //sends the image to EOCVsim
        return maskedInputMat;
    }

    //function called in opmode to get the position
    public Signal getAnalysis()
    {
        return sig;
    }
}
