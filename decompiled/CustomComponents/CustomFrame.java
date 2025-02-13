/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.netbeans.lib.awtextra.AbsoluteConstraints
 *  org.netbeans.lib.awtextra.AbsoluteLayout
 */
package CustomComponents;

import CustomComponents.TitleBar;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Polygon;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import org.netbeans.lib.awtextra.AbsoluteConstraints;
import org.netbeans.lib.awtextra.AbsoluteLayout;

public class CustomFrame
extends JFrame {
    private static final int NOT_RESIZING = -1;
    private static final int TOP_LEFT = 0;
    private static final int TOP = 1;
    private static final int TOP_RIGHT = 2;
    private static final int RIGHT = 3;
    private static final int BOT_RIGHT = 4;
    private static final int BOT = 5;
    private static final int BOT_LEFT = 6;
    private static final int LEFT = 7;
    private Insets borderInsets;
    private Container contentPane;
    private int resizeDirection = -1;
    private TitleBar titleBar = new TitleBar();
    private JPanel borderPanel;
    private JPanel contentPanel;
    private TitleBar titleBar1;

    public CustomFrame() {
        this((JFrame)null);
    }

    public CustomFrame(JFrame frame) {
        this.borderInsets = new Insets(3, 3, 3, 3);
        this.initComponents();
        Polygon s = new Polygon(new int[]{0, 5, this.getWidth() - 5, this.getWidth(), this.getWidth(), 0}, new int[]{5, 0, 0, 5, this.getHeight(), this.getHeight()}, 6);
        this.setShape(s);
        this.updateInsets();
        if (frame != null) {
            this.setContentPanel(frame);
        }
        this.setLocationRelativeTo(frame);
    }

    public CustomFrame(JFrame frame, Insets insets) {
        this.borderInsets = insets;
        this.initComponents();
        this.updateInsets();
        this.setContentPanel(frame);
    }

    public final void setContentPanel(JFrame frame) {
        this.contentPane = frame.getContentPane();
        this.titleBar.setTitle(frame.getTitle());
        this.setIconImage(frame.getIconImage());
        this.titleBar.setIcon(new ImageIcon(frame.getIconImage()));
        this.setResizable(frame.isResizable());
        this.contentPanel.setSize(this.contentPane.getSize());
        this.contentPanel.removeAll();
        this.updateInsets(this.contentPane.getSize());
        this.contentPanel.add(this.contentPane);
        Dimension dimension = this.contentPane.getMinimumSize();
        dimension.setSize(dimension.getWidth() + (double)this.borderInsets.top + (double)this.borderInsets.bottom, dimension.getHeight() + (double)this.titleBar.getHeight() + (double)this.borderInsets.left + (double)this.borderInsets.right);
        this.setMinimumSize(dimension);
        this.validate();
    }

    public final void updateInsets() {
        this.updateInsets(this.contentPanel.getSize());
    }

    public final void updateInsets(Dimension dim) {
        this.setSize((int)((double)(this.borderInsets.left + this.borderInsets.right) + dim.getWidth()), (int)((double)(this.borderInsets.top + this.borderInsets.bottom + this.titleBar.getHeight()) + dim.getHeight()));
        this.contentPanel.setSize(dim);
        SpringLayout layout = new SpringLayout();
        layout.putConstraint("West", (Component)this.titleBar, this.borderInsets.left, "West", (Component)this.borderPanel);
        layout.putConstraint("East", (Component)this.titleBar, 0, "East", (Component)this.contentPanel);
        layout.putConstraint("North", (Component)this.titleBar, this.borderInsets.top, "North", (Component)this.borderPanel);
        layout.putConstraint("West", (Component)this.contentPanel, this.borderInsets.left, "West", (Component)this.borderPanel);
        layout.putConstraint("North", (Component)this.contentPanel, 0, "South", (Component)this.titleBar);
        layout.putConstraint("East", (Component)this.borderPanel, this.borderInsets.right, "East", (Component)this.contentPanel);
        layout.putConstraint("South", (Component)this.borderPanel, this.borderInsets.bottom, "South", (Component)this.contentPanel);
        this.borderPanel.setLayout(layout);
        this.updateShape();
        this.validate();
        this.repaint();
    }

    private void updateShape() {
        Polygon s = new Polygon(new int[]{0, 5, this.getWidth() - 5, this.getWidth(), this.getWidth(), 0}, new int[]{5, 0, 0, 5, this.getHeight(), this.getHeight()}, 6);
        this.setShape(s);
    }

    private void initComponents() {
        this.borderPanel = new JPanel();
        this.contentPanel = new JPanel();
        this.titleBar1 = new TitleBar();
        this.setDefaultCloseOperation(3);
        this.setUndecorated(true);
        this.addWindowStateListener(new WindowStateListener(){

            @Override
            public void windowStateChanged(WindowEvent evt) {
                CustomFrame.this.formWindowStateChanged(evt);
            }
        });
        this.borderPanel.setBackground(new Color(51, 51, 51));
        this.borderPanel.addMouseMotionListener(new MouseMotionAdapter(){

            @Override
            public void mouseDragged(MouseEvent evt) {
                CustomFrame.this.borderPanelMouseDragged(evt);
            }

            @Override
            public void mouseMoved(MouseEvent evt) {
                CustomFrame.this.borderPanelMouseMoved(evt);
            }
        });
        this.borderPanel.addMouseListener(new MouseAdapter(){

            @Override
            public void mouseExited(MouseEvent evt) {
                CustomFrame.this.borderPanelMouseExited(evt);
            }

            @Override
            public void mouseReleased(MouseEvent evt) {
                CustomFrame.this.borderPanelMouseReleased(evt);
            }
        });
        this.borderPanel.setLayout((LayoutManager)new AbsoluteLayout());
        this.contentPanel.setBackground(new Color(51, 51, 51));
        this.contentPanel.addMouseListener(new MouseAdapter(){

            @Override
            public void mouseEntered(MouseEvent evt) {
                CustomFrame.this.contentPanelMouseEntered(evt);
            }
        });
        this.contentPanel.setLayout(new BorderLayout());
        this.borderPanel.add((Component)this.contentPanel, new AbsoluteConstraints(0, 30, 390, 256));
        this.borderPanel.add((Component)this.titleBar1, new AbsoluteConstraints(0, 0, 390, -1));
        GroupLayout layout = new GroupLayout(this.getContentPane());
        this.getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.borderPanel, -1, -1, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.borderPanel, -1, -1, Short.MAX_VALUE));
        this.pack();
    }

    private void borderPanelMouseExited(MouseEvent evt) {
        this.borderPanel.setCursor(Cursor.getDefaultCursor());
    }

    private void borderPanelMouseMoved(MouseEvent evt) {
        if (this.isResizable()) {
            if (evt.getX() <= this.borderInsets.left) {
                if (evt.getY() <= this.borderInsets.top) {
                    this.borderPanel.setCursor(Cursor.getPredefinedCursor(6));
                    this.resizeDirection = 0;
                } else if (evt.getY() >= this.getHeight() - this.borderInsets.bottom) {
                    this.borderPanel.setCursor(Cursor.getPredefinedCursor(4));
                    this.resizeDirection = 6;
                } else {
                    this.borderPanel.setCursor(Cursor.getPredefinedCursor(10));
                    this.resizeDirection = 7;
                }
            } else if (evt.getX() >= this.getWidth() - this.borderInsets.right) {
                if (evt.getY() <= this.borderInsets.top) {
                    this.borderPanel.setCursor(Cursor.getPredefinedCursor(7));
                    this.resizeDirection = 2;
                } else if (evt.getY() >= this.getHeight() - this.borderInsets.bottom) {
                    this.borderPanel.setCursor(Cursor.getPredefinedCursor(5));
                    this.resizeDirection = 4;
                } else {
                    this.borderPanel.setCursor(Cursor.getPredefinedCursor(11));
                    this.resizeDirection = 3;
                }
            } else if (evt.getY() > this.borderInsets.top) {
                this.borderPanel.setCursor(Cursor.getPredefinedCursor(8));
                this.resizeDirection = 5;
            } else {
                this.borderPanel.setCursor(Cursor.getPredefinedCursor(9));
                this.resizeDirection = 1;
            }
            this.dispatchEvent(new ComponentEvent(this.contentPanel, 101));
        }
    }

    private void contentPanelMouseEntered(MouseEvent evt) {
    }

    private void borderPanelMouseDragged(MouseEvent evt) {
        Dimension minSize;
        int width = this.getWidth();
        boolean moveX = false;
        int height = this.getHeight();
        boolean moveY = false;
        if (this.resizeDirection == 0 || this.resizeDirection == 7 || this.resizeDirection == 6) {
            width = this.getWidth() + this.getX() - evt.getXOnScreen();
            moveX = true;
        }
        if (this.resizeDirection == 2 || this.resizeDirection == 3 || this.resizeDirection == 4) {
            width = evt.getXOnScreen() - this.getX();
        }
        if (this.resizeDirection == 0 || this.resizeDirection == 1 || this.resizeDirection == 2) {
            height = this.getHeight() + this.getY() - evt.getYOnScreen();
            moveY = true;
        }
        if (this.resizeDirection == 6 || this.resizeDirection == 5 || this.resizeDirection == 4) {
            height = evt.getYOnScreen() - this.getY();
        }
        if ((minSize = this.getMinimumSize()).getWidth() > (double)width) {
            width = (int)minSize.getWidth();
        }
        if (minSize.getHeight() > (double)height) {
            height = (int)minSize.getHeight();
        }
        if (moveX) {
            this.setLocation(this.getX() - (width - this.getWidth()), this.getY());
        }
        if (moveY) {
            this.setLocation(this.getX(), this.getY() - (height - this.getHeight()));
        }
        this.setSize(width, height);
        this.titleBar.setSize(new Dimension(this.getWidth() - this.borderInsets.left - this.borderInsets.right, this.titleBar.getHeight()));
        this.contentPanel.setSize(new Dimension(this.getWidth() - this.borderInsets.left - this.borderInsets.right, this.contentPanel.getHeight() - this.titleBar.getHeight() - this.borderInsets.top - this.borderInsets.bottom));
        this.titleBar.setPreferredSize(new Dimension(this.getWidth() - this.borderInsets.left - this.borderInsets.right, this.titleBar.getHeight()));
        this.contentPanel.setPreferredSize(new Dimension(this.getWidth() - this.borderInsets.left - this.borderInsets.right, this.contentPanel.getHeight() - this.titleBar.getHeight() - this.borderInsets.top - this.borderInsets.bottom));
        this.updateShape();
        this.validate();
    }

    private void formWindowStateChanged(WindowEvent evt) {
        this.titleBar.setSize(new Dimension(this.getWidth() - this.borderInsets.left - this.borderInsets.right, this.titleBar.getHeight()));
        this.contentPanel.setSize(new Dimension(this.getWidth() - this.borderInsets.left - this.borderInsets.right, this.contentPanel.getHeight() - this.titleBar.getHeight() - this.borderInsets.top - this.borderInsets.bottom));
        this.titleBar.setPreferredSize(new Dimension(this.getWidth() - this.borderInsets.left - this.borderInsets.right, this.titleBar.getHeight()));
        this.contentPanel.setPreferredSize(new Dimension(this.getWidth() - this.borderInsets.left - this.borderInsets.right, this.contentPanel.getHeight() - this.titleBar.getHeight() - this.borderInsets.top - this.borderInsets.bottom));
        this.updateShape();
        this.validate();
    }

    private void borderPanelMouseReleased(MouseEvent evt) {
        this.resizeDirection = -1;
    }
}

