/*
 * Decompiled with CFR 0.152.
 */
package Images;

import Images.Picture;
import java.awt.Image;

public class ImageCover
extends Picture {
    private Image image;

    public ImageCover(Image image) {
        this.image = image;
    }

    @Override
    public Image getImage() {
        return this.image;
    }
}

