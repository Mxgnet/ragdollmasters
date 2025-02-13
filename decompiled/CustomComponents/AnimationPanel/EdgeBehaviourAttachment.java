/*
 * Decompiled with CFR 0.152.
 */
package CustomComponents.AnimationPanel;

import CustomComponents.AnimationPanel.Attachment;
import CustomComponents.AnimationPanel.DepthAttachment;
import CustomComponents.AnimationPanel.Sprite;
import java.awt.Point;

public class EdgeBehaviourAttachment
extends Attachment {
    protected int edgeX;
    protected int edgeY;
    protected int edgeWidth;
    protected int edgeHeight;
    protected byte edgeBehaviour;
    public static final byte EB_CONTINUE = 0;
    public static final byte EB_BOUNCE = 1;
    public static final byte EB_SLIDE = 2;
    public static final byte EB_STOP = 3;

    public EdgeBehaviourAttachment(String name, byte edgeBehaviour, int edgeX, int edgeY, int edgeWidth, int edgeHeight) {
        this.name = name;
        this.tag = "EdgeBehaviour";
        this.edgeBehaviour = edgeBehaviour;
        this.edgeX = edgeX;
        this.edgeY = edgeY;
        this.edgeWidth = edgeWidth;
        this.edgeHeight = edgeHeight;
    }

    @Override
    public void update(long timePassed, Sprite s, float oldX, float oldY) {
        switch (this.edgeBehaviour) {
            case 0: {
                break;
            }
            case 1: {
                Attachment[] depthArr;
                if (s.getXPosition() > this.edgeWidth + this.edgeX) {
                    s.setXVelocity(s.getXVelocity() * -1.0);
                    s.setXPosition(this.edgeWidth + this.edgeX);
                }
                if (s.getXPosition() < this.edgeX) {
                    s.setXVelocity(s.getXVelocity() * -1.0);
                    s.setXPosition(this.edgeX);
                }
                if (s.getYPosition() > this.edgeHeight + this.edgeY) {
                    s.setYVelocity(s.getYVelocity() * -1.0);
                    s.setYPosition(this.edgeHeight + this.edgeY);
                }
                if (s.getYPosition() < this.edgeY) {
                    s.setYVelocity(s.getYVelocity() * -1.0);
                    s.setYPosition(this.edgeY);
                }
                for (Attachment a : depthArr = s.getAttachmentsByTag("Depth")) {
                    DepthAttachment d;
                    if (!(a instanceof DepthAttachment) || (d = (DepthAttachment)a).getZPosition() < d.getMaxDepth() && d.getZPosition() > d.getMinDepth()) continue;
                    d.bounce();
                }
                break;
            }
            case 2: {
                if (s.getXPosition() > this.edgeWidth + this.edgeX) {
                    s.setXVelocity(0.0);
                    s.setXPosition(this.edgeWidth + this.edgeX);
                }
                if (s.getXPosition() < this.edgeX) {
                    s.setXVelocity(0.0);
                    s.setXPosition(this.edgeX);
                }
                if (s.getYPosition() > this.edgeHeight + this.edgeY) {
                    s.setYVelocity(0.0);
                    s.setYPosition(this.edgeHeight + this.edgeY);
                }
                if (s.getYPosition() >= this.edgeY) break;
                s.setYVelocity(0.0);
                s.setYPosition(this.edgeY);
                break;
            }
            case 3: {
                if (s.getYPosition() >= this.edgeY && s.getYPosition() <= this.edgeHeight + this.edgeY && s.getXPosition() >= this.edgeX && s.getXPosition() <= this.edgeWidth + this.edgeX) break;
                if (s.getYVelocity() < 0.0) {
                    s.setYPosition(0.0);
                } else {
                    s.setYPosition(this.edgeHeight + this.edgeY);
                }
                s.setYVelocity(0.0);
                break;
            }
            default: {
                throw new AssertionError();
            }
        }
    }

    public byte getEdgeBehaviour() {
        return this.edgeBehaviour;
    }

    public void setEdgeBehaviour(byte eb) {
        this.edgeBehaviour = eb;
    }

    public Point getEdgePoint() {
        return new Point(this.edgeX, this.edgeY);
    }

    public void setEdgePoint(int x, int y) {
        this.edgeX = x;
        this.edgeY = y;
    }

    public int getEdgeWidth() {
        return this.edgeWidth;
    }

    public void setEdgeWidth(int width) {
        this.edgeWidth = width;
    }

    public int getEdgeHeight() {
        return this.edgeHeight;
    }

    public void setEdgeHeight(int height) {
        this.edgeHeight = height;
    }
}

