/*
 * Decompiled with CFR 0.152.
 */
package Files;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import javax.swing.JOptionPane;

public class FileManager {
    private final String internalPath;
    private final String appdataPath;
    public static String projectPath;
    private boolean resource;

    public FileManager(String path, boolean resource) {
        this.internalPath = path;
        this.appdataPath = System.getProperty("os.name").toLowerCase().contains("windows") ? System.getenv("appdata") : System.getProperty("user.home");
        this.resource = resource;
    }

    public FileManager(String path) {
        this(path, false);
    }

    public String[] getProperties(String[] keys) throws FileNotFoundException {
        String[] result = new String[keys.length];
        try (Scanner scnFile = new Scanner(new File(this.getPath()));){
            while (scnFile.hasNextLine()) {
                String line = scnFile.nextLine();
                for (int i = 0; i < keys.length; ++i) {
                    if (line.startsWith(keys[i] + "=[")) {
                        result[i] = line.replace(keys[i] + "=[", "[\n");
                        boolean inArray = true;
                        while (scnFile.hasNextLine() && inArray) {
                            line = scnFile.nextLine();
                            if (line.contains("]")) {
                                inArray = false;
                                continue;
                            }
                            int n = i;
                            result[n] = result[n] + line + "\n";
                        }
                        result[i] = result[i].trim();
                        continue;
                    }
                    if (!line.startsWith(keys[i] + "=")) continue;
                    result[i] = line.replace(keys[i] + "=", "");
                }
            }
        }
        return result;
    }

    public String getProperty(String key) throws FileNotFoundException {
        return this.getProperties(new String[]{key})[0];
    }

    public void setProperties(String[] keys, String[] values, String def) throws FileNotFoundException, IOException {
        ArrayList<String> lines = new ArrayList<String>();
        Scanner scnFile = new Scanner(new File(this.getPath()));
        while (scnFile.hasNextLine()) {
            String line = scnFile.nextLine();
            for (int i = 0; i < keys.length; ++i) {
                if (keys[i] == null || !line.startsWith(keys[i] + "=")) continue;
                if (values[i].startsWith("[") && values[i].endsWith("]")) {
                    String s;
                    lines.add(keys[i] + "=[");
                    values[i] = values[i].replaceFirst("\\[", "");
                    String[] stringArray = values[i].split("\n");
                    int n = stringArray.length;
                    for (int j = 0; j < n && !(s = stringArray[j]).equals("]"); ++j) {
                        lines.add(s);
                    }
                    if (line.startsWith(keys[i] + "=[")) {
                        while (scnFile.hasNextLine() && !scnFile.nextLine().equals("]")) {
                        }
                    }
                    line = "]";
                    keys[i] = null;
                    continue;
                }
                line = keys[i] + "=" + values[i];
                keys[i] = null;
            }
            lines.add(line);
        }
        scnFile.close();
        for (int i = 0; i < keys.length; ++i) {
            if (keys[i] == null) continue;
            if (values[i] != null) {
                if (values[i].startsWith("[") && values[i].endsWith("]")) {
                    lines.add(keys[i] + "=[");
                    values[i] = values[i].replaceFirst("\\[", "");
                    for (String s : values[i].split("\n")) {
                        if (s.equals("]")) break;
                        lines.add(s);
                    }
                    lines.add("]");
                    keys[i] = null;
                    continue;
                }
                lines.add(keys[i] + "=" + values[i]);
                keys[i] = null;
                continue;
            }
            lines.add(keys[i] + "=" + def);
        }
        try (PrintWriter pw = new PrintWriter(new File(this.getPath()));){
            for (String l : lines) {
                if (l.isEmpty()) continue;
                pw.println(l);
            }
            pw.flush();
        }
    }

    public void setProperties(String[] keys, String[] values) throws FileNotFoundException, IOException {
        this.setProperties(keys, values, "");
    }

    public void setProperty(String key, String value, String def) throws FileNotFoundException, IOException {
        this.setProperties(new String[]{key}, new String[]{value}, def);
    }

    public void setProperty(String key, String value) throws FileNotFoundException, IOException {
        this.setProperties(new String[]{key}, new String[]{value}, "");
    }

