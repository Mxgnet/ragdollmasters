/*
 * Decompiled with CFR 0.152.
 */
package CustomComponents;

import Images.GIF.GIF;
import Images.Picture;
import java.awt.Graphics;
import javax.swing.GroupLayout;
import javax.swing.JPanel;
import javax.swing.RepaintManager;

public class ImagePanel
extends JPanel {
    Picture image;

    public ImagePanel(Picture image) {
        this.image = image;
        this.initComponents();
    }

    public ImagePanel() {
        this.image = null;
        this.initComponents();
    }

    public Picture getImage() {
        return this.image;
    }

    public void setImage(Picture image) {
        this.image = image;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (this.image != null) {
            g.drawImage(this.image.getImage(), 0, 0, this.getWidth(), this.getHeight(), this);
            if (this.image instanceof GIF) {
                RepaintManager.currentManager(this).markCompletelyDirty(this);
            }
        }
    }

    private void initComponents() {
        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGap(0, 400, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGap(0, 300, Short.MAX_VALUE));
    }
}

