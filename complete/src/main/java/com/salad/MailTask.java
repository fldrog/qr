package com.salad;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import net.glxn.qrgen.core.image.ImageType;
import net.glxn.qrgen.javase.QRCode;

import org.springframework.beans.factory.annotation.Autowired;

public class MailTask implements Runnable {

    private String mail;

    @Autowired
    private SaladMailSender mailSender;

    public MailTask(String mail, SaladMailSender mailSender2) {
        this.mail = mail;
        this.mailSender = mailSender2;
    }

    @Override
    public void run() {
        try {
            MimeMessage message = composeMessage(mailSender.getSession(), mail);
            mailSender.send(message);
        } catch (Exception ex) {
            // simply log it and go on...
            System.err.println(ex.getMessage());
        }
    }

    private MimeMessage composeMessage(Session session, String mail2) throws AddressException, MessagingException {
        MimeMessage message = new MimeMessage(session);
        message.setSubject(Conf.getEnv().getProperty("email.subject"));
        message.setFrom(new InternetAddress(Conf.getEnv().getProperty("email.from")));
        message.setReplyTo(new InternetAddress[] { new InternetAddress(Conf.getEnv().getProperty("email.from")) });
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(mail));

        //
        // This HTML mail have to 2 part, the BODY and the embedded image
        //
        MimeMultipart multipart = new MimeMultipart("related");

        // first part (the html)
        BodyPart messageBodyPart = new MimeBodyPart();
        String htmlText = "<H1>Present this coupon and receive one FREE salad. We'll let you know where to find us as soon as we open. In order to unsubscribe reply to this message with UNSUBSCRIBE in the subject.</H1><img src=\"cid:image\"> <h1> See you soon !</h1>";
        messageBodyPart.setContent(htmlText, "text/html");

        // add it
        multipart.addBodyPart(messageBodyPart);

        // GENERATE THE QR CODE AND SAVE IT TO DISK
        File f = generateQRCode();
        copyImageToDisk(f, mail);

        // second part (the image)
        messageBodyPart = new MimeBodyPart();
        DataSource fds = new FileDataSource(f.getAbsolutePath());
        messageBodyPart.setDataHandler(new DataHandler(fds));
        messageBodyPart.setHeader("Content-ID", "<image>");

        // add it
        multipart.addBodyPart(messageBodyPart);

        // put everything together
        message.setContent(multipart);
        return message;
    }

    private void copyImageToDisk(File f, String mail2) {
        try {
            Files.copy(f.toPath(), Paths.get("./" + mail + ".png"));
        } catch (IOException e) {
            System.err.println("Failed to save image for mail : " + mail);
            e.printStackTrace();

        }
    }

    private File generateQRCode() {
        return QRCode.from(new UUID(24, 24).toString()).to(ImageType.PNG).file("./" + mail);
    }

    public void setMailSender(SaladMailSender mailSender) {
        this.mailSender = mailSender;
    }
}
