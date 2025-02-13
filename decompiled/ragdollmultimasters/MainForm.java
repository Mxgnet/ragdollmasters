/*
 * Decompiled with CFR 0.152.
 */
package ragdollmultimasters;

import Network.NetworkUtilities;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.SpinnerNumberModel;
import ragdollmultimasters.ClientGame;
import ragdollmultimasters.GameInstance;
import ragdollmultimasters.HostGame;

public class MainForm
extends JFrame {
    private boolean host;
    private JButton btnDConnect;
    private ButtonGroup btnGroupControl;
    private JButton btnHost;
    private JLabel jLabel1;
    private JLabel jLabel2;
    private JLabel jLabel3;
    private JLabel jLabel4;
    private JLabel jLabel5;
    private JLabel jLabel6;
    private JPanel jPanel1;
    private JPanel jPanel2;
    private JSeparator jSeparator1;
    private JLabel lblIP;
    private JLabel lblUsername;
    private JRadioButton rbtnArrows;
    private JRadioButton rbtnWASD;
    private JSpinner spnQuality;
    private JTextField txfIP;
    private JTextField txfUsername;

    public MainForm() {
        this.initComponents();
        this.lblIP.setText(NetworkUtilities.getIntIP() + " : " + NetworkUtilities.getExtIP());
        this.setLocationRelativeTo(null);
    }

    public boolean isHost() {
        return this.host;
    }

    public void setHost(boolean host) {
        this.host = host;
    }

    private void initComponents() {
        this.btnGroupControl = new ButtonGroup();
        this.jSeparator1 = new JSeparator();
        this.jPanel1 = new JPanel();
        this.btnHost = new JButton();
        this.rbtnWASD = new JRadioButton();
        this.rbtnArrows = new JRadioButton();
        this.jLabel6 = new JLabel();
        this.jLabel4 = new JLabel();
        this.spnQuality = new JSpinner();
        this.jLabel3 = new JLabel();
        this.jLabel5 = new JLabel();
        this.lblIP = new JLabel();
        this.jPanel2 = new JPanel();
        this.btnDConnect = new JButton();
        this.jLabel1 = new JLabel();
        this.txfIP = new JTextField();
        this.jLabel2 = new JLabel();
        this.lblUsername = new JLabel();
        this.txfUsername = new JTextField();
        this.setDefaultCloseOperation(3);
        this.setTitle("Ragdoll Multimasters");
        this.btnHost.setText("Start Hosting");
        this.btnHost.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent evt) {
                MainForm.this.btnHostActionPerformed(evt);
            }
        });
        this.btnGroupControl.add(this.rbtnWASD);
        this.rbtnWASD.setText("WASD");
        this.btnGroupControl.add(this.rbtnArrows);
        this.rbtnArrows.setSelected(true);
        this.rbtnArrows.setText("Arrows");
        this.jLabel6.setText("Host controls: ");
        this.jLabel4.setText("Quality:");
        this.spnQuality.setModel(new SpinnerNumberModel(48, 1, 255, 1));
        this.jLabel3.setFont(new Font("Tahoma", 1, 14));
        this.jLabel3.setText("Host:");
        this.jLabel5.setText("(Max 255, Recommended 48)");
        this.lblIP.setHorizontalAlignment(4);
        this.lblIP.setText("IP");
        GroupLayout jPanel1Layout = new GroupLayout(this.jPanel1);
        this.jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.btnHost, -1, -1, Short.MAX_VALUE).addGroup(jPanel1Layout.createSequentialGroup().addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.TRAILING, false).addComponent(this.jLabel3, -1, -1, Short.MAX_VALUE).addComponent(this.jLabel4)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addComponent(this.spnQuality, -1, 167, Short.MAX_VALUE).addGap(18, 18, 18).addComponent(this.jLabel5)).addComponent(this.lblIP, -1, -1, Short.MAX_VALUE))).addGroup(jPanel1Layout.createSequentialGroup().addComponent(this.jLabel6).addGap(18, 18, 18).addComponent(this.rbtnArrows).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.rbtnWASD))).addContainerGap()));
        jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.jLabel3).addComponent(this.lblIP)).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.spnQuality, -2, -1, -2).addComponent(this.jLabel4).addComponent(this.jLabel5)).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.rbtnArrows).addComponent(this.jLabel6).addComponent(this.rbtnWASD)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.btnHost).addContainerGap(-1, Short.MAX_VALUE)));
        this.btnDConnect.setText("Direct Connect");
        this.btnDConnect.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent evt) {
                MainForm.this.btnDConnectActionPerformed(evt);
            }
        });
        this.jLabel1.setText("IP Address:");
        this.txfIP.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent evt) {
                MainForm.this.txfIPActionPerformed(evt);
            }
        });
        this.jLabel2.setFont(new Font("Tahoma", 1, 18));
        this.jLabel2.setText("Join:");
        GroupLayout jPanel2Layout = new GroupLayout(this.jPanel2);
        this.jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addContainerGap().addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.jLabel2, -1, -1, Short.MAX_VALUE).addComponent(this.btnDConnect, -1, -1, Short.MAX_VALUE).addGroup(jPanel2Layout.createSequentialGroup().addComponent(this.jLabel1).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.txfIP))).addContainerGap()));
        jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addContainerGap().addComponent(this.jLabel2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.jLabel1).addComponent(this.txfIP, -2, -1, -2)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.btnDConnect).addContainerGap(-1, Short.MAX_VALUE)));
        this.lblUsername.setText("Username:");
        GroupLayout layout = new GroupLayout(this.getContentPane());
        this.getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING, false).addComponent(this.jPanel1, -1, -1, Short.MAX_VALUE).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(this.jSeparator1)).addComponent(this.jPanel2, -1, -1, Short.MAX_VALUE)).addGap(0, 0, Short.MAX_VALUE)).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(this.lblUsername).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.txfUsername).addContainerGap()));
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addContainerGap(-1, Short.MAX_VALUE).addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.lblUsername).addComponent(this.txfUsername, -2, -1, -2)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.jPanel1, -2, -1, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.jSeparator1, -2, -1, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.jPanel2, -2, -1, -2)));
        this.pack();
    }

    private void btnDConnectActionPerformed(ActionEvent evt) {
        GameInstance.USERNAME = this.txfUsername.getText();
        this.dispose();
        new Thread(new ClientGame(this.txfIP.getText())).start();
    }

    private void btnHostActionPerformed(ActionEvent evt) {
        GameInstance.USERNAME = this.txfUsername.getText();
        this.dispose();
        new Thread(new HostGame((byte)((Integer)this.spnQuality.getValue()).intValue(), !this.rbtnArrows.isSelected())).start();
    }

    private void txfIPActionPerformed(ActionEvent evt) {
        this.btnDConnect.doClick();
    }
}

