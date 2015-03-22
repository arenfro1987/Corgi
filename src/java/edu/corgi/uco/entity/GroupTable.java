/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.corgi.uco.entity;

import java.io.Serializable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author loganhuskins
 */
@Entity
@Table(name = "GROUP_TABLE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GroupTable.findAll", query = "SELECT g FROM GroupTable g"),
    @NamedQuery(name = "GroupTable.findByGroupname", query = "SELECT g FROM GroupTable g WHERE g.groupTablePK.groupname = :groupname"),
    @NamedQuery(name = "GroupTable.findByUserId", query = "SELECT g FROM GroupTable g WHERE g.groupTablePK.userId = :userId")})
public class GroupTable implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected GroupTablePK groupTablePK;
    @JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private UserTable userTable;

    public GroupTable() {
    }

    public GroupTable(GroupTablePK groupTablePK) {
        this.groupTablePK = groupTablePK;
    }

    public GroupTable(String groupname, int userId) {
        this.groupTablePK = new GroupTablePK(groupname, userId);
    }

    public GroupTablePK getGroupTablePK() {
        return groupTablePK;
    }

    public void setGroupTablePK(GroupTablePK groupTablePK) {
        this.groupTablePK = groupTablePK;
    }

    public UserTable getUserTable() {
        return userTable;
    }

    public void setUserTable(UserTable userTable) {
        this.userTable = userTable;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (groupTablePK != null ? groupTablePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GroupTable)) {
            return false;
        }
        GroupTable other = (GroupTable) object;
        if ((this.groupTablePK == null && other.groupTablePK != null) || (this.groupTablePK != null && !this.groupTablePK.equals(other.groupTablePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.corgi.uco.entity.GroupTable[ groupTablePK=" + groupTablePK + " ]";
    }
    
}
