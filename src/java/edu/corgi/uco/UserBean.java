/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.corgi.uco;


import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
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
    
    @NotNull(message="Email cannot be empty.")
    @Pattern(regexp=".+[@].+[.].+", message="A valid email of the format "
            + "'person@domain.com' is required." )
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
    
    
    public UserBean() {
    }
    
}
