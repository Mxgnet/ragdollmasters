/*
 * Decompiled with CFR 0.152.
 */
package ragdollmultimasters;

import Network.NetworkUtilities;
import java.awt.AWTException;
import java.awt.DisplayMode;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.SocketTimeoutException;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.MemoryCacheImageOutputStream;
import javax.swing.JOptionPane;
import ragdollmultimasters.GameInstance;
import ragdollmultimasters.HostingDialog;

public class HostGame
extends GameInstance {
    private Robot keyBot;
    private int maxReadAmount = 25;
    private ImageWriteParam pngParams;
    private ImageWriter pngWriter;
    private DatagramPacket sendPacket;
    private HostingDialog hostDialog;
    private boolean sendingScreenData = true;

    public HostGame(byte transferResolutionMultiplier, boolean sendArrows) {
        this.transferResolutionMultiplier = transferResolutionMultiplier;
        this.sendArrows = sendArrows;
        this.pngWriter = ImageIO.getImageWritersByFormatName("PNG").next();
        this.pngParams = this.pngWriter.getDefaultWriteParam();
    }

    @Override
    public void start() {
        this.hostDialog = new HostingDialog(this);
        this.setupConnections();
        System.out.println();
        if (this.tcpConnection != null) {
            this.syncData();
            System.out.println("Host res: " + this.hostResolutionWidth + "x" + this.hostResolutionHeight);
            System.out.println("Trans res: " + 4 * this.transferResolutionMultiplier + "x" + 3 * this.transferResolutionMultiplier);
            System.out.println("Host controls: " + (this.sendArrows ? "WASD" : "Arrows"));
            try {
                this.keyBot = new Robot();
            }
            catch (AWTException ex) {
                System.err.println("Failed to create robot");
                this.running = false;
            }
            this.sendPacket = new DatagramPacket(this.byteBuffer, this.byteBuffer.length, this.tcpConnection.getInetAddress(), 8488);
            while (this.running) {
                if (this.sendingScreenData) {
                    this.sendScreenData();
                }
                this.checkForTCPData();
                this.checkForUDPData();
            }
            this.hostDialog.setHostDetails("Disconnecting");
            this.sendCloseCommand();
            this.closeConnections();
        }
        this.hostDialog.dispose();
        if (this.tcpConnection != null) {
            JOptionPane.showMessageDialog(null, "Connections closed", "Lost Connection", 1, null);
        }
        System.exit(10000);
    }

    private void syncData() {
        DisplayMode screen = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode();
        this.hostResolutionWidth = (short)1024;
        this.hostResolutionHeight = (short)screen.getHeight();
        byte[] sendData = new byte[]{0, (byte)(this.transferResolutionMultiplier - 129), 0};
        this.sendTCPData(sendData);
    }

    public void setupConnections() {
        block17: {
            this.acceptingConnections = true;
            this.running = true;
            ServerSocket tcpServer = null;
            try {
                tcpServer = new ServerSocket(8488);
                tcpServer.setSoTimeout(100);
                this.hostDialog.setHostDetails("Hosting on " + NetworkUtilities.getIntIP());
                this.hostDialog.setVisible(true);
                while (this.acceptingConnections && (this.tcpConnection == null || !this.tcpConnection.isConnected())) {
                    try {
                        this.tcpConnection = tcpServer.accept();
                    }
                    catch (SocketTimeoutException e) {
                        // empty catch block
                    }
                    try {
                        Thread.sleep(100L);
                    }
                    catch (InterruptedException ex) {}
                }
                if (this.tcpConnection != null && this.tcpConnection.isConnected()) {
                    System.out.println("Connection made with " + this.tcpConnection.getInetAddress());
                    DatagramPacket packet = new DatagramPacket(this.byteBuffer, this.byteBuffer.length);
                    this.udpConnection = new DatagramSocket(8488);
                    this.udpConnection.setSoTimeout(2000);
                    try {
                        this.udpConnection.receive(packet);
                        if (packet.getData().length > 0 && packet.getData()[0] == 123) {
                            int tcpAttempts = 0;
                            while (!this.sendTCPData(new byte[]{124}) && tcpAttempts < 10) {
                                ++tcpAttempts;
                                try {
                                    Thread.sleep(500L);
                                }
                                catch (InterruptedException ex) {}
                            }
                        }
                    }
                    catch (SocketTimeoutException e) {
                        System.out.println("UDP unsuccessful");
                        this.running = false;
                    }
                }
                tcpServer.close();
                if (this.tcpConnection != null) {
                    this.hostDialog.setHostDetails("Connected to " + this.tcpConnection.getInetAddress());
                    this.hostDialog.setConnected(true);
                    System.out.println("Connections have been setup");
                }
            }
            catch (IOException ex) {
                System.out.println("Connection setup failed");
                if (tcpServer == null) break block17;
                try {
                    tcpServer.close();
                }
                catch (IOException ex1) {
                    ex.printStackTrace(System.err);
                }
                ex.printStackTrace(System.err);
            }
        }
    }

    public boolean isAcceptingConnections() {
        return this.acceptingConnections;
    }

    public void setAcceptingConnections(boolean acceptingConnections) {
        this.acceptingConnections = acceptingConnections;
    }

    public int getBit(byte b, int position) {
        return b >> position & 1;
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
                case 4: {
                    int length = data[1] + 128;
                    String message = new String(data, 2, length);
                    this.sendMessage(message);
                    continue block4;
                }
            }
        }
    }

    private void sendScreenData() {
        byte[] screenData = this.retrieveScreenData();
        if (this.byteBuffer.length < screenData.length + 3) {
            this.byteBuffer = new byte[screenData.length + 3];
        }
        this.byteBuffer[0] = 7;
        this.byteBuffer[1] = (byte)(screenData.length >> 8 & 0xFF);
        this.byteBuffer[2] = (byte)screenData.length;
        System.arraycopy(screenData, 0, this.byteBuffer, 3, screenData.length);
        this.sendPacket.setData(this.byteBuffer);
        this.sendPacket.setLength(screenData.length + 3);
        try {
            this.udpConnection.send(this.sendPacket);
        }
        catch (IOException ex) {
            System.err.println("Could not send UDP Key Packet");
        }
    }

    public void sendMessage(String message) {
        byte[] messageArr = new byte[message.length() + 2];
        messageArr[0] = 5;
        messageArr[1] = (byte)(Math.min(255, message.length()) - 128);
        System.arraycopy(message.getBytes(), 0, messageArr, 2, Math.min(255, message.length()));
        if (this.sendTCPData(messageArr)) {
            this.hostDialog.appendChat(message);
        }
    }

    private void checkForUDPData() {
        DatagramPacket recPacket;
        block3: for (int packetsRead = 0; (recPacket = this.listenOnUDP(10)) != null && packetsRead < this.maxReadAmount; ++packetsRead) {
            byte[] data = recPacket.getData();
            if (recPacket.getLength() <= 0) continue;
            switch (data[0]) {
                case 6: {
                    byte keyByte = data[1];
                    if (this.getBit(keyByte, 0) == 1) {
                        this.keyBot.keyPress(this.sendArrows ? 38 : 87);
                    } else {
                        this.keyBot.keyRelease(this.sendArrows ? 38 : 87);
                    }
                    if (this.getBit(keyByte, 1) == 1) {
                        this.keyBot.keyPress(this.sendArrows ? 40 : 83);
                    } else {
                        this.keyBot.keyRelease(this.sendArrows ? 40 : 83);
                    }
                    if (this.getBit(keyByte, 2) == 1) {
                        this.keyBot.keyPress(this.sendArrows ? 37 : 65);
                    } else {
                        this.keyBot.keyRelease(this.sendArrows ? 37 : 65);
                    }
                    if (this.getBit(keyByte, 3) == 1) {
                        this.keyBot.keyPress(this.sendArrows ? 39 : 68);
                        continue block3;
                    }
                    this.keyBot.keyRelease(this.sendArrows ? 39 : 68);
                    continue block3;
                }
            }
        }
    }

    private byte[] retrieveScreenData() {
        BufferedImage capture = this.keyBot.createScreenCapture(new Rectangle(0, 0, 1024, 768));
        BufferedImage transImage = new BufferedImage(this.transferResolutionMultiplier * 4, this.transferResolutionMultiplier * 3, 10);
        Graphics2D g = (Graphics2D)transImage.getGraphics();
        g.drawImage(capture.getScaledInstance(this.transferResolutionMultiplier * 4, this.transferResolutionMultiplier * 3, 2), 0, 0, null);
        g.dispose();
        transImage.flush();
        ByteArrayOutputStream outByte = new ByteArrayOutputStream();
        MemoryCacheImageOutputStream out = new MemoryCacheImageOutputStream(outByte);
        try {
            this.pngWriter.setOutput(out);
            this.pngWriter.write(null, new IIOImage(transImage, null, null), this.pngParams);
            out.flush();
            byte[] data = outByte.toByteArray();
            outByte.close();
            out.close();
            return data;
        }
        catch (IOException ex) {
            ex.printStackTrace(System.err);
            return null;
        }
    }

    public boolean isSendingScreenData() {
        return this.sendingScreenData;
    }

    public void setSendingScreenData(boolean sendingScreenData) {
        this.sendingScreenData = sendingScreenData;
    }
}

