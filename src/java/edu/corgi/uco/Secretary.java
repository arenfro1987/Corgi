/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.corgi.uco;


import java.io.InputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.sql.DataSource;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author Derek
 */
@Named(value = "secretary")
@SessionScoped
public class Secretary implements Serializable  
{
    private List<CompletedStudentReview> Students;
    private StreamedContent file;
    
    @Resource(name = "jdbc/database2")
    private DataSource ds;
    
    @PostConstruct
    public void init()
    {
        Students = new ArrayList<>();
        InputStream stream = ((ServletContext)FacesContext.getCurrentInstance().getExternalContext().getContext()).getResourceAsStream("/Enroll.docx");
        file = new DefaultStreamedContent(stream, "application/docx", "Enroll.docx");
        
    }

    public List<CompletedStudentReview> getStudents() {
        return Students;
    }

    public void setStudents(List<CompletedStudentReview> Students) {
        this.Students = Students;
    }

    public List<CompletedStudentReview> getStudentList() throws SQLException {
        
        try 
        {
            if (Students.size() == 0) {
            if (ds == null) {
                throw new SQLException("ds is null; Can't get data source");
            }

            Connection conn = ds.getConnection();

            if (conn == null) {
                throw new SQLException("conn is null; Can't get db connection");
            }

            try {
                PreparedStatement ps = conn.prepareStatement(
                        "select * from Schedule join UserTable on Schedule.userID = UserTable.userID"
                                + " join Appointment on Schedule.userID = Appointment.userID "
                                + "where approved = true and holdRemoved = false"
                );

                ResultSet result = ps.executeQuery();

                while (result.next()) {
                    CompletedStudentReview b = new CompletedStudentReview();
                    b.setStudentFirstName(result.getString("FIRSTNAME"));
                    b.setStudentLastName(result.getString("LASTNAME"));
                    b.setMeetingDate(result.getDate("appointmentDate"));
                    b.setStudentEmail(result.getString("email"));
                    Students.add(b);
                }
                return Students;
            } finally {
                conn.close();
            }
        }
            
        }
        catch(Exception e)
        {
            
        }
        
        return Students;
    }

    public StreamedContent getFile() {
        return file;
    }

    public void setFile(StreamedContent file) {
        this.file = file;
    }
    
    
    
    
}
