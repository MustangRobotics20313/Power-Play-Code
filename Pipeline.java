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
    public enum Signal {
        GREEN,
        ORANGE,
        PURPLE
    }

    //top left point of box
    //x is measured left to right 0-320, y is measured top to bottom 0-240
    private static final Point ANCHOR_POINT = new Point(190, 80);

    //box dimensions
    private static final int REGION_WIDTH = 30;
    private static final int REGION_HEIGHT = 30;

    //color
    private final Scalar BLUE = new Scalar(0, 0, 255);

    //top left & bottom right points
    private Point pointA = new Point(ANCHOR_POINT.x, ANCHOR_POINT.y);
    private Point pointB = new Point(ANCHOR_POINT.x + REGION_WIDTH, ANCHOR_POINT.y + REGION_HEIGHT);

    //image objects & average ints
    Mat region_Cb;
    Mat YCrCb = new Mat();
    Mat Cb = new Mat();
    int sum;

    //position variable
    private volatile Signal sig = Signal.GREEN;

    //telemetry for EOCVSim
    private Telemetry telemetry;

    //constructor for telemetry
    public Pipeline(Telemetry telemetry) {
        this.telemetry = telemetry;
    }

    //converts RGB to YCrCb, extracts Cb channel to Cb mat
    private void inputToCb(Mat input) {
        Imgproc.cvtColor(input, YCrCb, Imgproc.COLOR_RGB2YCRCB);
        Core.extractChannel(YCrCb, Cb, 2);
    }

    //initializes image
    @Override
    public void init(Mat firstFrame) {
        //initializes image
        inputToCb(firstFrame);

        //subimages of what's inside the box
        region_Cb = Cb.submat(new Rect(point A, point B));
    }

    //image processor
    @Override
    public Mat processFrame(Mat input) {
        //refreshes new input
        inputToCb(input);

        //sums the box
        sum = (int) Core.sumElems(region_Cb).val[0];

        //draws rectangle for box
        Imgproc.rectangle(
                input, //image to draw on
                pointA, //top left of box
                pointB, //bottom right of box
                BLUE, //rectangle color,
                2); //thickness of rectangle

        //sets sig to whichever side of cone is used
        if (sum > 90000 && sum < 102000) {
            sig = Signal.ORANGE;
        } else if (sum >= 102000 && sum <= 115000) {
            sig = Signal.GREEN;
        } else if (sum > 115000) {
            sig = Signal.PURPLE;
        }

        telemetry.addData("Pattern", sig);
        telemetry.addData("sum", sum);
        telemetry.update();

        //sends image to EOCVSim
        return input;
    }

    public Signal getAnalysis() {
        return sig;
    }
}