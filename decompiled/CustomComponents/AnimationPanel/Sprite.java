/*
 * Decompiled with CFR 0.152.
 */
package CustomComponents.AnimationPanel;

import CustomComponents.AnimationPanel.Animation;
import CustomComponents.AnimationPanel.Attachment;
import CustomComponents.AnimationPanel.Vector;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Sprite {
    protected ArrayList<Animation> costumes = new ArrayList();
    protected double x;
    protected double y;
    protected double vx;
    protected double vy;
    protected float accX;
    protected float accY;
    protected short size;
    protected short drawSize;
    protected byte curCostume;
    protected ArrayList<Attachment> attachmentList;

    public Sprite(Animation a, double x, double y, double vx, double vy, short size) {
        this.costumes.add(a);
        this.attachmentList = new ArrayList();
        this.curCostume = 0;
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.accX = 0.0f;
        this.accY = 0.0f;
        this.size = size;
        this.drawSize = (short)100;
    }

    public Sprite(Animation a) {
        this.attachmentList = new ArrayList();
        this.costumes.add(a);
        this.curCostume = 0;
        this.x = 0.0;
        this.y = 0.0;
        this.vx = 0.0;
        this.vy = 0.0;
        this.accX = 0.0f;
        this.accY = 0.0f;
        this.size = (short)100;
        this.drawSize = (short)100;
    }

    public void update(long timePassed) {
        int oldX = (int)this.x;
        int oldY = (int)this.y;
        this.vx += (double)(this.accX * (float)timePassed);
        this.vy += (double)(this.accY * (float)timePassed);
        this.x += this.vx * (double)timePassed;
        this.y += this.vy * (double)timePassed;
        this.customUpdate(timePassed);
        for (Attachment a : this.attachmentList) {
            a.update(timePassed, this, oldX, oldY);
        }
        this.costumes.get(this.curCostume).update(timePassed);
    }

    public void setDirection(float dir) {
        dir = Math.abs(dir % 360.0f);
        double speed = Vector.getResultantMagnitude(this.vx, this.vy * -1.0);
        this.vx = (float)Math.cos(Math.toRadians(dir) * speed);
        this.vy = (float)Math.sin(Math.toRadians(dir) * speed) * -1.0f;
    }

    public void changeDirection(float d) {
        d = Math.abs((d + Vector.getVectorDirection(this.vx, this.vy * -1.0)) % 360.0f);
        double speed = Vector.getResultantMagnitude(this.vx, this.vy * -1.0);
        this.vx = (float)Math.cos(Math.toRadians(d) * speed);
        this.vy = (float)Math.sin(Math.toRadians(d) * speed) * -1.0f;
    }

    public void bounce() {
        this.vx *= -1.0;
        this.vy *= -1.0;
    }

    public float getDirection() {
        return Vector.getVectorDirection(this.vx, this.vy * -1.0);
    }

    public void addCostume(Animation a) {
        this.costumes.add(a);
    }

    public void setCostume(byte i) {
        if (i < this.costumes.size() && i >= 0) {
            this.curCostume = i;
        } else {
            System.err.println("Invalid integer entered.");
        }
    }

    public void setCostume(Animation a) {
        for (int i = 0; i < this.costumes.size(); ++i) {
            if (!this.costumes.get(i).equals(a)) continue;
            this.curCostume = (byte)i;
        }
    }

    public int getCostumeAmount() {
        return this.costumes.size();
    }

    public Animation[] getCostumeList() {
        return (Animation[])this.costumes.toArray();
    }

    public synchronized void resetCostumeList(Animation a) {
        this.costumes.clear();
        this.costumes.add(a);
        this.curCostume = 0;
    }

    public int getXPosition() {
        return (int)this.x;
    }

    public int getYPosition() {
        return (int)this.y;
    }

    public double getTrueXPositionF() {
        return this.x;
    }

    public double getTrueYPosition() {
        return this.y;
    }

    public void setXPosition(double newX) {
        this.x = newX;
    }

    public void setYPosition(double newY) {
        this.y = newY;
    }

    public void changeXPosition(float num) {
        this.x = (double)num + this.x;
    }

    public void changeYPosition(float num) {
        this.y = (double)num + this.y;
    }

    public int getWidth() {
        return this.costumes.get(this.curCostume).getImage().getWidth(null);
    }

    public int getHeight() {
        return this.costumes.get(this.curCostume).getImage().getHeight(null);
    }

    public double getXVelocity() {
        return this.vx;
    }

    public double getYVelocity() {
        return this.vy;
    }

    public void setXVelocity(double xVelocity) {
        this.vx = xVelocity;
    }

    public void setYVelocity(double yVelocity) {
        this.vy = yVelocity;
    }

    public void setXAcceleration(float acc) {
        this.accX = acc;
    }

    public float getXAcceleration() {
        return this.accX;
    }

    public void setYAcceleration(float acc) {
        this.accY = acc;
    }

    public float getYAcceleration() {
        return this.accY;
    }

    public void setDrawSize(short size) {
        this.drawSize = size;
    }

    public short getDrawSize() {
        return this.drawSize;
    }

    public void setSize(short size) {
        this.size = size;
    }

    public short getSize() {
        return this.size;
    }

    public Attachment getAttachment(String name) {
        for (Attachment a : this.attachmentList) {
            if (!a.getName().equals(name)) continue;
            return a;
        }
        return null;
    }

    public boolean addAttachment(Attachment att) {
        boolean exists = false;
        for (Attachment a : this.attachmentList) {
            if (!a.getName().equals(att.getName())) continue;
            exists = true;
        }
        if (exists) {
            return false;
        }
        this.attachmentList.add(att);
        return true;
    }

    public Attachment[] getAttachmentsByTag(String tag) {
        int c = 0;
        for (Attachment a : this.attachmentList) {
            if (!a.getTag().equals(tag)) continue;
            ++c;
        }
        Attachment[] aList = new Attachment[c];
        c = 0;
        for (Attachment a : this.attachmentList) {
            if (!a.getTag().equals(tag)) continue;
            aList[c] = a;
            ++c;
        }
        return aList;
    }

    public Attachment getFirstAttachmentByTag(String tag) {
        for (Attachment a : this.attachmentList) {
            if (!a.getTag().equals(tag)) continue;
            return a;
        }
        return null;
    }

    public Image getSpriteImage() {
        Image img = this.costumes.get(this.curCostume).getImage();
        int newHeight = (int)((double)img.getHeight(null) * ((double)this.drawSize / 100.0));
        int newWidth = (int)((double)img.getWidth(null) * ((double)this.drawSize / 100.0));
        if (newHeight <= 0 || newWidth <= 0) {
            return null;
        }
        img = this.resizeImage(img, newWidth, newHeight, 2);
        return img;
    }

    private Image resizeImage(Image img, int width, int height, int type) {
        BufferedImage newImg = new BufferedImage(width, height, 2);
        Graphics2D g2 = newImg.createGraphics();
        g2.drawImage(img, 0, 0, width, height, null);
        g2.dispose();
        return newImg;
    }

    public Sprite clone() {
        Sprite newSprite = new Sprite(this.costumes.get(0), this.x, this.y, this.vx, this.vy, this.size);
        for (int i = 1; i < this.costumes.size(); ++i) {
            newSprite.addCostume(this.costumes.get(i));
        }
        newSprite.setCostume(this.curCostume);
        for (Attachment att : this.attachmentList) {
            newSprite.addAttachment(att);
        }
        return newSprite;
    }

    protected void customUpdate(long timePassed) {
    }
}

