/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.corgi.uco;


import java.io.InputStream;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.Dependent;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
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
        
        return Students;
    }

    public StreamedContent getFile() {
        return file;
    }

    public void setFile(StreamedContent file) {
        this.file = file;
    }
    
    
    
    
}
