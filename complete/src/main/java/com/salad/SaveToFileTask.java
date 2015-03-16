package com.salad;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;

public class SaveToFileTask implements Runnable {

	private String mail;

	public SaveToFileTask(String mail) {
		this.mail = mail;
	}

	@Override
	public void run() {
		synchronized (SaveToFileTask.class) {
			try {
				File f = new File("mails.txt");
				if (!f.exists()) {
					f.createNewFile();
				}
				try (PrintWriter out = new PrintWriter(new BufferedWriter(
						new FileWriter(f, true)))) {
					out.println(mail);
				}
			} catch (IOException e) {
				e.printStackTrace();
				System.err.println("Error saving mail: " + mail);
			}
		}
	}
}
