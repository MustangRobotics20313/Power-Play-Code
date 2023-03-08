package org.firstinspires.ftc.teamcode;

public class TwoWheelOdom {

    private double xpos;
    private double ypos;
    private double rot;
    private double lEncDis;
    private double rEncDis;




    public TwoWheelOdom(double startxpos, double startypos, double startrot, double rEncDis, double lEncDis){
        xpos = startxpos;
        ypos = startypos;
        rot = startrot;
        this.rEncDis = rEncDis;
        this.lEncDis = lEncDis;
    }
    public void updatePos(double changeR,double changeL){
        double fwd = ((changeR*lEncDis + changeL*rEncDis)/(rEncDis+lEncDis));
        rot = rot + ((changeR*lEncDis- changeL*rEncDis)/(rEncDis+lEncDis));
        xpos = xpos + Math.cos(rot)*fwd;
        ypos = xpos + Math.sin(rot)*fwd;
    }
    public double getxpos (){
        return xpos;
    }
    public double getrotation (){
        return rot;
    }
    public double getypos (){
        return ypos;
    }

}
