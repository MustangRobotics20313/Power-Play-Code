import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

@Disabled
public class SleevePipeline extends OpenCvPipeline {
    public enum Signal {
        LEFT, //Green
        CENTER, //Black/orange/whatever
        RIGHT, //Magenta
        NULL,
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

    public ColorSpace colorSpace = ColorSpace.RGB;

    //variables for tuning
    public Scalar upper = new Scalar(255, 255, 255);
    public Scalar lower = new Scalar(0, 0, 0);

    //box dimensions
    public static final int REGION_WIDTH = 25;
    public static final int REGION_HEIGHT = 25;

    //Anchor points
    private static final Point ANCHOR = new Point(160, 130);
    public final Point pointA = new Point(ANCHOR.x, ANCHOR.y);
    public final Point pointB = new Point(ANCHOR.x + REGION_WIDTH, ANCHOR.y + REGION_HEIGHT);

    //lower + upper boundaries for colors
    private static final Scalar green_l = new Scalar(68, 124, 124);
    private static final Scalar green_u = new Scalar(167, 117, 106); 
    private static final Scalar black_l = new Scalar(35, 128, 128);
    private static final Scalar black_u = new Scalar(58, 128, 130);
    private static final Scalar magenta_l = new Scalar(54, 134, 129);
    private static final Scalar magenta_u = new Scalar(149, 155, 136);

    //Color definitions
    private final Scalar GREEN = new Scalar(0, 255, 0);
    private final Scalar BLACK = new Scalar(0, 0, 0);
    private final Scalar MAGENTA = new Scalar(255, 0, 255);

    //Telemetry
    private Telemetry telemetry;
    public SleevePipeline(Telemetry telemetry) {
        this.telemetry = telemetry;
    }

    
    //Percentage
    private double greenPercent;
    private double blackPercent;
    private double magentaPercent;
    private double maximum;

    //Mat declarations
    private Mat greenMat = new Mat();
    private Mat blackMat = new Mat();
    private Mat magentaMat = new Mat();
    private Mat YCrCb = new Mat();
    private Mat Cb = new Mat();
    private Mat binaryMat = new Mat();
    private Mat maskedInputMat = new Mat();
    private Mat region = new Mat();

    //signal variable
    private volatile Signal sig = Signal.LEFT;

    public void inputToCb(Mat input) {
        Imgproc.cvtColor(input, YCrCb, colorSpace.cvtCode);
        Core.extractChannel(YCrCb, Cb, 2);
    }

    @Override
    public void init(Mat firstFrame) {
        inputToCb(firstFrame);
    }

    @Override
    public Mat processFrame(Mat input) {
        inputToCb(input);
        Core.inRange(YCrCb, lower, upper, binaryMat);
        Core.bitwise_and(input, input, maskedInputMat, binaryMat);
        //Noise reduction
        region = input.submat(new Rect(pointA, pointB));

        //applys filters to colors
        Core.inRange(region, green_l, green_u, greenMat);
        Core.inRange(region, black_l, black_u, blackMat);
        Core.inRange(region, magenta_l, magenta_u, magentaMat);
        
        greenPercent = Core.countNonZero(greenMat);
        blackPercent = Core.countNonZero(blackMat);
        magentaPercent = Core.countNonZero(magentaMat);

        telemetry.addData("greenPercent: ", greenPercent);
        telemetry.addData("blackPercent: ", blackPercent);
        telemetry.addData("magentaPercent: ", magentaPercent);

        maximum = Math.max(greenPercent, Math.max(blackPercent, magentaPercent));
        telemetry.addData("max:", maximum);
        if (maximum == greenPercent) {
            Imgproc.rectangle(maskedInputMat, pointA, pointB, GREEN, 2);
            sig = Signal.LEFT;
        } else if (maximum == blackPercent) {
            Imgproc.rectangle(maskedInputMat, pointA, pointB, BLACK, 2);
            sig = Signal.CENTER;
        } else if (maximum == magentaPercent) {
            Imgproc.rectangle(maskedInputMat, pointA, pointB, MAGENTA, 2);
            sig = Signal.RIGHT;
        } else {
            Imgproc.rectangle(maskedInputMat, pointA, pointB, BLACK, -1);
            sig = Signal.NULL;
        }
        
        region.release();
        greenMat.release();
        blackMat.release();
        magentaMat.release();

        telemetry.addData("Pattern: ", sig);
        telemetry.update();

        return maskedInputMat;
    }

    public Signal getAnalysis() {
        return sig;
    }
}