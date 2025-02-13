/*
 * Decompiled with CFR 0.152.
 */
package CustomComponents;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.LayoutStyle;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;

public class TitleBar
extends JPanel {
    private static int mouseX;
    private static int mouseY;
    private JButton btnExit;
    private JButton btnMax;
    private JButton btnMinimise;
    private JLabel lblTitle;

    public TitleBar(String title) {
        this.initComponents();
        this.lblTitle.setText(title);
    }

    public TitleBar() {
        this("");
    }

    public void setMaximiseEnabled(boolean b) {
        this.btnMax.setVisible(b);
    }

    public void setMinimiseEnabled(boolean b) {
        this.btnMinimise.setVisible(b);
    }

    public final void setIcon(ImageIcon i) {
        this.lblTitle.setIcon(i);
    }

    public final ImageIcon getIcon() {
        return (ImageIcon)this.lblTitle.getIcon();
    }

    public void setExitEnabled(boolean b) {
        this.btnExit.setVisible(b);
    }

    public void setTitle(String name) {
        this.lblTitle.setText(name);
    }

    @Override
    public void setForeground(Color c) {
        super.setForeground(c);
        if (this.lblTitle != null) {
            this.lblTitle.setForeground(c);
        }
        if (this.btnMax != null) {
            this.btnMax.setForeground(c);
        }
        if (this.btnMinimise != null) {
            this.btnMinimise.setForeground(c);
        }
        if (this.btnExit != null) {
            this.btnExit.setForeground(c);
        }
    }

    protected void closeEvent() {
        Window root = SwingUtilities.getWindowAncestor(this);
        if (root instanceof JFrame) {
            JFrame frame = (JFrame)SwingUtilities.getWindowAncestor(this);
            switch (frame.getDefaultCloseOperation()) {
                case 2: {
                    root.dispose();
                    break;
                }
                case 1: {
                    root.setVisible(false);
                    break;
                }
                case 0: {
                    break;
                }
                default: {
                    System.exit(0);
                    break;
                }
            }
        } else {
            root.dispose();
        }
    }

    private void initComponents() {
        this.btnMax = new JButton();
        this.btnMinimise = new JButton();
        this.btnExit = new JButton();
        this.lblTitle = new JLabel();
        this.setBackground(new Color(21, 21, 21));
        this.addMouseMotionListener(new MouseMotionAdapter(){

            @Override
            public void mouseDragged(MouseEvent evt) {
                TitleBar.this.frameDrag(evt);
            }
        });
        this.addMouseListener(new MouseAdapter(){

            @Override
            public void mousePressed(MouseEvent evt) {
                TitleBar.this.formMousePressed(evt);
            }
        });
        this.btnMax.setBackground(new Color(35, 35, 35));
        this.btnMax.setFont(new Font("Dimitri Swank", 0, 14));
        this.btnMax.setText("O");
        this.btnMax.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
        this.btnMax.setFocusable(false);
        this.btnMax.setPreferredSize(new Dimension(20, 18));
        this.btnMax.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent evt) {
                TitleBar.this.btnMaxActionPerformed(evt);
            }
        });
        this.btnMinimise.setBackground(new Color(35, 35, 35));
        this.btnMinimise.setFont(new Font("Dimitri Swank", 0, 11));
        this.btnMinimise.setText("_");
        this.btnMinimise.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
        this.btnMinimise.setFocusable(false);
        this.btnMinimise.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent evt) {
                TitleBar.this.btnMinimiseActionPerformed(evt);
            }
        });
        this.btnExit.setBackground(new Color(35, 35, 35));
        this.btnExit.setFont(new Font("Dimitri Swank", 0, 14));
        this.btnExit.setText("X");
        this.btnExit.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
        this.btnExit.setFocusable(false);
        this.btnExit.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent evt) {
                TitleBar.this.btnExitActionPerformed(evt);
            }
        });
        this.lblTitle.setFont(new Font("FORCED SQUARE", 0, 13));
        this.lblTitle.setText("<Title>");
        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addGap(7, 7, 7).addComponent(this.lblTitle, -1, -1, Short.MAX_VALUE).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.btnMinimise, -2, 20, -2).addGap(3, 3, 3).addComponent(this.btnMax, -2, 20, -2).addGap(3, 3, 3).addComponent(this.btnExit, -2, 20, -2)));
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addGap(1, 1, 1).addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.btnExit, GroupLayout.Alignment.TRAILING, -2, 18, -2).addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.btnMinimise, -2, 18, -2).addComponent(this.lblTitle, -2, 18, -2).addComponent(this.btnMax, -2, 18, -2)))));
    }

    private void btnExitActionPerformed(ActionEvent evt) {
        this.closeEvent();
    }

    private void btnMaxActionPerformed(ActionEvent evt) {
        JFrame root = (JFrame)SwingUtilities.getWindowAncestor(this);
        if (root.getExtendedState() != 6) {
            ((JFrame)SwingUtilities.getWindowAncestor(this)).setExtendedState(6);
        } else {
            ((JFrame)SwingUtilities.getWindowAncestor(this)).setExtendedState(0);
        }
    }

    private void btnMinimiseActionPerformed(ActionEvent evt) {
        ((JFrame)SwingUtilities.getWindowAncestor(this)).setExtendedState(1);
    }

    private void formMousePressed(MouseEvent evt) {
        mouseX = evt.getX();
        mouseY = evt.getY();
    }

    private void frameDrag(MouseEvent evt) {
        Window root = SwingUtilities.getWindowAncestor(this);
        root.setLocation(evt.getXOnScreen() - mouseX, evt.getYOnScreen() - mouseY);
    }
}

