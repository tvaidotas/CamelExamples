package camel.filecopyjava;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class FileCopier {

    public static void main(String args[]) throws Exception {
        initiateCopying("inbox", "outbox");
    }

    private static void initiateCopying(String inbox, String outbox) throws Exception  {
        File inboxDirectory = new File(inbox); // have this directory in the root of the project
        File outboxDirectory = new File(outbox); // will be created if the directory doesn't exist
        outboxDirectory.mkdir(); // creates directory
        copyFiles(inboxDirectory, outboxDirectory); // initialises file copying
    }

    private static void copyFiles(File inbox, File outbox) throws Exception {
        // goes through all the files in the inbox and copies them to outbox using the copyFile()
        File[] files = inbox.listFiles();
        for (File source : files) {
            if (source.isFile()) {
                File dest = new File(outbox.getPath() + File.separator + source.getName());
                copyFile(source, dest);
            }
        }
    }

    private static void copyFile(File source, File dest) throws IOException {
        OutputStream out = new FileOutputStream(dest);
        byte[] buffer = new byte[(int) source.length()];
        FileInputStream in = new FileInputStream(source);
        in.read(buffer);
        try {
            out.write(buffer);
        } finally {
            out.close();
            in.close();
        }
    }

}
