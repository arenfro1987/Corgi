/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.corgi.uco;

import java.util.Date;
import org.primefaces.model.DefaultScheduleEvent;

/**
 *
 * @author vdpotvin
 */
public class AppointmentEvent extends DefaultScheduleEvent{
    private Student student;
    private int appointmentID;
    
    public AppointmentEvent(String title, Date start, Date end, int id){
        super(title, start, end);
        this.appointmentID = id;
    } 

    AppointmentEvent() {
        super();
        appointmentID = 0;
    }
    
    public void setStudent(Student student) {
        this.student = student;
    }
}
