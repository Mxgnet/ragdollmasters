/*
 * Decompiled with CFR 0.152.
 */
package Images.GIF;

import Images.GIF.Frame;
import Images.Picture;
import Nodes.NodeUtils;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.imageio.IIOException;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageOutputStream;

public class GIF
extends Picture {
    private boolean loop;
    private List<Frame> frames;
    private ImageWriter writer;
    private int totalTime;

    public GIF(boolean loop) throws IIOException {
        Iterator<ImageWriter> itr = ImageIO.getImageWritersByFormatName("gif");
        if (!itr.hasNext()) {
            throw new IIOException("GIF writer doesn't exist on this JVM!");
        }
        this.writer = itr.next();
        this.frames = new ArrayList<Frame>();
        this.loop = loop;
    }

    public IIOMetadataNode createLoopApplicationExtensionNode() {
        IIOMetadataNode app_node = new IIOMetadataNode("ApplicationExtension");
        app_node.setAttribute("applicationID", "NETSCAPE");
        app_node.setAttribute("authenticationCode", "2.0");
        app_node.setUserObject(new byte[]{1, 0, 0});
        return app_node;
    }

    public void addFrame(Frame f) {
        this.updateTime();
        this.frames.add(f);
    }

    protected void updateTime() {
        this.totalTime = 0;
        for (Frame frame : this.frames) {
            this.totalTime += frame.getDelay();
        }
    }

    public Frame getFrame(int index) {
        return this.frames.get(index);
    }

    public Frame getFrameAtTime(long time) {
        this.updateTime();
        time %= (long)this.totalTime;
        for (Frame frame : this.frames) {
            if ((time -= (long)frame.getDelay()) > 0L) continue;
            return frame;
        }
        return null;
    }

    public int getFrameCount() {
        return this.frames.size();
    }

    public void moveFrame(int frameIndex, int newIndex) {
        if (newIndex >= this.frames.size() - 1) {
            this.frames.add(this.frames.remove(frameIndex));
        } else {
            this.frames.add(newIndex, this.frames.remove(frameIndex));
        }
    }

    public void createFrame(BufferedImage img, int delay) {
        this.frames.add(new Frame(img, delay, this.writer, this));
        this.updateTime();
    }

    public void createGIF(File output) throws IOException {
        output.createNewFile();
        try (ImageOutputStream ios = ImageIO.createImageOutputStream(output);){
            IIOMetadataNode loopNode = this.createLoopApplicationExtensionNode();
            this.writer.setOutput(ios);
            this.writer.prepareWriteSequence(null);
            for (Frame frame : this.frames) {
                IIOMetadata metadata = frame.getMetadata();
                IIOMetadataNode metaRoot = (IIOMetadataNode)metadata.getAsTree("javax_imageio_gif_image_1.0");
                if (this.loop) {
                    IIOMetadataNode app_extens = (IIOMetadataNode)NodeUtils.getNodeByName("ApplicationExtensions", metaRoot);
                    app_extens.appendChild(loopNode);
                    metadata.setFromTree("javax_imageio_gif_image_1.0", metaRoot);
                }
                IIOImage img = new IIOImage(frame.getImage(), null, metadata);
                this.writer.writeToSequence(img, null);
            }
            this.writer.endWriteSequence();
        }
    }

    @Override
    public Image getImage() {
        return this.getFrameAtTime(System.currentTimeMillis()).getImage();
    }
}

