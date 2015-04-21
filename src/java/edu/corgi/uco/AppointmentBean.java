/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.corgi.uco;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.sql.DataSource;


/**
 *
 * @author vdpotvin
 */
@Named(value = "appointmentBean")
@RequestScoped
public class AppointmentBean {
    
    @Resource(name = "jdbc/corgiDatabase")
            private DataSource ds;
            

    private ArrayList<AppointmentEvent> appointments;
    
    public void init(){
        appointments = new ArrayList<>();
    }
    
    public ArrayList<Student> getStudents(AppointmentEvent ae) {
        return ae.getStudents();
    }
    
    public ArrayList<AppointmentEvent> getAppointments(){
        if(appointments == null) appointments = new ArrayList<>();
        try(Connection conn = ds.getConnection()) {
            PreparedStatement query = conn.prepareStatement("select * from appointment");
            ResultSet rs = query.executeQuery();
            while(rs.next()) {
                AppointmentEvent ae = new AppointmentEvent();
                ae.setStartDate(rs.getTimestamp("startdate"));
                ae.setEndDate(rs.getTimestamp("enddate"));
                ae.setAppointmentID(rs.getInt("appointmentid"));
                String s = "select * from appointment_slots natural join usertable "
                        + "where appointmentid= ?";
                query = conn.prepareStatement(s, Statement.RETURN_GENERATED_KEYS);
                query.setInt(1,ae.getAppointmentID());
                ResultSet srs = query.executeQuery();
                
                while(srs.next()){
                    Student stud = new Student();
                    stud.setId(srs.getString("ucoid"));
                    stud.setFirstName(srs.getString("firstname"));
                    stud.setLastName(srs.getString("lastname"));
                    stud.setEmail("email");
                    ae.addStudent(stud);
                }
                if(ae.getStudents().size() > 0) appointments.add(ae);
            }
        } catch (SQLException ex) {
            Logger.getLogger(AppointmentBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return appointments;
    }
}
