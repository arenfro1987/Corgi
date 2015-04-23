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
import java.sql.Timestamp;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.sql.DataSource;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import org.apache.commons.mail.EmailException;

/**
 *
 * @author vdpotvin
 */
@Named(value = "userBean")
@SessionScoped
public class UserBean implements Serializable {

    @Resource(name = "jdbc/corgiDatabase")
    private DataSource dataSource;

    @NotNull(message = "Email cannot be empty.")
    @Pattern(regexp = "^[a-zA-Z0-9]+@uco\\.edu", message = "A valid email of the format "
            + "'person@uco.edu' is required.")
    private String email;

    @NotNull(message = "Please enter your UCO ID")
    @Size(min = 8, max = 8, message = "Please check your ID and try again.")
    private String ucoId;

    @NotNull(message = "Please enter a password at least 4 characters long.")
    @Size(min = 3, message = "Password must be at least 4 characters long.")
    private String password;

    @NotNull(message = "First Name cannot be empty.")
    @Pattern(regexp = "[A-Za-z]++", message = "First name is required and "
            + "only letters can be used.")
    private String firstName;

    @NotNull(message = "Last Name cannot be empty.")
    @Pattern(regexp = "[A-Za-z]++", message = "Last name is required and only "
            + "letters can be used.")
    private String lastName;

    @NotNull(message = "Please select a major.")
    private String major;

    @NotNull(message = "Please enter Security Token")
    int token;

    @NotNull(message = "Please enter User ID")
    int userID;

    public int getToken() {
        return token;
    }

    public void setToken(int token) {
        this.token = token;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

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
        try (Connection conn = dataSource.getConnection()) {
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
            while (results.next()) {
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
    public String add() throws SQLException, EmailException {

        if (dataSource == null) {
            throw new SQLException("DataSource is null");
        }

        Connection connection = dataSource.getConnection();

        if (connection == null) {
            throw new SQLException("Connection");
        }

        try {

            Random rand = new Random();
            int randomNum = rand.nextInt((999999999 - 100000000) + 1) + 100000000;

            PreparedStatement addUser = connection.prepareStatement(
                    "insert into UserTable (email, ucoID, password, firstName, lastName,authKey) values (?, ?, ?, ?, ?,?)",
                    Statement.RETURN_GENERATED_KEYS);

            addUser.setString(1, email);
            addUser.setString(2, ucoId);
            addUser.setString(3, password);
            addUser.setString(4, firstName);
            addUser.setString(5, lastName);
            addUser.setInt(6, randomNum);

            addUser.executeUpdate();

            ResultSet results = addUser.getGeneratedKeys();

            //for whatever really fun reason it says userid is not a field
            int id = 0;
            while (results.next()) {
                id = results.getInt(1);
            }

            PreparedStatement addUserToGroup = connection.prepareStatement(
                    "insert into GroupTable (userID, email) values (?, ?)");

            addUserToGroup.setInt(1, id);
            addUserToGroup.setString(2, email);

            addUserToGroup.executeUpdate();

            PreparedStatement addMajor = connection.prepareStatement(
                    "insert into MajorCodes (userID, majorCode) values (?, ?)");

            addMajor.setInt(1, id);
            addMajor.setString(2, major);

            addMajor.executeUpdate();
            sendEmails send = new sendEmails();
            send.sendConfirmation(email, firstName, lastName, randomNum, id);
            System.out.print("test");

        } finally {
            connection.close();
        }

        return "thanks";
    }

    public void authUser() throws SQLException {
        System.out.print("hit authUser");
        if (dataSource == null) {
            throw new SQLException("DataSource is null");
        }

        Connection connection = dataSource.getConnection();

        if (connection == null) {
            throw new SQLException("Connection");
        }

        try {

            PreparedStatement getUser = connection.prepareStatement(
                    "select * from UserTable where authKey = ? and userid = ?"
            );
            getUser.setInt(1, token);
            getUser.setInt(2, userID);
            ResultSet results = getUser.executeQuery();

            if (results.next()) {
                PreparedStatement setUser = connection.prepareStatement(
                        "update grouptable set groupname = 'student' where userid = ?"
                );
                
                setUser.setInt(1, userID);
                
                setUser.executeUpdate();
                
                FacesContext.getCurrentInstance().addMessage("actForm:sub", new FacesMessage("Account Activated Succesfully"));

            } else {
                FacesContext.getCurrentInstance().addMessage("actForm:user", new FacesMessage("Unable to find account information"));
                System.out.print("no user");
            }
        } finally {
            connection.close();
        }

        
    }

    public AppointmentEvent getAppointment() {
        String user = FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement query = conn.prepareStatement(
                    "select userid from usertable where email = ?",
                    Statement.RETURN_GENERATED_KEYS
            );

            query.setString(1, user);
            ResultSet rs = query.executeQuery();
            int uid = 0;
            if (rs.next()) {
                uid = rs.getInt(1);
            }

            query = conn.prepareStatement(
                    "select * from appointment natural join appointment_slots "
                    + "where userid = ?", Statement.RETURN_GENERATED_KEYS);
            query.setInt(1, uid);
            rs = query.executeQuery();

            AppointmentEvent ae;
            Timestamp sd = null;
            Timestamp ed = null;
            if (rs.next()) {
                sd = rs.getTimestamp("startdate");
                ed = rs.getTimestamp("enddate");
            }

            ae = new AppointmentEvent("Student Event", sd, ed, 0);

            return ae;
        } catch (SQLException ex) {
            Logger.getLogger(UserBean.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public void changePassword() throws SQLException {
        if (dataSource == null) {
            throw new SQLException("DataSource is null");
        }

        Connection connection = dataSource.getConnection();

        if (connection == null) {
            throw new SQLException("Connection");
        }

        try {

            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            String username = ec.getRemoteUser();

            PreparedStatement changePassword = connection.prepareStatement("update UserTable set password=? where email=?");
            changePassword.setString(1, getPassword());
            changePassword.setString(2, username);

            changePassword.executeUpdate();
        } finally {
            connection.close();
        }
    }

    public void changeMajor() throws SQLException {
        if (dataSource == null) {
            throw new SQLException("DataSource is null");
        }

        Connection connection = dataSource.getConnection();

        if (connection == null) {
            throw new SQLException("Connection");
        }

        try {
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            String username = ec.getRemoteUser();

            PreparedStatement getId = connection.prepareStatement("select userId from UserTable where email=?");
            getId.setString(1, username);

            ResultSet rs = getId.executeQuery();
            int userId = 0;
            while (rs.next()) {
                userId = rs.getInt("userId");
            }

            PreparedStatement changeMajor = connection.prepareStatement("update MajorCodes set majorcode=? where userId=?");
            changeMajor.setString(1, getMajor());
            changeMajor.setInt(2, userId);
            changeMajor.executeUpdate();
        } finally {
            connection.close();
        }
    }

}
