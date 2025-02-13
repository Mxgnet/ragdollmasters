/*
 * Decompiled with CFR 0.152.
 */
package CustomComponents.AnimationPanel;

public class Vector {
    protected double vx;
    protected double vy;
    protected double vz;

    public Vector(double xVelocity, double yVelocity, double zVelocity) {
        this.vx = xVelocity;
        this.vy = yVelocity;
        this.vz = zVelocity;
    }

    public Vector(float xVelocity, float yVelocity) {
        this(xVelocity, yVelocity, 0.0);
    }

    public double getXVelocity() {
        return this.vx;
    }

    public void setXVelocity(double xVelocity) {
        this.vx = xVelocity;
    }

    public void changeXVelocity(double d) {
        this.vx += d;
    }

    public double getYVelocity() {
        return this.vy;
    }

    public void setYVelocity(double yVelocity) {
        this.vy = yVelocity;
    }

    public void changeYVelocity(double d) {
        this.vy += d;
    }

    public double getZVelocity() {
        return this.vz;
    }

    public void setZVelocity(double zVelocity) {
        this.vz = zVelocity;
    }

    public void changeZVelocity(double d) {
        this.vz += d;
    }

    public static float getDirectionOfLine(double x1, double y1, double x2, double y2) {
        float dir;
        try {
            dir = (float)Math.toDegrees(Math.atan((y2 - y1) / (x2 - x1)));
            if (dir < 0.0f) {
                dir = (float)((double)dir + 360.0);
            }
            if (x2 < x1) {
                dir = (float)((double)dir + 180.0);
            }
            dir %= 360.0f;
        }
        catch (ArithmeticException x) {
            dir = 90.0f;
        }
        return dir;
    }

    public static float getVectorDirection(double yComp, double xComp) {
        float dir;
        try {
            dir = (float)Math.toDegrees(Math.atan(yComp / xComp));
            if (dir < 0.0f) {
                dir += 360.0f;
            }
            if (xComp < 0.0) {
                dir += 180.0f;
            }
            dir %= 360.0f;
        }
        catch (ArithmeticException x) {
            dir = 90.0f;
        }
        return dir;
    }

    public static double getResultantMagnitude(double vx, double vy) {
        return Math.sqrt(vx * vx + vy * vy);
    }

    public static double getXComponent(double mag, float dir) {
        return Math.cos(Math.toRadians(dir)) * mag;
    }

    public static double getYComponent(double mag, float dir) {
        return Math.sin(Math.toRadians(dir)) * mag;
    }
}

