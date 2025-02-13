/*
 * Decompiled with CFR 0.152.
 */
package Files;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileUtilities {
    public static Charset getCharset(File in) throws IOException {
        Charset charset = Charset.defaultCharset();
        try {
            InputStreamReader r = new InputStreamReader(new FileInputStream(in));
            charset = Charset.forName(r.getEncoding());
            r.close();
        }
        catch (FileNotFoundException ex) {
            Logger.getLogger(FileUtilities.class.getName()).log(Level.SEVERE, null, ex);
        }
        in = null;
        return charset;
    }

    public static String[] readToArray(Path file) throws IOException {
        String[] fileContents;
        int listSize;
        Charset charset = FileUtilities.getCharset(file.toFile());
        try (BufferedReader reader1 = Files.newBufferedReader(file, charset);){
            listSize = 0;
            while (reader1.readLine() != null) {
                ++listSize;
            }
            reader1.close();
        }
        var5_3 = null;
        try (BufferedReader reader2 = Files.newBufferedReader(file, charset);){
            String line;
            fileContents = new String[listSize];
            int counter = 0;
            while ((line = reader2.readLine()) != null) {
                fileContents[counter] = line;
                ++counter;
            }
            reader2.close();
        }
        catch (Throwable throwable) {
            var5_3 = throwable;
            throw throwable;
        }
        return fileContents;
    }

    public static void writeToFile(String[] toWrite, Path file, boolean overwrite) throws IOException {
        String[] newWrite;
        if (!overwrite) {
            int i;
            String[] oldFile = FileUtilities.readToArray(file);
            newWrite = new String[oldFile.length + toWrite.length];
            for (i = 0; i < oldFile.length; ++i) {
                System.out.println("old:" + oldFile[i]);
                newWrite[i] = oldFile[i];
            }
            for (i = 0; i < toWrite.length; ++i) {
                newWrite[i + oldFile.length] = toWrite[i];
            }
        } else {
            newWrite = toWrite;
        }
        Charset charset = FileUtilities.getCharset(file.toFile());
        try (BufferedWriter writer = Files.newBufferedWriter(file, charset, new OpenOption[0]);){
            for (int i = 0; i < newWrite.length; ++i) {
                System.out.println(newWrite[i]);
                writer.write(newWrite[i]);
                if (i == newWrite.length - 1) continue;
                writer.newLine();
            }
            writer.close();
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public static String[] readDirectory(String path) {
        int listSize = new File(path).listFiles().length;
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(path, new String[0]));){
            int counter = 0;
            String[] arrayList = new String[listSize];
            for (Path file : stream) {
                arrayList[counter] = "" + file.getFileName();
                ++counter;
            }
            stream.close();
            String[] stringArray = arrayList;
            return stringArray;
        }
        catch (IOException | DirectoryIteratorException x) {
            String[] arrayList = new String[1];
            arrayList[1] = "failed";
            System.err.println(x);
            return arrayList;
        }
    }
}

