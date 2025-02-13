/*
 * Decompiled with CFR 0.152.
 */
package ragdollmultimasters;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import ragdollmultimasters.ClientGameForm;
import ragdollmultimasters.GameInstance;

public class ClientGame
extends GameInstance {
    private String keyBinary = "00000000";
    private DatagramPacket keyPacket;
    private boolean hasKeyBinaryChanged = true;
    private ClientGameForm display;
    private int maxReadAmount = 25;
    private String hostIP;
    private boolean sendKeyStrokes = true;

    public ClientGame(String ip) {
        this.hostIP = ip;
    }

    @Override
    public void start() {
        this.running = true;
        this.display = new ClientGameForm(this);
        this.display.displayLoadingDialog(this.hostIP);
        this.setupConnection();
        if (this.running) {
            this.syncData();
            System.out.println("Trans res: " + 4 * this.transferResolutionMultiplier + "x" + 3 * this.transferResolutionMultiplier);
            System.out.println("Host controls: " + (this.sendArrows ? "WASD" : "Arrows"));
            this.display.setVisible(true);
            long lastKeyTimestamp = 0L;
            while (this.running && this.display.isDisplayable()) {
                if (this.sendKeyStrokes && (this.hasKeyBinaryChanged || System.currentTimeMillis() - lastKeyTimestamp > 250L)) {
                    this.sendKeyData();
                    lastKeyTimestamp = System.currentTimeMillis();
                }
                this.checkForTCPData();
                this.checkForUDPData();
                try {
                    Thread.sleep(20L);
                }
                catch (InterruptedException interruptedException) {}
            }
            this.sendCloseCommand();
        }
        if (this.display.isDisplayable()) {
            this.display.disposeLoadingDialog();
            this.display.dispose();
        }
        this.closeConnections();
        System.exit(10000);
    }

    private void sendKeyData() {
        byte keyByte = (byte)Integer.parseInt(this.keyBinary, 2);
        if (this.keyPacket == null) {
            this.keyPacket = new DatagramPacket(new byte[]{6, keyByte}, 2, this.tcpConnection.getInetAddress(), 8488);
        }
        byte[] newData = this.keyPacket.getData();
        newData[1] = keyByte;
        this.keyPacket.setData(newData);
        try {
            this.udpConnection.send(this.keyPacket);
            this.hasKeyBinaryChanged = false;
        }
        catch (IOException ex) {
            System.err.println("Could not send UDP Key Packet");
        }
    }

    public void sendMessage(String message) {
        byte[] messageArr = new byte[message.length() + 2];
        messageArr[0] = 4;
        messageArr[1] = (byte)(Math.min(255, message.length()) - 128);
        System.arraycopy(message.getBytes(), 0, messageArr, 2, Math.min(255, message.length()));
        this.sendTCPData(messageArr);
    }

    private void checkForUDPData() {
        DatagramPacket recPacket;
        block5: for (int packetsRead = 0; (recPacket = this.listenOnUDP(10)) != null && packetsRead < this.maxReadAmount; ++packetsRead) {
            byte[] data = recPacket.getData();
            if (recPacket.getLength() <= 0) continue;
            switch (data[0]) {
                case 7: {
                    short length = (short)((data[1] << 8) + (data[2] & 0xFF));
                    if (recPacket.getLength() < length + 3) {
                        this.byteBuffer = new byte[length + 50];
                        continue block5;
                    }
                    try {
                        ByteArrayInputStream bain = new ByteArrayInputStream(data, 3, length);
                        BufferedImage transImage = ImageIO.read(bain);
                        bain.close();
                        if (transImage == null) continue block5;
                        this.display.setScreenImage(transImage);
                    }
                    catch (IOException ex) {
                        System.err.println("Could not load screen image");
                    }
                    continue block5;
                }
            }
        }
    }

    private void checkForTCPData() {
        byte[] data;
        block4: for (int packetsRead = 0; (data = this.listenOnTCP(10)) != null && packetsRead < this.maxReadAmount; ++packetsRead) {
            if (data.length <= 0) continue;
            switch (data[0]) {
                case 0: {
                    this.running = false;
                    continue block4;
                }
                case 5: {
                    int length = data[1] + 128;
                    this.display.appendChat(new String(data, 2, length));
                    continue block4;
                }
            }
        }
    }

    private void syncData() {
        byte[] data = null;
        do {
            data = this.listenOnTCP(1000);
            try {
                Thread.sleep(1000L);
            }
            catch (InterruptedException interruptedException) {
                // empty catch block
            }
        } while (this.tcpConnection.isConnected() && (data == null || data.length == 0 || data[0] != 0));
        if (data != null && data.length >= 10 && data[0] == 0) {
            this.transferResolutionMultiplier = (byte)(data[1] + 129);
            this.sendArrows = data[2] != 1;
        } else {
            this.running = false;
        }
    }

    private void setupConnection() {
        this.tcpConnection = new Socket();
        try {
            this.tcpConnection.setSoTimeout(10);
            this.tcpConnection.connect(new InetSocketAddress(this.hostIP, 8488));
            System.out.println("TCP Connection to " + this.tcpConnection.getInetAddress() + " successful");
            this.udpConnection = new DatagramSocket(8488);
            DatagramPacket pingPacket = new DatagramPacket(new byte[]{123}, 1, this.tcpConnection.getInetAddress(), 8488);
            byte[] tcpResponse = null;
            int updsSent = 1;
            do {
                tcpResponse = this.listenOnTCP(100);
                this.udpConnection.send(pingPacket);
                System.out.println("UDP connection attempt " + updsSent);
            } while (!this.tcpConnection.isClosed() && (tcpResponse == null || tcpResponse.length > 0 && tcpResponse[0] != 124) && ++updsSent < 100);
            this.display.hideLoadingDialog();
            if (updsSent < 100) {
                System.out.println("UDP connection successful");
            } else {
                JOptionPane.showMessageDialog(null, "Could not connect to " + this.hostIP + ":8848");
            }
        }
        catch (IOException ex) {
            Logger.getLogger(ClientGame.class.getName()).log(Level.SEVERE, null, ex);
            this.display.hideLoadingDialog();
            JOptionPane.showMessageDialog(null, "Could not connect to " + this.hostIP + ":8848");
            this.running = false;
        }
    }

    public int getBit(byte b, int position) {
        return b >> position & 1;
    }

    public void setKeyState(int bitPosition, boolean pressed) {
        char[] characterArr = this.keyBinary.toCharArray();
        characterArr[bitPosition] = pressed ? 49 : 48;
        this.keyBinary = new String(characterArr);
        this.hasKeyBinaryChanged = true;
    }

    public boolean isSendKeyStrokes() {
        return this.sendKeyStrokes;
    }

    public void setSendKeyStrokes(boolean sendKeyStrokes) {
        this.sendKeyStrokes = sendKeyStrokes;
    }
}

