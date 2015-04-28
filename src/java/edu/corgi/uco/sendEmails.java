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
       
         System.out.print("hit send");
         Email email = new SimpleEmail();
         System.out.print("created email file");
         email.setDebug(true);
         email.setHostName("smtp.gmail.com");
         email.setAuthenticator(new DefaultAuthenticator("ucocorgi2@gmail.com", "ucodrsung"));
         email.setStartTLSEnabled(true);
         email.setSmtpPort(587);
         email.setFrom("ucocorgi@gmail.com", "UCO CS Secretary");
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
                email.setAuthenticator(new DefaultAuthenticator("ucocorgi2@gmail.com", "ucodrsung"));
                email.setStartTLSEnabled(true);
                email.setSmtpPort(587);
                email.setFrom("ucocorgi@gmail.com", "UCO CS Corgi");
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
                email.setAuthenticator(new DefaultAuthenticator("ucocorgi2@gmail.com", "ucodrsung"));
                email.setStartTLSEnabled(true);
                email.setSmtpPort(587);
                email.setFrom("ucocorgi@gmail.com", "UCO Advisement");
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
         email.setAuthenticator(new DefaultAuthenticator("ucocorgi2@gmail.com", "ucodrsung"));
         email.setStartTLSEnabled(true);
         email.setSmtpPort(587);
         email.setFrom("ucocorgi@gmail.com", "UCO CS Corgi");
         email.setSubject("Account Confirmation");
         email.setMsg(firstName + " " + lastName + " please go to the following address http://localhost:8080/Corgi/faces/accountAuth.xhtml "
                 + "and enter the token:" + token +" and the ID:"+id+" to confirm and activate your account");
         System.out.print("Email Address: "+email2);
         email.addTo(email2);
        
        
         
         email.send();
         
        
    }

    public static void sendStudentConfirmation(String fn, String ln, Date date) {
        
        try{
                System.out.print("hit send");
                Email email = new SimpleEmail();
                System.out.print("created email file");
                email.setDebug(true);
                email.setHostName("smtp.gmail.com");
                email.setAuthenticator(new DefaultAuthenticator("ucocorgi2@gmail.com", "ucodrsung"));
                email.setStartTLSEnabled(true);
                email.setSmtpPort(587);
                email.setFrom("ucocorgi@gmail.com", "UCO Advisement");
                email.setSubject("Advisement Update");
                email.setMsg(fn + " " + ln + ", you have signed up for an advisement appointment at "
                    + date + ". It is recommended that you use the Corgi system to define your "
                        + "preferred schedule for next semester prior to your meeting.");
                        
                System.out.print("Email Address: ucocorgi@gmail.com");
                email.addTo("ucocorgi@gmail.com");

                System.out.print("added values");

                email.send();
                System.out.print("sent");
            } catch (EmailException ex) {
                Logger.getLogger(sendEmails.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
    
    public static void sendSecretaryNotification(String name) {
        try {
            System.out.print("hit send");
            Email email = new SimpleEmail();
            System.out.print("created email file");
            email.setDebug(true);
            email.setHostName("smtp.gmail.com");
            email.setAuthenticator(new DefaultAuthenticator("ucocorgi2@gmail.com", "ucodrsung"));
            email.setStartTLSEnabled(true);
            email.setSmtpPort(587);
            email.setFrom("ucocorgi@gmail.com", "UCO Advisement");
            email.setSubject("Advisement");
            email.setMsg(name + "'s Schedule has been approved. "
                    + "Please remove their hold promptly so they may enroll.");
            
            System.out.print("Email Address: ucocorgi@gmail.com");
            email.addTo("ucosecretary@gmail.com");
            
            System.out.print("added values");
            
            email.send();
            System.out.print("sent");
        } catch (EmailException ex) {
            Logger.getLogger(sendEmails.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
