/*
 * Decompiled with CFR 0.152.
 */
package Images;

import Images.Picture;
import java.awt.Image;
import javax.swing.ImageIcon;

public class ImageIconCover
extends Picture {
    private ImageIcon image;

    public ImageIconCover(ImageIcon image) {
        this.image = image;
    }

    @Override
    public Image getImage() {
        return this.image.getImage();
    }
}

