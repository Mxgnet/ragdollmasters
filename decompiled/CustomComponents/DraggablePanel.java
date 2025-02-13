/*
 * Decompiled with CFR 0.152.
 */
package CustomComponents;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import javax.swing.GroupLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JWindow;
import javax.swing.border.LineBorder;

public class DraggablePanel
extends JPanel {
    private static final LineBorder HOVER_BORDER = new LineBorder(new Color(0, 102, 255), 2, true);
    private static final LineBorder IDLE_BORDER = new LineBorder(new Color(240, 240, 240), 2, true);
    private int swapImageIndex;
    private final JWindow swapWindow;
    private final JSeparator swapSeparator;
    private Point swapWindowOffset = new Point();
    private JComponent swapComponent = null;
    private JPanel pnlItems;
    private JScrollPane scrlItems;

    public DraggablePanel() {
        this.initComponents();
        this.initComponents();
        this.swapSeparator = new JSeparator(1);
        this.swapWindow = new JWindow((Window)this.getTopLevelAncestor()){

            @Override
            public void paint(Graphics g) {
                if (DraggablePanel.this.swapComponent != null) {
                    DraggablePanel.this.swapComponent.paint(g);
                }
            }
        };
        this.swapWindow.setLayout(new BorderLayout());
        this.swapWindow.pack();
    }

    @Override
    public Component add(Component component) {
        return this.add(component, 0);
    }

    @Override
    public Component add(Component component, int index) {
        if (component instanceof JComponent) {
            JComponent jc = (JComponent)component;
            jc.addMouseListener(new ImageBoxMouseListener());
            jc.setBorder(IDLE_BORDER);
            jc.addMouseMotionListener(new ImagePanelMouseMotionListener());
            return this.pnlItems.add((Component)jc, index);
        }
        JPanel pnl = new JPanel();
        pnl.setSize(component.getSize());
        pnl.setPreferredSize(component.getPreferredSize());
        pnl.addMouseListener(new ImageBoxMouseListener());
        pnl.setBorder(IDLE_BORDER);
        pnl.addMouseMotionListener(new ImagePanelMouseMotionListener());
        return this.pnlItems.add((Component)pnl, index);
    }

    private void initComponents() {
        this.scrlItems = new JScrollPane();
        this.pnlItems = new JPanel();
        this.scrlItems.setVerticalScrollBarPolicy(21);
        this.pnlItems.addMouseMotionListener(new MouseMotionAdapter(){

            @Override
            public void mouseMoved(MouseEvent evt) {
                DraggablePanel.this.pnlItemsMouseMoved(evt);
            }
        });
        this.pnlItems.addMouseListener(new MouseAdapter(){

            @Override
            public void mouseReleased(MouseEvent evt) {
                DraggablePanel.this.pnlItemsMouseReleased(evt);
            }
        });
        this.pnlItems.setLayout(new FlowLayout(3, 5, 0));
        this.scrlItems.setViewportView(this.pnlItems);
        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.scrlItems, -1, 400, Short.MAX_VALUE));
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(this.scrlItems, -1, 268, Short.MAX_VALUE).addGap(0, 0, 0)));
    }

    private void pnlItemsMouseMoved(MouseEvent evt) {
    }

    private void pnlItemsMouseReleased(MouseEvent evt) {
        this.stopDragging(evt.getLocationOnScreen());
    }

    @Override
    public int getComponentCount() {
        return this.pnlItems.getComponentCount();
    }

    private int getComponentIndexFromScreenPoint(JPanel panel, Point screenPoint) {
        int pos;
        for (pos = 0; pos < panel.getComponentCount(); ++pos) {
            if (!panel.getComponent(pos).isShowing() || screenPoint.x - this.swapComponent.getWidth() / 2 >= panel.getComponent((int)pos).getLocationOnScreen().x) continue;
            ++pos;
            break;
        }
        return Math.max(pos - 1, 0);
    }

    private void stopDragging(Point screen) {
        System.out.println("Stop drag");
        this.pnlItems.remove(this.swapSeparator);
        this.swapComponent.setVisible(true);
        int pos = this.getComponentIndexFromScreenPoint(this.pnlItems, screen);
        if (this.swapImageIndex != pos) {
            this.pnlItems.remove(this.swapComponent);
            this.pnlItems.add((Component)this.swapComponent, pos);
        }
        this.swapComponent = null;
        this.swapWindow.setVisible(false);
        this.pnlItems.validate();
        this.pnlItems.repaint();
    }

    private class ImageBoxMouseListener
    implements MouseListener {
        private ImageBoxMouseListener() {
        }

        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void mousePressed(MouseEvent e) {
            System.out.println("press on ImageBox");
            DraggablePanel.this.swapWindowOffset = e.getPoint();
            DraggablePanel.this.swapComponent = (JComponent)e.getComponent();
            for (int i = 0; i < DraggablePanel.this.pnlItems.getComponentCount(); ++i) {
                if (!DraggablePanel.this.pnlItems.getComponent(i).equals(DraggablePanel.this.swapComponent)) continue;
                DraggablePanel.this.swapImageIndex = i;
                break;
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            DraggablePanel.this.stopDragging(e.getLocationOnScreen());
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            for (Component c : DraggablePanel.this.pnlItems.getComponents()) {
                if (!(c instanceof JPanel)) continue;
                ((JPanel)c).setBorder(IDLE_BORDER);
            }
            if (e.getComponent() instanceof JPanel && DraggablePanel.this.swapComponent == null) {
                ((JPanel)e.getComponent()).setBorder(HOVER_BORDER);
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            if (e.getComponent() instanceof JPanel && !e.getComponent().contains(e.getPoint())) {
                ((JPanel)e.getComponent()).setBorder(IDLE_BORDER);
            }
        }
    }

    private class ImagePanelMouseMotionListener
    implements MouseMotionListener {
        private ImagePanelMouseMotionListener() {
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (DraggablePanel.this.swapComponent != null) {
                if (DraggablePanel.this.swapWindow.isVisible()) {
                    DraggablePanel.this.swapWindow.setLocation(e.getXOnScreen() - ((DraggablePanel)DraggablePanel.this).swapWindowOffset.x, e.getYOnScreen() - ((DraggablePanel)DraggablePanel.this).swapWindowOffset.y);
                    int pos = DraggablePanel.this.getComponentIndexFromScreenPoint(DraggablePanel.this.pnlItems, e.getLocationOnScreen());
                    DraggablePanel.this.pnlItems.remove(DraggablePanel.this.swapSeparator);
                    DraggablePanel.this.pnlItems.add((Component)DraggablePanel.this.swapSeparator, pos);
                    DraggablePanel.this.pnlItems.revalidate();
                    DraggablePanel.this.pnlItems.repaint();
                } else {
                    System.out.println("Original order");
                    for (Component comp : DraggablePanel.this.pnlItems.getComponents()) {
                        System.out.println(comp.getName());
                    }
                    System.out.println();
                    DraggablePanel.this.swapWindow.repaint();
                    DraggablePanel.this.swapWindow.setSize(DraggablePanel.this.swapComponent.getSize());
                    DraggablePanel.this.swapWindow.validate();
                    DraggablePanel.this.swapComponent.setVisible(false);
                    DraggablePanel.this.swapWindow.setVisible(true);
                    DraggablePanel.this.swapWindow.setLocation(e.getXOnScreen() - ((DraggablePanel)DraggablePanel.this).swapWindowOffset.x, e.getYOnScreen() - ((DraggablePanel)DraggablePanel.this).swapWindowOffset.y);
                    DraggablePanel.this.swapSeparator.setVisible(true);
                    DraggablePanel.this.swapSeparator.setSize(2, DraggablePanel.this.swapComponent.getHeight() - 6);
                    DraggablePanel.this.swapSeparator.setPreferredSize(DraggablePanel.this.swapSeparator.getSize());
                }
            }
        }

        @Override
        public void mouseMoved(MouseEvent e) {
        }
    }
}

