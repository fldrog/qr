package com.salad;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

	@RequestMapping(method = RequestMethod.POST, name = "/mail")
	public void index(@RequestParam(value = "mail") final String mail) {
		executors.execute(new MailTask(mail, mailSender));
		executors.execute(new SaveToFileTask(mail));
	}

}
