/*
 * Decompiled with CFR 0.152.
 */
package CustomComponents.AnimationPanel;

import CustomComponents.AnimationPanel.Attachment;
import CustomComponents.AnimationPanel.Sprite;

public class DepthAttachment
extends Attachment {
    private float z;
    private float vz;
    private int minDepth;
    private int maxDepth;

    public DepthAttachment(String name, float z, float vz, int minDepth, int maxDepth) {
        this.name = name;
        this.tag = "Depth";
        this.z = z;
        this.vz = vz;
        this.minDepth = minDepth;
        this.maxDepth = maxDepth;
    }

    @Override
    public void update(long timePassed, Sprite s, float oldX, float oldY) {
        short drawSize;
        this.z = this.z + this.vz * (float)timePassed > (float)this.minDepth && this.z + this.vz * (float)timePassed < (float)this.maxDepth ? (this.z += this.vz * (float)timePassed) : (this.z + this.vz * (float)timePassed < (float)this.minDepth ? (float)this.minDepth : (float)this.maxDepth);
        if (this.minDepth < this.maxDepth) {
            if (this.z < (float)((this.maxDepth + this.minDepth) / 2)) {
                drawSize = (short)((double)s.getSize() / 100.0 * (double)(((float)this.minDepth - this.z) / (float)this.minDepth) * 100.0);
                drawSize = (short)(drawSize / 2 + 50);
            } else {
                drawSize = (short)((double)s.getSize() / 100.0 * (double)(1.0f + this.z / (float)this.maxDepth) * 100.0);
            }
        } else {
            drawSize = s.getSize();
        }
        s.setDrawSize(drawSize);
    }

    public float getTrueZPosition() {
        return this.z;
    }

    public int getZPosition() {
        return (int)this.z;
    }

    public void changeZPosition(float num) {
        this.z = num + this.z;
    }

    public void setZPosition(float newZ) {
        this.z = newZ;
    }

    public void setZVelocity(float zVelocity) {
        this.vz = zVelocity;
    }

    public float getZVelocity() {
        return this.vz;
    }

    public void bounce() {
        this.vz *= -1.0f;
    }

    public void setDepth(int min, int max) {
        this.minDepth = min;
        this.maxDepth = max;
    }

    public int getMinDepth() {
        return this.minDepth;
    }

    public int getMaxDepth() {
        return this.maxDepth;
    }
}