    public void addProperties(HashMap<String, String> properties) throws FileNotFoundException {
        ArrayList<Object> lines = new ArrayList<Object>();
        Scanner scnFile = new Scanner(new File(this.getPath()));
        while (scnFile.hasNextLine()) {
            String line = scnFile.nextLine();
            if (line.contains("=")) {
                properties.remove(line.split("=")[0]);
            }
            lines.add(line);
        }
        scnFile.close();
        for (String key : properties.keySet()) {
            String value = properties.get(key).trim();
            if (value.startsWith("[") && value.endsWith("]")) {
                lines.add(key + "=[");
                value = value.replaceFirst("\\[", "");
                for (String s : value.split("\n")) {
                    lines.add(s);
                }
                continue;
            }
            lines.add(key + "=");
        }
        try (PrintWriter pw = new PrintWriter(new File(this.getPath()));){
            for (String string : lines) {
                if (string.isEmpty()) continue;
                pw.println(string);
            }
            pw.flush();
        }
    }

    public String getPath() {
        if (this.resource) {
            return this.internalPath;
        }
        return this.appdataPath + "/" + projectPath + "/" + this.internalPath;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public boolean setupFile(String[] vars) {
        boolean existing;
        File f = new File(this.getPath());
        f.getParentFile().mkdirs();
        if (!f.getParentFile().canWrite() || !f.getParentFile().canRead()) {
            f = new File(System.getProperty("user.dir") + "/" + this.internalPath);
        }
        if (!(existing = f.exists())) {
            try {
                f.createNewFile();
                if (vars == null || vars.length <= 0) return true;
                try (PrintWriter pw = new PrintWriter(new FileWriter(this.getPath()));){
                    for (String v : vars) {
                        pw.println(v + "=");
                    }
                    pw.flush();
                    return true;
                }
            }
            catch (IOException ex) {
                ex.printStackTrace(System.err);
                JOptionPane.showMessageDialog(null, "Config files could not be created.\nEnsure that the .jar is in an unrestricted folder", "Error", 0, null);
                return false;
            }
        }
        if (vars == null) return true;
        HashMap<String, String> propMap = new HashMap<String, String>();
        for (String v : vars) {
            propMap.put(v, "");
        }
        try {
            this.addProperties(propMap);
            return true;
        }
        catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Config files could not be created successfully.\nEnsure that the .jar is in an unrestricted folder", "Error", 0, null);
            return false;
        }
    }

    public boolean setupFile() {
        return this.setupFile(null);
    }

    public static String[][] readPropertyArray(String s) {
        int i;
        Scanner scan = new Scanner(s);
        ArrayList<String> vars = new ArrayList<String>();
        ArrayList<String> instances = new ArrayList<String>();
        while (scan.hasNextLine()) {
            String line = scan.nextLine();
            if (line.startsWith("v ")) {
                vars.add(line.replaceFirst("v ", ""));
                continue;
            }
            if (!line.startsWith("i ")) continue;
            instances.add(line.replaceFirst("i ", ""));
        }
        scan.close();
        String[][] result = new String[instances.size() + 1][vars.size()];
        for (i = 0; i < vars.size(); ++i) {
            result[0][i] = (String)vars.get(i);
        }
        i = 1;
        for (String ins : instances.toArray(new String[0])) {
            int c = 0;
            String[] stringArray = ins.split("//");
            int n = stringArray.length;
            for (int j = 0; j < n; ++j) {
                String split;
                result[i][c] = split = stringArray[j];
                ++c;
            }
            ++i;
        }
        return result;
    }

    public static String[][] readPropertyArray(String s, String[] reference) {
        int col;
        int row;
        String[][] prop = FileManager.readPropertyArray(s);
        String[][] res = new String[prop.length][reference.length];
        res[0] = reference;
        HashMap<String, String> propMap = new HashMap<String, String>();
        for (row = 1; row < prop.length; ++row) {
            for (col = 0; col < prop[0].length; ++col) {
                propMap.put("#" + row + "//" + prop[0][col], prop[row][col]);
            }
        }
        for (row = 1; row < res.length; ++row) {
            for (col = 0; col < reference.length; ++col) {
                res[row][col] = (String)propMap.get("#" + row + "//" + reference[col]);
            }
        }
        return res;
    }

    public static String createPropertyArray(String[][] s) {
        String result = "";
        for (String var : s[0]) {
            result = result + "v " + var + "\n";
        }
        for (int i = 1; i < s.length; ++i) {
            String line = "";
            for (String instance : s[i]) {
                line = line + "//" + instance;
            }
            line = line.replaceFirst("//", "");
            result = result + "i " + line + "\n";
        }
        result = "[\n" + result.trim() + "\n]";
        return result;
    }
}

