package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

@SuppressWarnings({"unused", "FieldCanBeLocal", "FieldMayBeFinal"})
public class Pipeline extends OpenCvPipeline {

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

        @SuppressWarnings("NonFinalFieldInEnum")
        public int cvtCode;

        ColorSpace(int cvtCode) {
            this.cvtCode = cvtCode;
        }
    }

    public Scalar lower = new Scalar(0, 0, 0);
    public Scalar upper = new Scalar(255, 255, 255);
    public ColorSpace colorSpace = ColorSpace.YCrCb;

    private static Scalar magenta_l = new Scalar(110, 110, 110);
    private static Scalar magenta_u = new Scalar(145, 170, 155);
    private static Scalar green_l = new Scalar(155, 80, 80);
    private static Scalar green_u = new Scalar(200, 135, 160);
    private static Scalar black_l = new Scalar(0, 125, 100);
    private static Scalar black_u = new Scalar(100, 130, 130);

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
    public Pipeline(Telemetry t) {
        telemetry = t;
    }

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
        Imgproc.cvtColor(input, input, colorSpace.cvtCode);

        region = input.submat(new Rect(pointA, pointB));

        Core.inRange(region, magenta_l, magenta_u, magentaMat);
        Core.inRange(region, green_l, green_u, greenMat);
        Core.inRange(region, black_l, black_u, blackMat);

        greenPercent = Core.countNonZero(greenMat);
        blackPercent = Core.countNonZero(blackMat);
        magentaPercent = Core.countNonZero(magentaMat);

        maximum = Math.max(greenPercent, Math.max(blackPercent, magentaPercent));

        if (maximum == greenPercent) {
            sig = Signal.LEFT;
            Imgproc.rectangle(input, pointA, pointB, GREEN);
        } else if (maximum == blackPercent) {
            sig = Signal.CENTER;
            Imgproc.rectangle(input, pointA, pointB, BLACK);
        } else if (maximum == magentaPercent) {
            sig = Signal.RIGHT;
            Imgproc.rectangle(input, pointA, pointB, MAGENTA);
        }

        telemetry.addData("greenPercent: ", Core.countNonZero(greenMat));
        telemetry.addData("blackPercent: ", Core.countNonZero(blackMat));
        telemetry.addData("magentaPercent: ", Core.countNonZero(magentaMat));
        telemetry.update();

        return input;
    }

    public Signal getAnalysis() {
        return sig;
    }
}