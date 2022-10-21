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
public class Pipeline extends OpenCvPipeline {

    //position enum
    public enum Signal
    {
        GREEN,
        BLACK,
        PURPLE
    }

    //top left point of box
    //x is measured from left 0 to right 320, y is measured from top 0 to bottom 240
    static final Point ANCHOR_POINT = new Point(190,30);
    
    //box dimensions
    static final int REGION_WIDTH = 30;
    static final int REGION_HEIGHT = 30;
    
    //color
    public final Scalar BLUE = new Scalar(0, 0, 255);

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
    Mat Cb = new Mat();
    int sum;

    //position variable
    private volatile Signal sig = Signal.GREEN;

    //telemetry, allows telemetry in EOCVSim
    private Telemetry telemetry;

    //constructor for telemetry
    public Pipeline(Telemetry telemetry) {
        this.telemetry = telemetry;
    }

    //converts RGB to YCrCb, extracts the Cb channel to Cb variable
    void inputToCb(Mat input)
    {
        Imgproc.cvtColor(input, YCrCb, Imgproc.COLOR_RGB2YCrCb);
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

        //averages the box
        sum = (int) Core.sumElems(region_Cb).val[0];

        //draws rectangle for each box
        //region 1
        Imgproc.rectangle(
                input, //image to draw on
                pointA, //top left of the box
                pointB, //bottom right of the box
                BLUE, //rectangle color
                2); //thickness of rectangle


        //sets sig to whichever side of cone is used
        if(sum < 105000)
        {
            sig = Signal.GREEN;
        }
        else if(sum >= 105000 && sum <= 120000)
        {
            sig = Signal.BLACK;
        }
        else if(sum > 100000)
        {
            sig = Signal.PURPLE;
        }

        telemetry.addData("Pattern", sig);
        telemetry.addData("sum", sum);
        telemetry.update();

        //sends the image to EOCVsim
        return input;
    }

    //function called in opmode to get the position
    public Signal getAnalysis()
    {
        return sig;
    }
}
