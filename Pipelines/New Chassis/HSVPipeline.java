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
public class HSVPipeline extends OpenCvPipeline {

    //signal enum
    public enum Signal {
        GREEN,
        BLACK,
        PURPLE
    }

    //colorspace enum
    private enum ColorSpace {
        RGB(Imgproc.COLOR_RGBA2RGB),
        HSV(Imgproc.COLOR_RGB2HSV),
        YCrCb(Imgproc.COLOR_RGB2YCrCb),
        Lab(Imgproc.COLOR_RGB2Lab);

        public int cvtCode = 0;

        ColorSpace(int cvtCode) {
            this.cvtCode = cvtCode;
        }
    }

    //sets lower and upper limits in EOCV-SIM
    public Scalar lower = new Scalar(0, 0, 0);
    public Scalar upper = new Scalar(255, 255, 255);
    
    public ColorSpace colorSpace = ColorSpace.YCrCb;

    //color constant
    public final Scalar BLUE = new Scalar(0, 0, 255);

    //top left points of the box
    //x is measured from left 0 to right 320, y is measured from top 0 to bottom 240
    public static final Point ANCHOR_POINT = new Point(165, 125);

    //box dimensions
    public static final int REGION_WIDTH = 20;
    public static final int REGION_HEIGHT = 20;

    //top left + bottom right points
    public final Point pointA = new Point(ANCHOR_POINT.x, ANCHOR_POINT.y);
    public final Point pointB = new Point(ANCHOR_POINT.x + REGION_WIDTH, ANCHOR_POINT.y + REGION_WIDTH);

    //image objects + working int
    Mat region;
    Mat hsvM = new Mat();
    private Mat binaryMat = new Mat();
    private Mat maskedInputMat = new Mat();
    Mat hs = new Mat();
    
    //position variable
    private volatile Signal sig = Signal.GREEN;

    //telemetry declaration
    private Telemetry telemetry;

    //constructor for telemetry
    public HSVPipeline(Telemetry telemetry) {
        this.telemetry = telemetry;
    }

    
    //inputs to Cb
    public void inputToSV(Mat input) {
        Imgproc.cvtColor(input, hsvM, Imgproc.COLOR_RGB2HSV);
        Core.extractChannel(hsvM, hs, 2);
    }


    //initializes image
    @Override
    public void init(Mat firstFrame) {
        //inputs to HSV
        inputToSV(firstFrame);

        //subimages
        region = hs.submat(new Rect(pointA, pointB));
    }


    //image processor
    @Override
    public Mat processFrame(Mat input) {
        
        inputToSV(input); //refreshes for new input
        
        Imgproc.cvtColor(input, input, Imgproc.COLOR_RGB2HSV); //converts to HSV
        Core.inRange(hsvM, lower, upper, binaryMat);
        
        maskedInputMat.release();

        Core.bitwise_and(input, input, maskedInputMat, binaryMat);

        

        return maskedInputMat;
    }
}