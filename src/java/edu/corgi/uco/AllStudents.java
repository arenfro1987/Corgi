
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
public class AllStudents implements Serializable{

    @Resource(name = "jdbc/corgiDatabase")
    private DataSource ds;
    
    public List<Student> getAllStudents() {
        ArrayList<Student> sList = new ArrayList<>();
        
        try(Connection conn = ds.getConnection()) {
            String select = "select ucoid, firstname, lastname, email from "
                    + "usertable natural join grouptable where groupname = 'student'";
            
            PreparedStatement statement = conn.prepareStatement(select);
            
            ResultSet rs = statement.executeQuery();
            
            while(rs.next()) {
                Student s = new Student();
                s.setId(rs.getString("ucoid"));
                s.setEmail(rs.getString("email"));
                s.setFirstName(rs.getString("firstname"));
                s.setLastName(rs.getString("lastname"));
                sList.add(s);
            }
        } catch (SQLException ex) {
            Logger.getLogger(AllStudents.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return sList;
    }
    
}
