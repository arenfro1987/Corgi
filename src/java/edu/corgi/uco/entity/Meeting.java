/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.corgi.uco.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author loganhuskins
 */
@Entity
@Table(name = "MEETING")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Meeting.findAll", query = "SELECT m FROM Meeting m"),
    @NamedQuery(name = "Meeting.findByMeetingId", query = "SELECT m FROM Meeting m WHERE m.meetingId = :meetingId"),
    @NamedQuery(name = "Meeting.findByMeetingDate", query = "SELECT m FROM Meeting m WHERE m.meetingDate = :meetingDate"),
    @NamedQuery(name = "Meeting.findByMeetingTime", query = "SELECT m FROM Meeting m WHERE m.meetingTime = :meetingTime")})
public class Meeting implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "MEETING_ID")
    private Integer meetingId;
    @Column(name = "MEETING_DATE")
    @Temporal(TemporalType.DATE)
    private Date meetingDate;
    @Column(name = "MEETING_TIME")
    @Temporal(TemporalType.TIME)
    private Date meetingTime;
    @JoinColumn(name = "STUDENT", referencedColumnName = "USER_ID")
    @ManyToOne
    private UserTable student;
    @JoinColumn(name = "PROFESSOR", referencedColumnName = "USER_ID")
    @ManyToOne
    private UserTable professor;

    public Meeting() {
    }

    public Meeting(Integer meetingId) {
        this.meetingId = meetingId;
    }

    public Integer getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(Integer meetingId) {
        this.meetingId = meetingId;
    }

    public Date getMeetingDate() {
        return meetingDate;
    }

    public void setMeetingDate(Date meetingDate) {
        this.meetingDate = meetingDate;
    }

    public Date getMeetingTime() {
        return meetingTime;
    }

    public void setMeetingTime(Date meetingTime) {
        this.meetingTime = meetingTime;
    }

    public UserTable getStudent() {
        return student;
    }

    public void setStudent(UserTable student) {
        this.student = student;
    }

    public UserTable getProfessor() {
        return professor;
    }

    public void setProfessor(UserTable professor) {
        this.professor = professor;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (meetingId != null ? meetingId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Meeting)) {
            return false;
        }
        Meeting other = (Meeting) object;
        if ((this.meetingId == null && other.meetingId != null) || (this.meetingId != null && !this.meetingId.equals(other.meetingId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "edu.corgi.uco.entity.Meeting[ meetingId=" + meetingId + " ]";
    }
    
}
