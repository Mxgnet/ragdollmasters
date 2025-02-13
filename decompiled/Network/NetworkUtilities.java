/*
 * Decompiled with CFR 0.152.
 */
package Network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;

public class NetworkUtilities {
    public static String getExtIP() {
        try {
            URL whatismyip = new URL("http://checkip.amazonaws.com");
            BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));
            String ip = in.readLine();
            return ip;
        }
        catch (MalformedURLException ex) {
            return "No Network";
        }
        catch (IOException ex) {
            return "No Network";
        }
    }

    public static String getIntIP() {
        try {
            InetAddress IP = InetAddress.getLocalHost();
            return IP.getHostAddress();
        }
        catch (UnknownHostException ex) {
            return null;
        }
    }
}

