package com.salad;

import java.util.Properties;

import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

@Component
public class SaladMailSender extends JavaMailSenderImpl {

    public SaladMailSender() {

        Properties props = new Properties();
        props.put("mail.smtp.auth", Conf.getEnv().getProperty("mail.auth.boolean"));
        props.put("mail.smtp.host", Conf.getEnv().getProperty("mail.host"));
        props.put("mail.smtp.port", Conf.getEnv().getProperty("mail.port"));
        props.put("mail.user", Conf.getEnv().getProperty("mail.username"));
        props.put("mail.password", Conf.getEnv().getProperty("mail.password"));

        setJavaMailProperties(props);
        setUsername(Conf.getEnv().getProperty("mail.username"));
        setPassword(Conf.getEnv().getProperty("mail.password"));
    }

}
