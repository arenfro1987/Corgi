/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.corgi.uco;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.sql.DataSource;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author Derek
 */
@Named(value = "secretary")
@SessionScoped
public class Secretary implements Serializable {

    private List<CompletedStudentReview> Students;
    private StreamedContent file;

    @Resource(name = "jdbc/corgiDatabase")
    private DataSource ds;

    @PostConstruct
    public void init() {
        Students = new ArrayList<>();
        InputStream stream = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getResourceAsStream("/Enroll.docx");
        file = new DefaultStreamedContent(stream, "application/docx", "Enroll.docx");

    }

    public List<CompletedStudentReview> getStudents() {
        return Students;
    }

    public void setStudents(List<CompletedStudentReview> Students) {
        this.Students = Students;
    }

    public void downloadReportData() throws IOException {
        if (!Students.isEmpty()) {
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet();
            HSSFRow row0 = sheet.createRow(0);
            HSSFCell cell0 = row0.createCell(0);
            cell0.setCellValue("First Name");
            HSSFCell cell01 = row0.createCell(1);
            cell01.setCellValue("Last Name");
            HSSFCell cell02 = row0.createCell(2);
            cell02.setCellValue("Email");
            HSSFCell cell03 = row0.createCell(3);
            cell03.setCellValue("UCO ID");
            HSSFCell cell04 = row0.createCell(4);
            cell04.setCellValue("Appointment Date");
            
            for(int x = 0 ; x < Students.size() ; x++)
            {
               HSSFRow row = sheet.createRow(x+1);
               HSSFCell cell = row.createCell(0);
               cell.setCellValue(Students.get(x).getStudentFirstName());
               HSSFCell cell1 = row.createCell(1);
               cell1.setCellValue(Students.get(x).getStudentLastName()); 
               HSSFCell cell2 = row.createCell(2);
               cell2.setCellValue(Students.get(x).getStudentEmail()); 
               HSSFCell cell3 = row.createCell(3);
               cell3.setCellValue(Students.get(x).getUcoID()); 
               HSSFCell cell4 = row.createCell(4);
               cell4.setCellValue(Students.get(x).getMeetingDate().toString());
            }
            
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = facesContext.getExternalContext();
            externalContext.setResponseContentType("application/vnd.ms-excel");
            externalContext.setResponseHeader("Content-Disposition", "attachment; filename=\"report.xls\"");

            workbook.write(externalContext.getResponseOutputStream());
            facesContext.responseComplete();

        }

    }

    public List<CompletedStudentReview> getStudentList() throws SQLException {
        System.out.print("hit get student");
        try {
            if (Students.size() == 0) {
                if (ds == null) {
                    throw new SQLException("ds is null; Can't get data source");
                }

                Connection conn = ds.getConnection();

                if (conn == null) {
                    throw new SQLException("conn is null; Can't get db connection");
                }

                try {
                    /*
                     DatabaseMetaData md = conn.getMetaData();
                     ResultSet rs = md.getTables(null, null, "%", null);
                     while (rs.next()) {
                     System.out.println(rs.getString(3));
                     }
                     */
                    System.out.print("trying statement");
                    PreparedStatement ps = conn.prepareStatement(
                            "select * from schedule natural join usertable natural "
                            + "join appointment_slots natural join appointment "
                            + "where approved = true and holdremoved = false"     
                    );
                    System.out.print("made query");
                    ResultSet result = ps.executeQuery();
                    System.out.print("execute query");
                    while (result.next()) {
                        CompletedStudentReview b = new CompletedStudentReview();
                        Timestamp ad = result.getTimestamp("startdate");
                        Calendar c = Calendar.getInstance();
                        c.setTime(ad);
                        Date sd = c.getTime();
                        
                        b.setStudentFirstName(result.getString("FIRSTNAME"));
                        b.setStudentLastName(result.getString("LASTNAME"));
                        b.setMeetingDate(sd);
                        b.setStudentEmail(result.getString("email"));
                        b.setUcoID(result.getString("UCOID"));
                        b.setMeetingDate(result.getDate("startdate"));
                        Students.add(b);
                    }
                    return Students;
                } finally {
                    conn.close();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
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
