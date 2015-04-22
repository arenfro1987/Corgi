
package edu.corgi.uco;

import java.util.ArrayList;


public class Schedule {
    private ArrayList<Course> courses;
    private int sid;
    private int uid;
    private boolean approved;
    
    public Schedule(int sid, int uid){
        courses = new ArrayList<>();
        this.sid = sid;
        this.uid = uid;
        approved = false;
    }
    
    public Schedule() {
        this(0, 0);
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }
    
    

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }
    
    public ArrayList<Course> getCourses() {
        if(courses == null) courses = new ArrayList<>();
        return courses;
    }

    public void setCourses(ArrayList<Course> courses) {
        this.courses = courses;
    }
    
    public void addCourse(Course course){
        courses.add(course);
    }
}
