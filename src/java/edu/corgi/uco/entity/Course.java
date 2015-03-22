/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.corgi.uco.entity;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.CascadeType;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author loganhuskins
 */
@Entity
@Table(name = "COURSE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Course.findAll", query = "SELECT c FROM Course c"),
    @NamedQuery(name = "Course.findByDepartment", query = "SELECT c FROM Course c WHERE c.coursePK.department = :department"),
    @NamedQuery(name = "Course.findByCourseNumber", query = "SELECT c FROM Course c WHERE c.coursePK.courseNumber = :courseNumber")})
public class Course implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected CoursePK coursePK;
    @JoinTable(name = "COURSE_PREREQUISITE", joinColumns = {
        @JoinColumn(name = "PRE_DEPARTMENT", referencedColumnName = "DEPARTMENT"),
        @JoinColumn(name = "PRE_COURSE_NUMBER", referencedColumnName = "COURSE_NUMBER")}, inverseJoinColumns = {
        @JoinColumn(name = "POST_DEPARTMENT", referencedColumnName = "DEPARTMENT"),
        @JoinColumn(name = "POST_COURSE_NUMBER", referencedColumnName = "COURSE_NUMBER")})
    @ManyToMany
    private Collection<Course> courseCollection;
    @ManyToMany(mappedBy = "courseCollection")
    private Collection<Course> courseCollection1;
    @ManyToMany(mappedBy = "courseCollection")
    private Collection<UserTable> userTableCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "course")
    private Collection<CourseOffering> courseOfferingCollection;

    public Course() {
    }

    public Course(CoursePK coursePK) {
        this.coursePK = coursePK;
    }

    public Course(String department, String courseNumber) {
        this.coursePK = new CoursePK(department, courseNumber);
    }

    public CoursePK getCoursePK() {
        return coursePK;
    }

    public void setCoursePK(CoursePK coursePK) {
        this.coursePK = coursePK;
    }

    @XmlTransient
    public Collection<Course> getCourseCollection() {
        return courseCollection;
    }

    public void setCourseCollection(Collection<Course> courseCollection) {
        this.courseCollection = courseCollection;
    }

    @XmlTransient
    public Collection<Course> getCourseCollection1() {
        return courseCollection1;
    }

    public void setCourseCollection1(Collection<Course> courseCollection1) {
        this.courseCollection1 = courseCollection1;
    }

    @XmlTransient
    public Collection<UserTable> getUserTableCollection() {
        return userTableCollection;
    }

    public void setUserTableCollection(Collection<UserTable> userTableCollection) {
        this.userTableCollection = userTableCollection;
    }

    @XmlTransient
    public Collection<CourseOffering> getCourseOfferingCollection() {
        return courseOfferingCollection;
    }

    public void setCourseOfferingCollection(Collection<CourseOffering> courseOfferingCollection) {
        this.courseOfferingCollection = courseOfferingCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (coursePK != null ? coursePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Course)) {
            return false;
        }
        Course other = (Course) object;
        if ((this.coursePK == null && other.coursePK != null) || (this.coursePK != null && !this.coursePK.equals(other.coursePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.corgi.uco.entity.Course[ coursePK=" + coursePK + " ]";
    }
    
}
