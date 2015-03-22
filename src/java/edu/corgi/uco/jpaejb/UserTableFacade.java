/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.corgi.uco.jpaejb;

import edu.corgi.uco.entity.UserTable;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author loganhuskins
 */
@Stateless
public class UserTableFacade extends AbstractFacade<UserTable> {
    @PersistenceContext(unitName = "CorgiPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UserTableFacade() {
        super(UserTable.class);
    }
    
}
