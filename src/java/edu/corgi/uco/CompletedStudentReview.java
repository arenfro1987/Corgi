/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.corgi.uco;

import java.io.Serializable;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;

/**
 *
 * @author Derek
 */
@Named(value = "completedStudentReview")
@SessionScoped
public class CompletedStudentReview implements Serializable {

    private String studentFirstName;
    private String studentLastName;
    private Date meetingDate;
    private String studentEmail;
    private String ucoID;
    

    @PostConstruct
    public void init() {
        studentFirstName = null;
        studentLastName = null;
        meetingDate = null;
        studentEmail = null;
        ucoID = null;
    }

    public String getUcoID() {
        return ucoID;
    }

    public void setUcoID(String ucoID) {
        this.ucoID = ucoID;
    }
    
    public String getStudentEmail() {
        return studentEmail;
    }

    public void setStudentEmail(String studentEmail) {
        this.studentEmail = studentEmail;
    }

    public String getStudentFirstName() {
        return studentFirstName;
    }

    public void setStudentFirstName(String studentFirstName) {
        this.studentFirstName = studentFirstName;
    }

    public String getStudentLastName() {
        return studentLastName;
    }

    public void setStudentLastName(String studentLastName) {
        this.studentLastName = studentLastName;
    }

    public Date getMeetingDate() {
        return meetingDate;
    }

    public void setMeetingDate(Date meetingDate) {
        this.meetingDate = meetingDate;
    }

}
