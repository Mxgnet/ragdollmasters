/*
 * Decompiled with CFR 0.152.
 */
package ragdollmultimasters;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.SpinnerNumberModel;
import ragdollmultimasters.GameInstance;
import ragdollmultimasters.HostGame;

public class HostingDialog
extends JFrame {
    private HostGame game;
    private boolean connected = false;
    private JDialog OptionsDialog;
    private JButton btnCancel;
    private JButton btnDialogCancel;
    private JButton btnOptions;
    private JButton btnSendChat;
    private JButton btnUpdateOptions;
    private JCheckBox chckSendVideo;
    private JLabel jLabel1;
    private JLabel jLabel4;
    private JLabel jLabel5;
    private JLabel jLabel6;
    private JScrollPane jScrollPane1;
    private JSeparator jSeparator1;
    private JLabel lblHostDetails;
    private JPanel pnlHold;
    private JPanel pnlHostDetails;
    private JPanel pnlUtilities;
    private JRadioButton rbtnArrows;
    private JRadioButton rbtnWASD;
    private JSpinner spnQuality;
    private JTextArea txaChat;
    private JTextField txfChat;

    public HostingDialog(HostGame game) {
        this.game = game;
        this.initComponents();
        this.pnlUtilities.setVisible(false);
        this.setSize(372, 110);
        this.setLocationRelativeTo(null);
    }

    public void setHostDetails(String details) {
        this.lblHostDetails.setText(details);
    }

    public boolean isConnected() {
        return this.connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
        this.btnCancel.setText("Stop");
        this.pnlUtilities.setVisible(connected);
        this.setSize(372, 400);
        this.revalidate();
    }

    public void appendChat(String s) {
        this.txaChat.append(s + "\n");
    }

    private void initComponents() {
        this.OptionsDialog = new JDialog();
        this.btnUpdateOptions = new JButton();
        this.rbtnWASD = new JRadioButton();
        this.rbtnArrows = new JRadioButton();
        this.jLabel6 = new JLabel();
        this.jLabel4 = new JLabel();
        this.spnQuality = new JSpinner();
        this.jLabel5 = new JLabel();
        this.btnDialogCancel = new JButton();
        this.pnlHold = new JPanel();
        this.pnlUtilities = new JPanel();
        this.jSeparator1 = new JSeparator();
        this.chckSendVideo = new JCheckBox();
        this.btnOptions = new JButton();
        this.jScrollPane1 = new JScrollPane();
        this.txaChat = new JTextArea();
        this.btnSendChat = new JButton();
        this.jLabel1 = new JLabel();
        this.txfChat = new JTextField();
        this.pnlHostDetails = new JPanel();
        this.btnCancel = new JButton();
        this.lblHostDetails = new JLabel();
        this.OptionsDialog.setTitle("Options");
        this.btnUpdateOptions.setText("Update options");
        this.btnUpdateOptions.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent evt) {
                HostingDialog.this.btnUpdateOptionsActionPerformed(evt);
            }
        });
        this.rbtnWASD.setText("WASD");
        this.rbtnArrows.setSelected(true);
        this.rbtnArrows.setText("Arrows");
        this.jLabel6.setText("Host controls: ");
        this.jLabel4.setText("Quality:");
        this.spnQuality.setModel(new SpinnerNumberModel(48, 1, 255, 1));
        this.jLabel5.setText("(Max 255, Recommended 48)");
        this.btnDialogCancel.setText("Cancel");
        this.btnDialogCancel.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent evt) {
                HostingDialog.this.btnDialogCancelActionPerformed(evt);
            }
        });
        GroupLayout OptionsDialogLayout = new GroupLayout(this.OptionsDialog.getContentPane());
        this.OptionsDialog.getContentPane().setLayout(OptionsDialogLayout);
        OptionsDialogLayout.setHorizontalGroup(OptionsDialogLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(OptionsDialogLayout.createSequentialGroup().addContainerGap().addGroup(OptionsDialogLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.btnUpdateOptions, -1, -1, Short.MAX_VALUE).addGroup(GroupLayout.Alignment.TRAILING, OptionsDialogLayout.createSequentialGroup().addComponent(this.jLabel6).addGap(18, 18, 18).addComponent(this.rbtnWASD).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, -1, Short.MAX_VALUE).addComponent(this.rbtnArrows)).addGroup(GroupLayout.Alignment.TRAILING, OptionsDialogLayout.createSequentialGroup().addComponent(this.jLabel4).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.spnQuality).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.jLabel5)).addComponent(this.btnDialogCancel, -1, -1, Short.MAX_VALUE)).addContainerGap()));
        OptionsDialogLayout.setVerticalGroup(OptionsDialogLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(OptionsDialogLayout.createSequentialGroup().addContainerGap().addGroup(OptionsDialogLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.jLabel5).addComponent(this.jLabel4).addComponent(this.spnQuality, -2, -1, -2)).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addGroup(OptionsDialogLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.rbtnArrows).addComponent(this.jLabel6).addComponent(this.rbtnWASD)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.btnUpdateOptions).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.btnDialogCancel).addContainerGap(-1, Short.MAX_VALUE)));
        this.setDefaultCloseOperation(0);
        this.setTitle("Ragdoll MultiMaster - Host");
        this.pnlHold.setLayout(new BorderLayout());
        this.chckSendVideo.setSelected(true);
        this.chckSendVideo.setText("Send video stream");
        this.chckSendVideo.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent evt) {
                HostingDialog.this.chckSendVideoActionPerformed(evt);
            }
        });
        this.btnOptions.setText("Options");
        this.btnOptions.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent evt) {
                HostingDialog.this.btnOptionsActionPerformed(evt);
            }
        });
        this.txaChat.setEditable(false);
        this.txaChat.setColumns(20);
        this.txaChat.setRows(5);
        this.txaChat.setBorder(BorderFactory.createTitledBorder("Chat"));
        this.jScrollPane1.setViewportView(this.txaChat);
        this.btnSendChat.setText("Send");
        this.btnSendChat.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent evt) {
                HostingDialog.this.btnSendChatActionPerformed(evt);
            }
        });
        this.jLabel1.setText("Enter message:");
        this.txfChat.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent evt) {
                HostingDialog.this.txfChatActionPerformed(evt);
            }
        });
        GroupLayout pnlUtilitiesLayout = new GroupLayout(this.pnlUtilities);
        this.pnlUtilities.setLayout(pnlUtilitiesLayout);
        pnlUtilitiesLayout.setHorizontalGroup(pnlUtilitiesLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(pnlUtilitiesLayout.createSequentialGroup().addContainerGap().addGroup(pnlUtilitiesLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.jScrollPane1).addComponent(this.jSeparator1).addGroup(pnlUtilitiesLayout.createSequentialGroup().addComponent(this.chckSendVideo, -1, 229, Short.MAX_VALUE).addGap(18, 18, 18).addComponent(this.btnOptions, -2, 90, -2)).addGroup(GroupLayout.Alignment.TRAILING, pnlUtilitiesLayout.createSequentialGroup().addComponent(this.jLabel1).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.txfChat).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.btnSendChat))).addContainerGap()));
        pnlUtilitiesLayout.setVerticalGroup(pnlUtilitiesLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(pnlUtilitiesLayout.createSequentialGroup().addComponent(this.jSeparator1, -2, -1, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(pnlUtilitiesLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.chckSendVideo).addComponent(this.btnOptions)).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.jScrollPane1).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addGroup(pnlUtilitiesLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(this.btnSendChat).addComponent(this.jLabel1).addComponent(this.txfChat, -2, -1, -2)).addGap(13, 13, 13)));
        this.pnlHold.add((Component)this.pnlUtilities, "Center");
        this.pnlHostDetails.setPreferredSize(new Dimension(357, 70));
        this.btnCancel.setText("Cancel");
        this.btnCancel.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent evt) {
                HostingDialog.this.btnCancelActionPerformed(evt);
            }
        });
        this.lblHostDetails.setHorizontalAlignment(0);
        this.lblHostDetails.setText("Hosting on ");
        GroupLayout pnlHostDetailsLayout = new GroupLayout(this.pnlHostDetails);
        this.pnlHostDetails.setLayout(pnlHostDetailsLayout);
        pnlHostDetailsLayout.setHorizontalGroup(pnlHostDetailsLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(pnlHostDetailsLayout.createSequentialGroup().addContainerGap().addGroup(pnlHostDetailsLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.btnCancel, -1, 337, Short.MAX_VALUE).addComponent(this.lblHostDetails, -1, -1, Short.MAX_VALUE)).addContainerGap()));
        pnlHostDetailsLayout.setVerticalGroup(pnlHostDetailsLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(pnlHostDetailsLayout.createSequentialGroup().addContainerGap().addComponent(this.lblHostDetails, -2, 14, -2).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addComponent(this.btnCancel).addContainerGap(-1, Short.MAX_VALUE)));
        this.pnlHold.add((Component)this.pnlHostDetails, "First");
        GroupLayout layout = new GroupLayout(this.getContentPane());
        this.getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.pnlHold, -2, -1, -2));
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.pnlHold, -1, -1, Short.MAX_VALUE));
        this.pack();
    }

    private void btnCancelActionPerformed(ActionEvent evt) {
        if (!this.connected) {
            this.game.setAcceptingConnections(false);
        } else {
            this.game.stop();
        }
    }

    private void btnOptionsActionPerformed(ActionEvent evt) {
        this.OptionsDialog.setVisible(true);
    }

    private void chckSendVideoActionPerformed(ActionEvent evt) {
        this.game.setSendingScreenData(this.chckSendVideo.isSelected());
    }

    private void btnDialogCancelActionPerformed(ActionEvent evt) {
        this.setVisible(false);
    }

    private void btnUpdateOptionsActionPerformed(ActionEvent evt) {
    }

    private void btnSendChatActionPerformed(ActionEvent evt) {
        this.game.sendMessage(GameInstance.USERNAME + ": " + this.txfChat.getText());
        this.txfChat.setText("");
    }

    private void txfChatActionPerformed(ActionEvent evt) {
        this.game.sendMessage(GameInstance.USERNAME + ": " + this.txfChat.getText());
        this.txfChat.setText("");
    }
}

