/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.corgi.uco;

import java.io.Serializable;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.enterprise.context.SessionScoped;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.Message.RecipientType;
import javax.mail.PasswordAuthentication;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author Derek
 */
@Named(value = "sendEmails")
@SessionScoped
public class sendEmails implements Serializable {

    public String send(String emailAddress, String studentFirstName, String studentLastName) throws EmailException {
        /*
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        
        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("ucocorgi@gmail.com", "drsunguco");
                    }
                });
        Transport transport = session.getTransport("smtp");
        try {
            Message message = new MimeMessage(session);
            System.out.print("made message");
            message.setFrom(new InternetAddress("ucocorgi@gmail.com"));
            System.out.print("set from");
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(emailAddress));
            System.out.print("set to");
            message.setSubject("Advisement Update");
            System.out.print("set sub");
            message.setText(studentFirstName + " " + studentLastName + " "
                    + "your advisment has been processed and the hold "
                    + "on your account will be removed shortly");
            System.out.print("set message");
            Transport.send(message);

            System.out.println("Done");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
           
        }

        */
         System.out.print("hit send");
         Email email = new SimpleEmail();
         System.out.print("created email file");
         email.setDebug(true);
         email.setHostName("smtp.gmail.com");
         email.setAuthenticator(new DefaultAuthenticator("ucocorgi@gmail.com", "drsunguco"));
         email.setStartTLSEnabled(true);
         email.setSmtpPort(587);
         email.setFrom("ucocorgi@yahoo.com", "UCO CS Secretary");
         email.setSubject("Advisement Update");
         email.setMsg(studentFirstName + " " + studentLastName + " your advisment has been processed and the hold on your account will be removed shortly");
         System.out.print("Email Address: "+emailAddress);
         email.addTo(emailAddress);
        
         System.out.print("added values");
         
         email.send();
         System.out.print("sent");
         
        return null;
    }
    
        public static void sendAdvisorCancel(String emailAddress, String studentFirstName, String studentLastName) {
            try {
                System.out.print("hit send");
                Email email = new SimpleEmail();
                System.out.print("created email file");
                email.setDebug(true);
                email.setHostName("smtp.gmail.com");
                email.setAuthenticator(new DefaultAuthenticator("ucocorgi@gmail.com", "drsunguco"));
                email.setStartTLSEnabled(true);
                email.setSmtpPort(587);
                email.setFrom("ucocorgi@yahoo.com", "UCO CS Secretary");
                email.setSubject("Advisement Update");
                email.setMsg(studentFirstName + " " + studentLastName + 
                        "your appointment has been canceled by the advisor."
                        + "You will need to log in to CORGI and sign up for another appointment to get advised."
                        + "Thank you.");
                System.out.print("Email Address: "+emailAddress);
                email.addTo(emailAddress);

                System.out.print("added values");

                email.send();
                System.out.print("sent");
            } catch (EmailException ex) {
                Logger.getLogger(sendEmails.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    public static void sendStudentSignUp( String studentFirstName, String studentLastName, Date time) {
            try {
                System.out.print("hit send");
                Email email = new SimpleEmail();
                System.out.print("created email file");
                email.setDebug(true);
                email.setHostName("smtp.gmail.com");
                email.setAuthenticator(new DefaultAuthenticator("ucocorgi@gmail.com", "drsunguco"));
                email.setStartTLSEnabled(true);
                email.setSmtpPort(587);
                email.setFrom("ucocorgi@yahoo.com", "UCO CS Secretary");
                email.setSubject("Advisement Update");
                email.setMsg("You have a new appointment with " + studentFirstName + " " 
                        + studentLastName + "on " + time + ". Any previously "
                        + "scheduled appointments with them have been canceled.");
                        
                System.out.print("Email Address: ucocorgi@gmail.com");
                email.addTo("ucocorgi@gmail.com");

                System.out.print("added values");

                email.send();
                System.out.print("sent");
            } catch (EmailException ex) {
                Logger.getLogger(sendEmails.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    
    public void sendConfirmation(String email2,String firstName,String lastName,int token,int id) throws EmailException
    {
        
         Email email = new SimpleEmail();
         
         email.setDebug(true);
         email.setHostName("smtp.gmail.com");
         email.setAuthenticator(new DefaultAuthenticator("ucocorgi@gmail.com", "drsunguco"));
         email.setStartTLSEnabled(true);
         email.setSmtpPort(587);
         email.setFrom("ucocorgi@gmail.com", "UCO CS Corgi");
         email.setSubject("Account Confirmation");
         email.setMsg(firstName + " " + lastName + " please go to the following address "
                 + "and enter the token:" + token +" and the ID:"+id+" to confirm and activate your account");
         System.out.print("Email Address: "+email2);
         email.addTo(email2);
        
        
         
         email.send();
         
        
    }

}
