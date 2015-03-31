/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.corgi.uco;

import java.io.Serializable;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.enterprise.context.SessionScoped;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

/**
 *
 * @author Derek
 */
@Named(value = "sendEmails")
@SessionScoped
public class sendEmails implements Serializable {

    public void send(String emailAddress, String studentName) throws EmailException {
        Email email = new SimpleEmail();
        email.setHostName("smtp.googlemail.com");
        email.setSmtpPort(465);
        email.setAuthenticator(new DefaultAuthenticator("ucocorgi@gmail.com", "drsunguco"));
        email.setSSLOnConnect(true);
        email.setFrom("ucocorgi@gmail.com");
        email.setSubject("Advisement Update");
        email.setMsg(studentName + " your advisment has been processed and the hold on your account will be removed shortly");
        email.addTo(emailAddress);
        email.send();
    }

}
