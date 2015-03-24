/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.corgi.uco;

import edu.corgi.uco.entity.UserTable;
import edu.corgi.uco.jpaejb.UserTableFacade;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Inject;

/**
 *
 * @author vdpotvin
 */
@Named(value = "userController")
@SessionScoped
public class UserController implements Serializable {

   @EJB
   UserTableFacade userTableFacade;
   
   @Inject
   UserBean userBean;
   
   public List<UserTable> getAll(){
       return userTableFacade.findAll();
   }
    
   public int count(){
       return userTableFacade.count();
   }
   
   public void add() {
       UserTable user = new UserTable();
       user.setUcoId(userBean.getUcoId());
       user.setFirstName(userBean.getFirstName());
       user.setLastName(userBean.getLastName());
       user.setEmail(userBean.getEmail());
       user.setPassword(userBean.getPassword());
       
       userTableFacade.create(user);
   }
    /**
     * Creates a new instance of UserController
     */
    public UserController() {
    }
    
}
