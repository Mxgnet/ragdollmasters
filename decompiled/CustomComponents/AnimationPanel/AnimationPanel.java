/*
 * Decompiled with CFR 0.152.
 */
package CustomComponents.AnimationPanel;

import CustomComponents.AnimationPanel.Animation;
import CustomComponents.AnimationPanel.EdgeBehaviourAttachment;
import CustomComponents.AnimationPanel.Sprite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import javax.swing.GroupLayout;
import javax.swing.JPanel;

public class AnimationPanel
extends JPanel
implements Runnable {
    private boolean running;
    private final ArrayList<Sprite> sprites = new ArrayList();
    private BufferedImage overlay;
    private Color color;
    private String title = "";
    private final int connectDist = 40;
    private EdgeBehaviourAttachment globalEB;
    private Animation globalAnimation;
    private Point mousePoint = new Point();
    private boolean mouseOn = false;
    private float spriteDensity = 0.0025f;

    public AnimationPanel() {
        this.initComponents();
    }

    @Override
    public void run() {
        this.running = true;
        this.init();
        this.gameloop();
    }

    protected void init() {
        this.mousePoint.setLocation(0, this.getHeight() / 2);
        this.color = this.getForeground();
        this.globalAnimation = new Animation();
        BufferedImage tempImg = new BufferedImage(3, 3, 2);
        Graphics2D g = (Graphics2D)tempImg.getGraphics();
        g.rotate(Math.toRadians(45.0));
        g.setColor(this.color.brighter());
        g.fillRect(0, 0, 3, 3);
        g.dispose();
        tempImg.flush();
        this.globalAnimation.addScene(tempImg, 10L);
        this.globalEB = new EdgeBehaviourAttachment("globalEB", 1, 0, 0, this.getWidth(), this.getHeight());
        int area = this.getWidth() * this.getHeight();
        int numSprites = (int)((float)area * this.spriteDensity);
        for (int i = 0; i < numSprites; ++i) {
            Sprite sp = new Sprite(this.globalAnimation, (float)Math.random() * (float)this.getWidth(), (float)Math.random() * (float)this.getHeight(), (float)Math.random() * 0.02f - 0.01f, (float)Math.random() * 0.02f - 0.01f, 100);
            sp.addAttachment(this.globalEB);
            this.sprites.add(sp);
        }
    }

    private void gameloop() {
        long startingTime;
        long totTime = startingTime = System.currentTimeMillis();
        while (!this.isShowing()) {
            try {
                Thread.sleep(100L);
            }
            catch (InterruptedException ex) {}
        }
        while (this.running && this.isShowing()) {
            long timePassed = System.currentTimeMillis() - totTime;
            totTime += timePassed;
            this.update(timePassed);
            this.overlay = new BufferedImage(this.getWidth(), this.getHeight(), 2);
            Graphics2D g = (Graphics2D)this.overlay.getGraphics();
            this.draw(g);
            this.overlay.flush();
            g.dispose();
            if (this.getRootPane() != null) {
                this.getRootPane().repaint();
            }
            try {
                Thread.sleep(20L);
            }
            catch (InterruptedException ex) {}
        }
    }

    private synchronized void update(long timePassed) {
        if (!this.mouseOn) {
            this.mousePoint.x = (int)((float)this.mousePoint.x + (float)timePassed * 0.2f);
            if (this.mousePoint.x > this.getWidth()) {
                this.mousePoint.x = 0;
            }
        }
        for (Sprite sprite : this.sprites.toArray(new Sprite[0])) {
            sprite.update(timePassed);
        }
    }

    private synchronized void draw(Graphics2D g) {
        for (Sprite sprite : this.sprites.toArray(new Sprite[0])) {
            g.drawImage(sprite.getSpriteImage(), sprite.getXPosition(), sprite.getYPosition(), null);
        }
        ArrayList<Sprite> connectList = new ArrayList<Sprite>();
        for (Sprite sp : this.sprites.toArray(new Sprite[0])) {
            if (!(this.mousePoint.distance(sp.getXPosition(), sp.getYPosition()) < 40.0)) continue;
            connectList.add(sp);
        }
        g.setColor(this.color.darker());
        for (int i = 0; i < connectList.size() - 1; ++i) {
            for (int j = i + 1; j < connectList.size(); ++j) {
                if (!(Point.distance(((Sprite)connectList.get(i)).getXPosition(), ((Sprite)connectList.get(i)).getYPosition(), ((Sprite)connectList.get(j)).getXPosition(), ((Sprite)connectList.get(j)).getYPosition()) <= 20.0)) continue;
                g.drawLine(((Sprite)connectList.get(i)).getXPosition(), ((Sprite)connectList.get(i)).getYPosition(), ((Sprite)connectList.get(j)).getXPosition(), ((Sprite)connectList.get(j)).getYPosition());
            }
        }
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(this.color);
        g.setFont(new Font("Dimitri Swank", 0, 28));
        Rectangle2D bounds = g.getFontMetrics().getStringBounds(this.title, g);
        g.drawString(this.title, (int)((double)(this.getWidth() / 2) - bounds.getCenterX()), (int)((double)(this.getHeight() / 2) - bounds.getCenterY()));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(this.overlay, 0, 0, this);
    }

    public void stop() {
        this.running = false;
    }

    public void updateMouse(Point mouse) {
        this.mousePoint = mouse;
    }

    public float getSpriteDensity() {
        return this.spriteDensity;
    }

    public synchronized void setSpriteDensity(float spriteDensity) {
        this.spriteDensity = spriteDensity;
        if (this.running) {
            int area = this.getWidth() * this.getHeight();
            int numSprites = (int)((float)area * spriteDensity);
            while (this.sprites.size() > numSprites) {
                this.sprites.remove(0);
            }
            while (this.sprites.size() < numSprites) {
                Sprite sp = new Sprite(this.globalAnimation, (float)Math.random() * (float)this.getWidth(), (float)Math.random() * (float)this.getHeight(), (float)Math.random() * 0.02f - 0.01f, (float)Math.random() * 0.02f - 0.01f, 100);
                sp.addAttachment(this.globalEB);
                this.sprites.add(sp);
            }
            for (Sprite s : this.sprites.toArray(new Sprite[0])) {
                s.setXPosition((float)Math.random() * (float)this.getWidth());
                s.setYPosition((float)Math.random() * (float)this.getHeight());
                s.setXVelocity((float)Math.random() * 0.02f - 0.01f);
                s.setYVelocity((float)Math.random() * 0.02f - 0.01f);
            }
        }
    }

    public float calculateSpriteDensity() {
        int area = this.getWidth() * this.getHeight();
        return (float)this.sprites.size() / (float)area;
    }

    private synchronized void setColor(Color c) {
        if (this.globalAnimation != null) {
            this.color = c;
            BufferedImage tempImg = new BufferedImage(3, 3, 2);
            Graphics2D g = (Graphics2D)tempImg.getGraphics();
            g.rotate(Math.toRadians(45.0));
            g.setColor(c.brighter());
            g.fillRect(0, 0, 3, 3);
            g.dispose();
            tempImg.flush();
            this.globalAnimation.replaceScenes(tempImg, 100L);
        }
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private void initComponents() {
        this.addMouseMotionListener(new MouseMotionAdapter(){

            @Override
            public void mouseDragged(MouseEvent evt) {
                AnimationPanel.this.formMouseDragged(evt);
            }

            @Override
            public void mouseMoved(MouseEvent evt) {
                AnimationPanel.this.formMouseMoved(evt);
            }
        });
        this.addMouseListener(new MouseAdapter(){

            @Override
            public void mouseExited(MouseEvent evt) {
                AnimationPanel.this.formMouseExited(evt);
            }
        });
        this.addComponentListener(new ComponentAdapter(){

            @Override
            public void componentResized(ComponentEvent evt) {
                AnimationPanel.this.formComponentResized(evt);
            }
        });
        this.addPropertyChangeListener(new PropertyChangeListener(){

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                AnimationPanel.this.formPropertyChange(evt);
            }
        });
        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGap(0, 400, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGap(0, 300, Short.MAX_VALUE));
    }

    private void formComponentResized(ComponentEvent evt) {
        if (this.globalEB != null) {
            this.globalEB.setEdgeWidth(this.getWidth());
            this.globalEB.setEdgeHeight(this.getHeight());
            this.setSpriteDensity(this.spriteDensity);
        }
    }

    private void formMouseMoved(MouseEvent evt) {
        this.mousePoint = evt.getPoint();
        this.mouseOn = true;
    }

    private void formMouseDragged(MouseEvent evt) {
        this.mousePoint = evt.getPoint();
        this.mouseOn = true;
    }

    private void formMouseExited(MouseEvent evt) {
        this.mousePoint.y = this.getHeight() / 2;
        this.mouseOn = false;
    }

    private void formPropertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("foreground")) {
            this.setColor((Color)evt.getNewValue());
        }
    }
}

