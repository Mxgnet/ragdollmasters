/*
 * Decompiled with CFR 0.152.
 */
package CustomComponents.AnimationPanel;

import CustomComponents.AnimationPanel.Sprite;

public abstract class Attachment {
    protected String name;
    protected String tag = "DefaultAttachmentTag";

    public abstract void update(long var1, Sprite var3, float var4, float var5);

    public String getName() {
        return this.name;
    }

    public void setName(String newName) {
        this.name = newName;
    }

    public String getTag() {
        return this.tag;
    }
}

