/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.corgi.uco;


import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.sql.DataSource;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 *
 * @author vdpotvin
 */
@Named(value = "userBean")
@SessionScoped
public class UserBean implements Serializable {
    
    @Resource(name = "jdbc/corgiDatabase")
    private DataSource dataSource;
    
    @NotNull(message="Email cannot be empty.")
    @Pattern(regexp="^[a-zA-Z0-9]+@uco\\.edu", message="A valid email of the format "
            + "'person@uco.edu' is required." )
    private String email;
    
    @NotNull(message="Please enter your UCO ID")
    @Size(min=8, max=8, message="Please check your ID and try again.")
    private String ucoId;
    
    @NotNull(message="Please enter a password at least 4 characters long.")
    @Size(min=3, message="Password must be at least 4 characters long.")
    private String password;
    
    @NotNull(message="First Name cannot be empty.")
    @Pattern(regexp="[A-Za-z]++", message="First name is required and "
            + "only letters can be used.")
    private String firstName;
    
    @NotNull(message="Last Name cannot be empty.")
    @Pattern(regexp="[A-Za-z]++", message="Last name is required and only "
            + "letters can be used.")
    private String lastName;

    @NotNull(message="Please select a major.")
    private String major;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUcoId() {
        return ucoId;
    }

    public void setUcoId(String ucoId) {
        this.ucoId = ucoId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = SHA256Encrypt.encrypt(password);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }
    
    
    public UserBean() {
    }
    
    public String addSecretary() {
        try(Connection conn = dataSource.getConnection()){
            PreparedStatement addUser = conn.prepareStatement(
                    "insert into UserTable (email, password, firstname, lastname) values (?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);


            addUser.setString(1, email);
            addUser.setString(2, SHA256Encrypt.encrypt("temp1234"));
            addUser.setString(3, firstName);
            addUser.setString(4, lastName);

            addUser.executeUpdate();
            
            ResultSet results = addUser.getGeneratedKeys();
            
            
            //for whatever really fun reason it says userid is not a field
            int id = 0;
            while(results.next()) {
                id = results.getInt(1);
            }
            
            PreparedStatement addUserToGroup = conn.prepareStatement(
                "insert into GroupTable (userID, groupname, email) values (?, 'secretary', ?)");
            
            addUserToGroup.setInt(1, id);
            addUserToGroup.setString(2, email);
            
            addUserToGroup.executeUpdate(); 
        } catch (SQLException ex) {
            Logger.getLogger(UserBean.class.getName()).log(Level.SEVERE, null, ex);
            FacesContext.getCurrentInstance().addMessage(email, new FacesMessage("Something went wrong. Please try again later."));
        }
        FacesContext.getCurrentInstance().addMessage(email, new FacesMessage("Success"));
        return null;
    }
    
    //adds a student user form signup to the database
    public String add() throws SQLException {
      
        if (dataSource == null) {
            throw new SQLException("DataSource is null");
        }

        Connection connection = dataSource.getConnection();

        if (connection == null) {
            throw new SQLException("Connection");
        }
        
        try {
              
            PreparedStatement addUser = connection.prepareStatement(
                "insert into UserTable (email, ucoID, password, firstName, lastName) values (?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);


            addUser.setString(1, email);
            addUser.setString(2, ucoId);
            addUser.setString(3, password);
            addUser.setString(4, firstName);
            addUser.setString(5, lastName);

            addUser.executeUpdate();
            
            ResultSet results = addUser.getGeneratedKeys();
            
            
            //for whatever really fun reason it says userid is not a field
            int id = 0;
            while(results.next()) {
                id = results.getInt(1);
            }
            
            PreparedStatement addUserToGroup = connection.prepareStatement(
                "insert into GroupTable (userID, groupname, email) values (?, 'student', ?)");
            
            addUserToGroup.setInt(1, id);
            addUserToGroup.setString(2, email);
            
            addUserToGroup.executeUpdate();            
            
            PreparedStatement addMajor = connection.prepareStatement(
                "insert into MajorCodes (userID, majorCode) values (?, ?)");
        
            addMajor.setInt(1, id);
            addMajor.setString(2, major);
            
            addMajor.executeUpdate();
            
        }
        
        finally {
            connection.close();
        }
                
        return "thanks";     
    }

 
    
}
