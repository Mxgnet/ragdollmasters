/*
 * Decompiled with CFR 0.152.
 */
package ragdollmultimasters;

import CustomComponents.ImagePanel;
import Images.ImageCover;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import ragdollmultimasters.ClientGame;
import ragdollmultimasters.GameInstance;

public class ClientGameForm
extends JFrame {
    private ClientGame game;
    private boolean[] keyStates = new boolean[4];
    private JButton btnSend;
    private JCheckBox chckSendKeys;
    private JScrollPane jScrollPane1;
    private JSeparator jSeparator1;
    private JLabel lblMessage;
    private JDialog loadingDialog;
    private JPanel pnlChat;
    private ImagePanel pnlImage;
    private JTextArea txaChat;
    private JTextField txfChatMessage;

    public ClientGameForm(ClientGame game) {
        this.game = game;
        this.initComponents();
        this.pnlChat.setVisible(true);
        this.pnlImage.setFocusable(true);
        this.loadingDialog.pack();
        this.loadingDialog.setLocationRelativeTo(null);
    }

    public void setScreenImage(Image img) {
        float resMultiplier = Math.min((float)this.getWidth() / 4.0f, (float)this.getHeight() / 3.0f);
        ImageCover pic = new ImageCover(img.getScaledInstance((int)(4.0f * resMultiplier), (int)(3.0f * resMultiplier), 2));
        this.pnlImage.setImage(pic);
        this.repaint();
    }

    public void displayLoadingDialog(String ip) {
        this.lblMessage.setText("Attempting to connect to " + ip);
        this.loadingDialog.setVisible(true);
    }

    public void hideLoadingDialog() {
        this.loadingDialog.setVisible(false);
    }

    public void disposeLoadingDialog() {
        this.loadingDialog.dispose();
    }

    public void appendChat(String message) {
        this.txaChat.append(message + "\n");
    }

    private void initComponents() {
        this.loadingDialog = new JDialog();
        this.lblMessage = new JLabel();
        this.pnlImage = new ImagePanel();
        this.pnlChat = new JPanel();
        this.chckSendKeys = new JCheckBox();
        this.jScrollPane1 = new JScrollPane();
        this.txaChat = new JTextArea();
        this.btnSend = new JButton();
        this.txfChatMessage = new JTextField();
        this.jSeparator1 = new JSeparator();
        this.loadingDialog.setDefaultCloseOperation(0);
        this.loadingDialog.setTitle("Connecting...");
        this.lblMessage.setHorizontalAlignment(0);
        this.lblMessage.setText("Attempting to connect to ");
        GroupLayout loadingDialogLayout = new GroupLayout(this.loadingDialog.getContentPane());
        this.loadingDialog.getContentPane().setLayout(loadingDialogLayout);
        loadingDialogLayout.setHorizontalGroup(loadingDialogLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(loadingDialogLayout.createSequentialGroup().addContainerGap().addComponent(this.lblMessage, -1, 380, Short.MAX_VALUE).addContainerGap()));
        loadingDialogLayout.setVerticalGroup(loadingDialogLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(loadingDialogLayout.createSequentialGroup().addContainerGap().addComponent(this.lblMessage).addContainerGap(-1, Short.MAX_VALUE)));
        this.setDefaultCloseOperation(2);
        this.pnlImage.setBackground(new Color(51, 51, 51));
        this.pnlImage.addMouseListener(new MouseAdapter(){

            @Override
            public void mousePressed(MouseEvent evt) {
                ClientGameForm.this.pnlImageMousePressed(evt);
            }
        });
        this.pnlImage.addKeyListener(new KeyAdapter(){

            @Override
            public void keyPressed(KeyEvent evt) {
                ClientGameForm.this.pnlImageKeyPressed(evt);
            }

            @Override
            public void keyReleased(KeyEvent evt) {
                ClientGameForm.this.pnlImageKeyReleased(evt);
            }
        });
        GroupLayout pnlImageLayout = new GroupLayout(this.pnlImage);
        this.pnlImage.setLayout(pnlImageLayout);
        pnlImageLayout.setHorizontalGroup(pnlImageLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGap(0, 424, Short.MAX_VALUE));
        pnlImageLayout.setVerticalGroup(pnlImageLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGap(0, 428, Short.MAX_VALUE));
        this.chckSendKeys.setSelected(true);
        this.chckSendKeys.setText("Send key strokes");
        this.chckSendKeys.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent evt) {
                ClientGameForm.this.chckSendKeysActionPerformed(evt);
            }
        });
        this.txaChat.setEditable(false);
        this.txaChat.setColumns(20);
        this.txaChat.setRows(5);
        this.txaChat.setBorder(BorderFactory.createTitledBorder("Chat"));
        this.jScrollPane1.setViewportView(this.txaChat);
        this.btnSend.setText("Send");
        this.btnSend.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent evt) {
                ClientGameForm.this.btnSendActionPerformed(evt);
            }
        });
        this.txfChatMessage.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent evt) {
                ClientGameForm.this.txfChatMessageActionPerformed(evt);
            }
        });
        this.jSeparator1.setOrientation(1);
        GroupLayout pnlChatLayout = new GroupLayout(this.pnlChat);
        this.pnlChat.setLayout(pnlChatLayout);
        pnlChatLayout.setHorizontalGroup(pnlChatLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(pnlChatLayout.createSequentialGroup().addComponent(this.jSeparator1, -2, -1, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addGroup(pnlChatLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.txfChatMessage).addComponent(this.chckSendKeys, GroupLayout.Alignment.TRAILING, -1, -1, Short.MAX_VALUE).addComponent(this.jScrollPane1, -1, 189, Short.MAX_VALUE).addComponent(this.btnSend, -1, -1, Short.MAX_VALUE)).addContainerGap()));
        pnlChatLayout.setVerticalGroup(pnlChatLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(pnlChatLayout.createSequentialGroup().addContainerGap().addGroup(pnlChatLayout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(pnlChatLayout.createSequentialGroup().addComponent(this.chckSendKeys).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.jScrollPane1).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.txfChatMessage, -2, -1, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.btnSend)).addComponent(this.jSeparator1)).addContainerGap()));
        GroupLayout layout = new GroupLayout(this.getContentPane());
        this.getContentPane().setLayout(layout);
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(this.pnlImage, -2, -1, -2).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(this.pnlChat, -2, -1, -2)));
        layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(this.pnlImage, -1, -1, Short.MAX_VALUE).addComponent(this.pnlChat, -1, -1, Short.MAX_VALUE));
        this.pack();
    }

    private void pnlImageKeyPressed(KeyEvent evt) {
        switch (evt.getKeyCode()) {
            case 38: 
            case 87: {
                if (!this.keyStates[0]) {
                    this.game.setKeyState(7, true);
                }
                this.keyStates[0] = true;
                break;
            }
            case 40: 
            case 83: {
                if (!this.keyStates[1]) {
                    this.game.setKeyState(6, true);
                }
                this.keyStates[1] = true;
                break;
            }
            case 37: 
            case 65: {
                if (!this.keyStates[2]) {
                    this.game.setKeyState(5, true);
                }
                this.keyStates[2] = true;
                break;
            }
            case 39: 
            case 68: {
                if (!this.keyStates[3]) {
                    this.game.setKeyState(4, true);
                }
                this.keyStates[3] = true;
                break;
            }
        }
    }

    private void pnlImageKeyReleased(KeyEvent evt) {
        switch (evt.getKeyCode()) {
            case 38: 
            case 87: {
                if (this.keyStates[0]) {
                    this.game.setKeyState(7, false);
                }
                this.keyStates[0] = false;
                break;
            }
            case 40: 
            case 83: {
                if (this.keyStates[1]) {
                    this.game.setKeyState(6, false);
                }
                this.keyStates[1] = false;
                break;
            }
            case 37: 
            case 65: {
                if (this.keyStates[2]) {
                    this.game.setKeyState(5, false);
                }
                this.keyStates[2] = false;
                break;
            }
            case 39: 
            case 68: {
                if (this.keyStates[3]) {
                    this.game.setKeyState(4, false);
                }
                this.keyStates[3] = false;
                break;
            }
        }
    }

    private void chckSendKeysActionPerformed(ActionEvent evt) {
        this.game.setSendKeyStrokes(this.chckSendKeys.isSelected());
        this.pnlImage.grabFocus();
    }

    private void btnSendActionPerformed(ActionEvent evt) {
        this.game.sendMessage(GameInstance.USERNAME + ": " + this.txfChatMessage.getText());
        this.txfChatMessage.setText("");
    }

    private void txfChatMessageActionPerformed(ActionEvent evt) {
        this.game.sendMessage(GameInstance.USERNAME + ": " + this.txfChatMessage.getText());
        this.txfChatMessage.setText("");
    }

    private void pnlImageMousePressed(MouseEvent evt) {
        this.pnlImage.grabFocus();
    }
}

