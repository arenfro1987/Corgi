/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.corgi.uco.entity;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author loganhuskins
 */
@Entity
@Table(name = "COURSE_OFFERING")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CourseOffering.findAll", query = "SELECT c FROM CourseOffering c"),
    @NamedQuery(name = "CourseOffering.findByCrn", query = "SELECT c FROM CourseOffering c WHERE c.courseOfferingPK.crn = :crn"),
    @NamedQuery(name = "CourseOffering.findByDepartment", query = "SELECT c FROM CourseOffering c WHERE c.courseOfferingPK.department = :department"),
    @NamedQuery(name = "CourseOffering.findByCourseNumber", query = "SELECT c FROM CourseOffering c WHERE c.courseOfferingPK.courseNumber = :courseNumber")})
public class CourseOffering implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected CourseOfferingPK courseOfferingPK;
    @ManyToMany(mappedBy = "courseOfferingCollection")
    private Collection<UserTable> userTableCollection;
    @JoinColumns({
        @JoinColumn(name = "DEPARTMENT", referencedColumnName = "DEPARTMENT", insertable = false, updatable = false),
        @JoinColumn(name = "COURSE_NUMBER", referencedColumnName = "COURSE_NUMBER", insertable = false, updatable = false)})
    @ManyToOne(optional = false)
    private Course course;

    public CourseOffering() {
    }

    public CourseOffering(CourseOfferingPK courseOfferingPK) {
        this.courseOfferingPK = courseOfferingPK;
    }

    public CourseOffering(String crn, String department, String courseNumber) {
        this.courseOfferingPK = new CourseOfferingPK(crn, department, courseNumber);
    }

    public CourseOfferingPK getCourseOfferingPK() {
        return courseOfferingPK;
    }

    public void setCourseOfferingPK(CourseOfferingPK courseOfferingPK) {
        this.courseOfferingPK = courseOfferingPK;
    }

    @XmlTransient
    public Collection<UserTable> getUserTableCollection() {
        return userTableCollection;
    }

    public void setUserTableCollection(Collection<UserTable> userTableCollection) {
        this.userTableCollection = userTableCollection;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (courseOfferingPK != null ? courseOfferingPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CourseOffering)) {
            return false;
        }
        CourseOffering other = (CourseOffering) object;
        if ((this.courseOfferingPK == null && other.courseOfferingPK != null) || (this.courseOfferingPK != null && !this.courseOfferingPK.equals(other.courseOfferingPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.corgi.uco.entity.CourseOffering[ courseOfferingPK=" + courseOfferingPK + " ]";
    }
    
}
