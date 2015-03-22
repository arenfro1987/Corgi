/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.corgi.uco.entity;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author loganhuskins
 */
@Entity
@Table(name = "USER_TABLE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UserTable.findAll", query = "SELECT u FROM UserTable u"),
    @NamedQuery(name = "UserTable.findByUserId", query = "SELECT u FROM UserTable u WHERE u.userId = :userId"),
    @NamedQuery(name = "UserTable.findByEmail", query = "SELECT u FROM UserTable u WHERE u.email = :email"),
    @NamedQuery(name = "UserTable.findByUcoId", query = "SELECT u FROM UserTable u WHERE u.ucoId = :ucoId"),
    @NamedQuery(name = "UserTable.findByPassword", query = "SELECT u FROM UserTable u WHERE u.password = :password"),
    @NamedQuery(name = "UserTable.findByFirstName", query = "SELECT u FROM UserTable u WHERE u.firstName = :firstName"),
    @NamedQuery(name = "UserTable.findByLastName", query = "SELECT u FROM UserTable u WHERE u.lastName = :lastName")})
public class UserTable implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "USER_ID")
    private Integer userId;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "EMAIL")
    private String email;
    @Size(max = 10)
    @Column(name = "UCO_ID")
    private String ucoId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "PASSWORD")
    private String password;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "FIRST_NAME")
    private String firstName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "LAST_NAME")
    private String lastName;
    @JoinTable(name = "PROPOSED_SCHEDULE", joinColumns = {
        @JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID")}, inverseJoinColumns = {
        @JoinColumn(name = "CRN", referencedColumnName = "CRN"),
        @JoinColumn(name = "DEPARTMENT", referencedColumnName = "DEPARTMENT"),
        @JoinColumn(name = "COURSE_NUMBER", referencedColumnName = "COURSE_NUMBER")})
    @ManyToMany
    private Collection<CourseOffering> courseOfferingCollection;
    @JoinTable(name = "HAS_TAKEN", joinColumns = {
        @JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID")}, inverseJoinColumns = {
        @JoinColumn(name = "DEPARTMENT", referencedColumnName = "DEPARTMENT"),
        @JoinColumn(name = "COURSE_NUMBER", referencedColumnName = "COURSE_NUMBER")})
    @ManyToMany
    private Collection<Course> courseCollection;
    @OneToMany(mappedBy = "student")
    private Collection<Meeting> meetingCollection;
    @OneToMany(mappedBy = "professor")
    private Collection<Meeting> meetingCollection1;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userTable")
    private Collection<GroupTable> groupTableCollection;

    public UserTable() {
    }

    public UserTable(Integer userId) {
        this.userId = userId;
    }

    public UserTable(Integer userId, String email, String password, String firstName, String lastName) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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
        this.password = password;
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

    @XmlTransient
    public Collection<CourseOffering> getCourseOfferingCollection() {
        return courseOfferingCollection;
    }

    public void setCourseOfferingCollection(Collection<CourseOffering> courseOfferingCollection) {
        this.courseOfferingCollection = courseOfferingCollection;
    }

    @XmlTransient
    public Collection<Course> getCourseCollection() {
        return courseCollection;
    }

    public void setCourseCollection(Collection<Course> courseCollection) {
        this.courseCollection = courseCollection;
    }

    @XmlTransient
    public Collection<Meeting> getMeetingCollection() {
        return meetingCollection;
    }

    public void setMeetingCollection(Collection<Meeting> meetingCollection) {
        this.meetingCollection = meetingCollection;
    }

    @XmlTransient
    public Collection<Meeting> getMeetingCollection1() {
        return meetingCollection1;
    }

    public void setMeetingCollection1(Collection<Meeting> meetingCollection1) {
        this.meetingCollection1 = meetingCollection1;
    }

    @XmlTransient
    public Collection<GroupTable> getGroupTableCollection() {
        return groupTableCollection;
    }

    public void setGroupTableCollection(Collection<GroupTable> groupTableCollection) {
        this.groupTableCollection = groupTableCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userId != null ? userId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UserTable)) {
            return false;
        }
        UserTable other = (UserTable) object;
        if ((this.userId == null && other.userId != null) || (this.userId != null && !this.userId.equals(other.userId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.corgi.uco.entity.UserTable[ userId=" + userId + " ]";
    }
    
}
