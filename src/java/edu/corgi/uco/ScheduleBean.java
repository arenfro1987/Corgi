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
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.servlet.http.Cookie;
import javax.sql.DataSource;

/**
 *
 * @author vdpotvin
 */
@Named(value = "scheduleBean")
@RequestScoped
public class ScheduleBean {

    private Schedule schedule;
    private String email;
    private String studentname;
    
    @Resource(name = "jdbc/corgiDatabase")
    private DataSource ds;

    public void setEmail(String email) {
        this.email = email;
    }
    
    @PostConstruct
    public void init(){
        try(Connection conn = ds.getConnection()){
            PreparedStatement query = conn.prepareStatement(
                    "select userid from usertable where email = ?", 
                    Statement.RETURN_GENERATED_KEYS);
            query.setString(1, email);
            ResultSet rs = query.executeQuery();
            int uid = 0;
            if(rs.next()) uid = rs.getInt("userid");
            
            query = conn.prepareStatement(
                    "select * from schedule natural join courseschedulelinkage "
                            + "natural join course where userid = ?", 
                    Statement.RETURN_GENERATED_KEYS);
            query.setInt(1, uid);
            rs = query.executeQuery();
            
            schedule = new Schedule(0, uid);
            int sid = 0;
            Boolean approved = false;
            while(rs.next()){
                sid = rs.getInt("scheduleid");
                approved = rs.getBoolean("approved");
                Course course = new Course();
                course.setDepartment(rs.getString("dept"));
                course.setCourseNumber(rs.getInt("coursenumber"));
                course.setHours(rs.getInt("hours"));
                course.setTitle(rs.getString("title"));
                schedule.addCourse(course);
            }
            
            schedule.setSid(sid);
            schedule.setApproved(approved);
            
            query = conn.prepareStatement("select firstname, lastname from usertable where email = ?", 
                    Statement.RETURN_GENERATED_KEYS);
            query.setString(1, email);
            rs = query.executeQuery();
            if(rs.next())
                studentname = rs.getString("firstname") + " " + rs.getString("lastname");
            
        } catch (SQLException ex) {
            Logger.getLogger(ScheduleBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String approveSchedule(){
        try(Connection conn = ds.getConnection()){
            PreparedStatement query = conn.prepareStatement(
                    "update schedule set approved = true where scheduleid = ?", 
                    Statement.RETURN_GENERATED_KEYS);
            query.setInt(1, schedule.getSid());
            query.executeQuery();
            schedule.setApproved(true);
        } catch (SQLException ex) {
            Logger.getLogger(ScheduleBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        sendEmails.sendSecretaryNotification(studentname);
        return "adminHome";
    }



    public String getStudentname() {
        return studentname;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public String getEmail() {
        return email;
    }
    
}
