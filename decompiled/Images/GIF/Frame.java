/*
 * Decompiled with CFR 0.152.
 */
package Images.GIF;

import Images.GIF.GIF;
import Nodes.NodeUtils;
import java.awt.image.BufferedImage;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOInvalidTreeException;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;

public class Frame {
    private BufferedImage image;
    private IIOMetadata metadata;
    private int delay;
    private GIF parent;

    public Frame(BufferedImage image, int delay, ImageWriter writer, GIF parent) {
        this.image = image;
        this.delay = delay;
        this.metadata = writer.getDefaultImageMetadata(ImageTypeSpecifier.createFromBufferedImageType(image.getType()), null);
        this.setGraphicControllerExtensions(delay);
    }

    public IIOMetadata getMetadata() {
        return this.metadata;
    }

    private void setGraphicControllerExtensions(int delay) {
        IIOMetadataNode root = (IIOMetadataNode)this.metadata.getAsTree("javax_imageio_gif_image_1.0");
        IIOMetadataNode graphics = (IIOMetadataNode)NodeUtils.getNodeByName("GraphicControlExtension", root);
        graphics.setAttribute("delayTime", "" + delay / 10);
        graphics.setAttribute("disposalMethod", "none");
        graphics.setAttribute("userInputFlag", "FALSE");
        try {
            this.metadata.setFromTree("javax_imageio_gif_image_1.0", root);
        }
        catch (IIOInvalidTreeException ex) {
            ex.printStackTrace(System.err);
        }
    }

    public BufferedImage getImage() {
        return this.image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public int getDelay() {
        return this.delay;
    }

    public void setDelay(int milli) {
        this.setGraphicControllerExtensions(milli);
        if (this.parent != null) {
            this.parent.updateTime();
        }
    }
}

