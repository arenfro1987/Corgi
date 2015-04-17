package edu.corgi.uco;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.sql.DataSource;

/**
 *
 * @author vdpotvin
 */
@Named(value = "allStudents")
@SessionScoped
public class AllStudents implements Serializable {

    @Resource(name = "jdbc/corgiDatabase")
    private DataSource ds;
    private Student selectedStudent;

    public Student getSelectedStudent() {
        return selectedStudent;
    }

    public void setSelectedStudent(Student selectedStudent) {
        this.selectedStudent = selectedStudent;
    }

    public void updateStudent(Student updateStudent) {
        try {

            if (ds == null) {
                throw new SQLException("ds is null; Can't get data source");
            }

            Connection conn = ds.getConnection();

            if (conn == null) {
                throw new SQLException("conn is null; Can't get db connection");
            }

            try {
                if (updateStudent.getPassword().isEmpty()) 
                {

                    PreparedStatement ps = conn.prepareStatement(
                            "update usertable set email = ?, ucoid = ?,  firstname = ?,"
                                    + "lastname = ? where userid = ?"
                    );
                    
                    ps.setString(1, updateStudent.getEmail());
                    ps.setString(2, updateStudent.getId());
                    ps.setString(3, updateStudent.getFirstName());
                    ps.setString(4, updateStudent.getLastName());
                    ps.setInt(5, updateStudent.getTableID());
                    ps.execute();

                } else {
                    System.out.print("trying statement");
                    PreparedStatement ps = conn.prepareStatement(
                            "update usertable set email = ?, ucoid = ?, password = ?, firstname = ?,"
                            + "lastname = ? where userid = ?"
                    );

                    ps.setString(1, updateStudent.getEmail());
                    ps.setString(2, updateStudent.getId());
                    ps.setString(3, SHA256Encrypt.encrypt(updateStudent.getPassword()));
                    ps.setString(4, updateStudent.getFirstName());
                    ps.setString(5, updateStudent.getLastName());
                    ps.setInt(6, updateStudent.getTableID());
                    ps.execute();
                }

                PreparedStatement ps2 = conn.prepareStatement(
                        "update grouptable set email = ? where userid = ?"
                );

                ps2.setString(1, updateStudent.getEmail());
                ps2.setInt(2, updateStudent.getTableID());
                ps2.execute();

            } finally {
                conn.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    public void deleteStudent(Student deleteStudent) {
        try {

            if (ds == null) {
                throw new SQLException("ds is null; Can't get data source");
            }

            Connection conn = ds.getConnection();

            if (conn == null) {
                throw new SQLException("conn is null; Can't get db connection");
            }

            try {
                

                    PreparedStatement ps = conn.prepareStatement(
                            "delete from usertable where userid = ?"
                    );                  
                    ps.setInt(1, deleteStudent.getTableID());
                    ps.execute();

                PreparedStatement ps2 = conn.prepareStatement(
                        "delete from grouptable where userid = ?"
                );
                ps2.setInt(1, deleteStudent.getTableID());
                ps2.execute();

            } finally {
                conn.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public List<Student> getAllStudents() {
        ArrayList<Student> sList = new ArrayList<>();

        try (Connection conn = ds.getConnection()) {
            String select = "select ucoid, firstname, lastname, email,USERID from "
                    + "usertable natural join grouptable where groupname = 'student' order by USERID ASC";

            PreparedStatement statement = conn.prepareStatement(select);

            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                Student s = new Student();
                s.setId(rs.getString("ucoid"));
                s.setEmail(rs.getString("email"));
                s.setFirstName(rs.getString("firstname"));
                s.setLastName(rs.getString("lastname"));
                s.setTableID(rs.getInt("USERID"));
                sList.add(s);

            }
        } catch (SQLException ex) {
            Logger.getLogger(AllStudents.class.getName()).log(Level.SEVERE, null, ex);
        }
        //:form:editStudent

        return sList;
    }

}
