/*
 * Decompiled with CFR 0.152.
 */
package ragdollmultimasters;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;

public abstract class GameInstance
implements Runnable {
    public static final byte SET_DATA = 0;
    public static final byte CLOSE_CONNECTION = 0;
    public static final byte SEND_MESSAGE = 4;
    public static final byte DISPLAY_MESSAGE = 5;
    public static final byte REGISTER_CLIENT_KEY_STATES = 6;
    public static final byte REGISTER_SCREENSHOT = 7;
    public static final byte GET_LOBBY_DETAILS = 8;
    public static final byte REGISTER_LOBBY_DETAILS = 9;
    public static final byte HOST_CONTROLS_WASD = 0;
    public static final byte HOST_CONTROLS_ARROWS = 1;
    public static final byte UP_KEY_BIT = 0;
    public static final byte DOWN_KEY_BIT = 1;
    public static final byte LEFT_KEY_BIT = 2;
    public static final byte RIGHT_KEY_BIT = 3;
    public static final byte TOP_LEFT_QUARTER = 0;
    public static final byte TOP_RIGHT_QUARTER = 1;
    public static final byte BOTTOM_LEFT_QUARTER = 2;
    public static final byte BOTTOM_RIGHT_QUARTER = 3;
    public static final byte FULL_SCREEN_SHOT = 4;
    protected boolean acceptingConnections = false;
    protected boolean running = false;
    protected short hostResolutionWidth = (short)1024;
    protected short hostResolutionHeight = (short)768;
    protected short transferResolutionMultiplier;
    protected boolean sendArrows;
    protected Socket tcpConnection;
    protected DatagramSocket udpConnection;
    protected byte[] byteBuffer = new byte[256];
    protected int bufferLength = 0;
    public static String USERNAME = System.getProperty("user.name");

    protected boolean sendTCPData(byte[] data) {
        try {
            OutputStream out = this.tcpConnection.getOutputStream();
            out.write(data);
            out.flush();
        }
        catch (IOException ex) {
            return false;
        }
        return true;
    }

    protected byte[] listenOnTCP(int timeout) {
        try {
            this.tcpConnection.setSoTimeout(timeout);
            InputStream in = this.tcpConnection.getInputStream();
            this.bufferLength = in.read(this.byteBuffer);
            return this.byteBuffer;
        }
        catch (IOException iOException) {
            return null;
        }
    }

    protected DatagramPacket listenOnUDP(int timeout) {
        try {
            this.udpConnection.setSoTimeout(timeout);
            DatagramPacket recPacket = new DatagramPacket(this.byteBuffer, this.byteBuffer.length, this.tcpConnection.getInetAddress(), 8488);
            this.udpConnection.receive(recPacket);
            this.bufferLength = recPacket.getLength();
            return recPacket;
        }
        catch (IOException iOException) {
            return null;
        }
    }

    protected boolean sendUDPData(byte[] data) {
        DatagramPacket packet = new DatagramPacket(data, data.length);
        try {
            this.udpConnection.send(packet);
        }
        catch (IOException ex) {
            System.err.println("Could not send UDP Key Packet");
            return false;
        }
        return true;
    }

    protected void sendCloseCommand() {
        this.sendTCPData(new byte[]{0});
    }

    protected void closeConnections() {
        if (this.tcpConnection != null) {
            try {
                this.tcpConnection.shutdownInput();
                this.tcpConnection.shutdownOutput();
                this.tcpConnection.close();
                this.tcpConnection.close();
            }
            catch (IOException iOException) {
                // empty catch block
            }
        }
        if (this.udpConnection != null) {
            this.udpConnection.close();
        }
    }

    public void stop() {
        this.running = false;
    }

    public abstract void start();

    @Override
    public void run() {
        this.start();
    }
}

