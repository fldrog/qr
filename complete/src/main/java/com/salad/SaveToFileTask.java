package com.salad;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class SaveToFileTask implements Runnable {

    private String mail;
    private String ip;

    public SaveToFileTask(String mail, String ip) {
        this.mail = mail;
        this.ip = ip;
    }

    @Override
    public void run() {
        synchronized (SaveToFileTask.class) {
            try {
                File f = new File(Conf.getEnv().getProperty("mails.file"));
                if (!f.exists()) {
                    f.createNewFile();
                }
                try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(f, true)))) {
                    out.println(mail + "-" + ip);
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Error saving mail: " + mail);
            }
        }
    }
}
