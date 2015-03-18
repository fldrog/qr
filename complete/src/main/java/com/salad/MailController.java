package com.salad;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MailController {
    private ExecutorService executors = Executors.newFixedThreadPool(2);

    @Autowired
    private SaladMailSender mailSender;

    @Autowired
    private MailChecker mailChecker;

    @Autowired
    private EmailValidator emailValidator;

    @RequestMapping(method = RequestMethod.POST, name = "/mail")
    public void index(HttpServletRequest req, @RequestParam(value = "mail") final String mail) {
        System.out.println(req.getHeader("X-Forwarded-For"));
        if (emailValidator.validate(mail) && mailChecker.checkMail(mail)) {
            executors.execute(new MailTask(mail, mailSender));
            executors.execute(new SaveToFileTask(mail, req.getRemoteAddr()));
        } else {
            System.out.println("MAIL ALREADY USED: " + mail);
        }
    }

}
