/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.corgi.uco.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author loganhuskins
 */
@Embeddable
public class CoursePK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 4)
    @Column(name = "DEPARTMENT")
    private String department;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 4)
    @Column(name = "COURSE_NUMBER")
    private String courseNumber;

    public CoursePK() {
    }

    public CoursePK(String department, String courseNumber) {
        this.department = department;
        this.courseNumber = courseNumber;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getCourseNumber() {
        return courseNumber;
    }

    public void setCourseNumber(String courseNumber) {
        this.courseNumber = courseNumber;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (department != null ? department.hashCode() : 0);
        hash += (courseNumber != null ? courseNumber.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CoursePK)) {
            return false;
        }
        CoursePK other = (CoursePK) object;
        if ((this.department == null && other.department != null) || (this.department != null && !this.department.equals(other.department))) {
            return false;
        }
        if ((this.courseNumber == null && other.courseNumber != null) || (this.courseNumber != null && !this.courseNumber.equals(other.courseNumber))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.corgi.uco.entity.CoursePK[ department=" + department + ", courseNumber=" + courseNumber + " ]";
    }
    
}
