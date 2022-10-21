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
import java.lang.Math;

public class TestPipeline extends OpenCvPipeline {

    public enum Signal {
        LEFT, 
        CENTER,
        RIGHT,
        NULL,
    }

    private enum ColorSpace {
        RGB(Imgproc.COLOR_RGBA2RGB),
        HSV(Imgproc.COLOR_RGB2HSV),
        YCrCb(Imgproc.COLOR_RGB2YCrCb),
        Lab(Imgproc.COLOR_RGB2Lab);

        public int cvtCode = 0;

        ColorSpace(int cvtCode) {
            this.cvtCode = 1;
        }
    }

    public Scalar lower = new Scalar(0, 0, 0);
    public Scalar upper = new Scalar(255, 255, 255);
    public ColorSpace colorSpace = ColorSpace.HSV;


    private static final Scalar green_l = new Scalar(51, 69, 65);
    private static final Scalar green_u = new Scalar(141, 175, 119); 
    private static final Scalar black_l = new Scalar(28, 21, 25);
    private static final Scalar black_u = new Scalar(80, 76, 86);
    private static final Scalar magenta_l = new Scalar(65, 42, 75);
    private static final Scalar magenta_u = new Scalar(175, 117, 154);

    public static final int REGION_WIDTH = 20;
    public static final int REGION_HEIGHT = 20;

    private static final Point ANCHOR = new Point(165, 132);
    public final Point pointA = new Point(ANCHOR.x, ANCHOR.y);
    public final Point pointB = new Point(ANCHOR.x + REGION_WIDTH, ANCHOR.y + REGION_HEIGHT);

    private final Scalar GREEN = new Scalar(0, 255, 0);
    private final Scalar BLACK = new Scalar(0, 0, 0);
    private final Scalar MAGENTA = new Scalar(255, 0, 255);

    private volatile Signal sig = Signal.NULL;

    private Telemetry telemetry;
    public TestPipeline(Telemetry telemetry) {
        this.telemetry = telemetry;
    }

    private Mat YCrCb = new Mat();
    private Mat binaryMat = new Mat();
    private Mat maskedInputMat = new Mat();
    private Mat greenMat = new Mat();
    private Mat blackMat = new Mat();
    private Mat magentaMat = new Mat();
    private Mat region = new Mat();

    private double greenPercent;
    private double blackPercent;
    private double magentaPercent;
    private double maximum;

    @Override
    public Mat processFrame(Mat input) {
        Imgproc.cvtColor(input, YCrCb, colorSpace.cvtCode);

        maskedInputMat.release();

        /*Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3, 3));
        Imgproc.morphologyEx(input, maskedInputMat, Imgproc.MORPH_CLOSE, kernel);*/
        Core.inRange(YCrCb, lower, upper, binaryMat);

        

        Core.bitwise_and(input, input, maskedInputMat, binaryMat);

        region = maskedInputMat.submat(new Rect(pointA, pointB));

        Core.inRange(region, magenta_l, magenta_u, magentaMat);
        Core.inRange(region, green_l, green_u, greenMat);
        Core.inRange(region, black_l, black_u, blackMat);

        greenPercent = Core.countNonZero(greenMat);
        blackPercent = Core.countNonZero(blackMat);
        magentaPercent = Core.countNonZero(magentaMat);
        
        
        telemetry.addData("Colorspace: ", colorSpace.name());
        telemetry.addData("Lower: ", lower);
        telemetry.addData("Upper: ", upper);
        telemetry.addData("greenPercent: ", greenPercent);
        telemetry.addData("blackPercent: ", blackPercent);
        telemetry.addData("magentaPercent: ", magentaPercent);

        maximum = Math.max(greenPercent, Math.max(blackPercent, magentaPercent));
        telemetry.addData("max: ", maximum);
        
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

        telemetry.addData("Pattern: ", sig);

        telemetry.update();

        return maskedInputMat;
    }

    public Signal getAnalysis() {
        return sig;
    }
}