package com.salad;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;

import org.springframework.stereotype.Component;

@Component
public class MailChecker {
    private File f;
    private ArrayList<String> mails;

    public MailChecker() throws IOException {
        f = new File("mailchecker.txt");
        if (!f.exists()) {
            f.createNewFile();
        }

        mails = readFile(f);
    }

    private ArrayList<String> readFile(File f) throws IOException {
        ArrayList<String> ips = new ArrayList<String>();
        try (BufferedReader reader = Files.newBufferedReader(f.toPath(), Charset.defaultCharset())) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                ips.add(line.trim());
            }
        }
        return ips;
    }

    public boolean checkMail(String mail) {
        if (mails.contains(mail)) {
            return false;
        }
        mails.add(mail);
        writeMailToFile(mail);
        return true;
    }

    private synchronized void writeMailToFile(String mail) {
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(f, true)))) {
            out.println(mail);
        } catch (IOException e) {
            System.err.println("Error saving mail: " + mail);
            e.printStackTrace();
        }
    }

}
