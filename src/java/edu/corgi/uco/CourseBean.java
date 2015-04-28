package edu.corgi.uco;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.sql.DataSource;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.primefaces.model.DualListModel;

/**
 *
 * @author loganhuskins
 */
@Named(value = "courseBean")
@ViewScoped
public class CourseBean implements Serializable {

    private int year;
    private String semester;
    
    @Size(min = 1, message = "Please enter a title.")
    private String title;

    @NotNull(message = "Please select a department.")
    private String department;

    @NotNull(message = "Please enter a course number.")
    @Size(min = 4, max = 4, message = "Course number must be 4 numbers long.")
    private String courseNumber;

    @Size(min = 1, max = 1, message = "Enter a grade (a-f).")
    private String grade;
    private String courseToAddPreReqTo = null;
    private boolean displayPreReq = false;
    private String prereqCourseNumber;
    private int hours;
    private List<Course> availCourse = new ArrayList<Course>();
    private List<Course> addedCourses;
    private List<String> allCourses = new ArrayList<String>();
    private List<Course> currentPreReq = new ArrayList<Course>();
    private List<Course> semesterCourses = new ArrayList<>();
    private List<Course> semesterAddedCourses = new ArrayList<>();
    private List<Course> prereqAddedCourses = new ArrayList<>();
    private List<Course> fillAllCourses = new ArrayList<>();
    private List<Course> coursesToAddToPreReq = new ArrayList<>();
    private List<Course> addedPreReqCourses = new ArrayList<>();
    private int mainID =0;


    ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
    String username = ec.getRemoteUser();
    ArrayList<Course> courses = new ArrayList<>();
    @Resource(name = "jdbc/corgiDatabase")
    private DataSource ds;
    
    public String addPreReqCourses() throws SQLException
    {
        if (ds == null) {
            throw new SQLException("DataSource is null");
        }

        Connection connection = ds.getConnection();

        if (connection == null) {
            throw new SQLException("Connection");
        }

        try {
            for(int x = 0; x<addedPreReqCourses.size(); x++ )
            {
                System.out.print(addedPreReqCourses.get(x).getTitle());
                PreparedStatement add = connection.prepareStatement("insert into isprereq "
                        + "(maincourseid, prereqcourseid) values (?, ?)");
                add.setInt(1, mainID);
                add.setInt(2, addedPreReqCourses.get(x).getId());
                add.executeUpdate();
                
            }
            
        
        } finally {
            connection.close();
        }
        
        return "pastCourses?faces-redirect=true";
    }
    
    public void fillCurrentPreReq() throws SQLException
    {
        displayPreReq =  true;
        currentPreReq.clear();
        coursesToAddToPreReq.clear();
        if (ds == null) {
            throw new SQLException("DataSource is null");
        }

        Connection connection = ds.getConnection();

        if (connection == null) {
            throw new SQLException("Connection");
        }

        try {
            
            PreparedStatement state = connection.prepareStatement("select courseid from  course where title = ?");
            state.setString(1, courseToAddPreReqTo);
            ResultSet results = state.executeQuery();
            while (results.next()) {
                
                mainID = results.getInt("courseid");
            }
            
            PreparedStatement courseList = connection.prepareStatement("select courseid, hours, dept, coursenumber, "
                    + "title from isprereq join course on isprereq.PREREQCOURSEID = "
                    + "course.COURSEID where maincourseid = ?");
            courseList.setInt(1, mainID);
            ResultSet results2 = courseList.executeQuery();
            
            while(results2.next())
            {
                Course temp = new Course();
                temp.setTitle(results2.getString("title"));
                temp.setCourseNumber(results2.getInt("coursenumber"));
                temp.setDepartment(results2.getString("dept"));
                temp.setHours(results2.getInt("hours"));
                temp.setId(results2.getInt("courseid"));
                currentPreReq.add(temp);
            }
            
            PreparedStatement allCourseList = connection.prepareStatement("select courseid, hours, dept, coursenumber, "
                    + "title from course "
                    + "except "
                    + "select courseid, hours, dept, coursenumber, "
                    + "title from isprereq join course on isprereq.PREREQCOURSEID = "
                    + "course.COURSEID where maincourseid = ?");
            allCourseList.setInt(1, mainID);
            ResultSet results3 = allCourseList.executeQuery();
            
            while(results3.next())
            {
                Course temp = new Course();
                temp.setTitle(results3.getString("title"));
                temp.setCourseNumber(results3.getInt("coursenumber"));
                temp.setDepartment(results3.getString("dept"));
                temp.setHours(results3.getInt("hours"));
                temp.setId(results3.getInt("courseid"));
                coursesToAddToPreReq.add(temp);
            }
        
        } finally {
            connection.close();
        }
        
    }
    
    public List<Course> getSemesterCourses() {
        semesterCourses.clear();
        try (Connection conn = ds.getConnection()) {
            PreparedStatement statement = conn.prepareStatement("select c.title, "
                    + "c.courseNumber, c.dept "
                    + "from CourseOffering co Join Course c on c.courseNumber=co.courseNumber "
                    + "where co.semester=? and co.yearOffered=?");
            statement.setString(1, getSemester());
            statement.setInt(2, getYear());

            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                Course course = new Course();
                course.setCourseNumber(rs.getInt("courseNumber"));
                course.setDepartment(rs.getString("dept"));
                course.setTitle(rs.getString("title"));
                semesterCourses.add(course);
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return semesterCourses;

    }
    
    public List<Course> getSemesterPlannedCourses() {
        semesterCourses.clear();
        try (Connection conn = ds.getConnection()) {
            boolean schedExists = false;
            PreparedStatement statement = conn.prepareStatement("select * from "
                    + "schedule s join usertable u on s.userid=u.userid"
                    + " where s.semester=? and s.yearPlanned=? and u.email=?");
            statement.setString(1, getSemester());
            statement.setInt(2, getYear());
            statement.setString(3, username);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                schedExists = true;
                if (schedExists) {
                    PreparedStatement getSched = conn.prepareStatement("select "
                            + "* from "
                    + "course c join CourseScheduleLinkage cs on cs.courseid=c.courseid"
                    + " where cs.scheduleid=?");
                    getSched.setInt(1, rs.getInt("scheduleID"));
                    
                    ResultSet result = getSched.executeQuery();
                    
                    while (result.next()) {
                        Course course = new Course();
                        course.setCourseNumber(result.getInt("coursenumber"));
                        course.setDepartment(result.getString("dept"));
                        course.setTitle(result.getString("title"));
                        semesterCourses.add(course);
                    }
                }
            }
            if (!schedExists && year != 0) {
                    PreparedStatement userid = conn.prepareStatement("select * from usertable where"
                            + " email=?");
                    userid.setString(1, username);
                    ResultSet useridset = userid.executeQuery();
                    int id = 0;
                    while (useridset.next()) {
                        id = useridset.getInt("userid");
                    }
                    PreparedStatement createSched = conn.prepareStatement("insert into schedule "
                            + "(userid, approved, holdremoved, "
                            + "semester, yearplanned)  values (?, false, false, ?, ?)", Statement.RETURN_GENERATED_KEYS);
                    createSched.setInt(1, id);
                    createSched.setString(2, semester);
                    createSched.setInt(3, year);
                    createSched.executeUpdate();
                    ResultSet schedExe = createSched.getGeneratedKeys();
                    
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return semesterCourses;

    }
    
    public void createAllCourses() throws SQLException {
        allCourses.clear();
        if (ds == null) {
            throw new SQLException("DataSource is null");
        }

        Connection connection = ds.getConnection();

        if (connection == null) {
            throw new SQLException("Connection");
        }

        try {
            PreparedStatement state = connection.prepareStatement("select * from  course");
            ResultSet results = state.executeQuery();
            while (results.next()) {
                
                allCourses.add(results.getString("title"));
            }

        } finally {
            connection.close();
        }

    }
    public void preReqList(ActionEvent actionEvent)
    {
        displayPreReq = true;
        System.out.print("in get pre req course name:" + courseToAddPreReqTo);
             
        
    }
    public ArrayList<Course> getPastCourses() {
        courses.clear();
        try (Connection conn = ds.getConnection()) {

            PreparedStatement statement = conn.prepareStatement("select hours, dept, coursenumber, title, tc.grade from course c join takencourses tc on tc.courseid=c.courseid join usertable u on u.userid=tc.userid where u.email=?");
            statement.setString(1, username);

            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                Course course = new Course();
                course.setCourseNumber(rs.getInt("courseNumber"));
                course.setHours(rs.getInt("hours"));
                course.setDepartment(rs.getString("dept"));
                course.setTitle(rs.getString("title"));
                course.setGrade(rs.getString("grade"));
                courses.add(course);
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return courses;
    }
    
    public String addSemesterPlannedCourse() {
        
        try (Connection conn = ds.getConnection()) {
            int userId = 0;
            PreparedStatement state = conn.prepareStatement("select userid from usertable "
                    + "where email=?");
            state.setString(1, username);

            ResultSet results = state.executeQuery();

            while (results.next()) {
                userId = results.getInt("userid");
            }
            
            PreparedStatement schedid = conn.prepareStatement("select * from schedule where"
                    + " userid=? and yearplanned=? and semester=?");
            schedid.setInt(1, userId);
            schedid.setInt(2, year);
            schedid.setString(3, semester);
            int schedId = 0;
            System.out.println("userid" + userId);
            ResultSet rs = schedid.executeQuery();
            while (rs.next()) {
                schedId = rs.getInt("scheduleid");
            }
            
            for (int i = 0; i < getSemesterAddedCourses().size(); i++) {
                PreparedStatement statement = conn.prepareStatement("insert into courseschedulelinkage (scheduleid, courseid)"
                        + " values (?, ?)");
                System.out.println(getSemesterAddedCourses().get(i).getId());
                System.out.println(schedId);
                statement.setInt(2, getSemesterAddedCourses().get(i).getId());
                statement.setInt(1, schedId);
                statement.executeUpdate();
            }
            
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        
        return "semesterPlanner";
    }
    public String addSemesterCourse() {
        
        try (Connection conn = ds.getConnection()) {
            for (int i = 0; i < getSemesterAddedCourses().size(); i++) {
                PreparedStatement statement = conn.prepareStatement("insert into CourseOffering "
                    + "(courseNumber, semester, yearOffered) values (?, ?, ?)");
                statement.setInt(1, getSemesterAddedCourses().get(i).getCourseNumber());
                statement.setString(2, getSemester());
                statement.setInt(3, getYear());
                statement.executeUpdate();
            }
            
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        
        return "semesterCourses";
    }

    public String addCourse() {

        try (Connection conn = ds.getConnection()) {
            System.out.println(hours + " " + department + " " + courseNumber + " " + title);
            PreparedStatement statement = conn.prepareStatement("insert "
                    + "into COURSE (hours, dept, courseNumber, title)"
                    + "values (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, hours);
            statement.setString(2, department);
            statement.setString(3, courseNumber);
            statement.setString(4, title);

            statement.executeUpdate();
            createAllCourses();

            ResultSet results = statement.getGeneratedKeys();
            int courseId;
            if (prereqCourseNumber != null && !prereqCourseNumber.isEmpty()) {
                while (results.next()) {
                    courseId = results.getInt(1);
                    PreparedStatement prereq = conn.prepareStatement("select * from Course "
                            + "where courseNumber=?");
                    System.out.println(prereqCourseNumber);
                    prereq.setString(1, prereqCourseNumber);
                    ResultSet rs = prereq.executeQuery();
                    System.out.println("here");

                    while (rs.next()) {
                        int preID = rs.getInt("courseID");
                        System.out.println("again");
                        PreparedStatement genPrereq = conn.prepareStatement("insert into IspReReq (mainCourseId, preReqCourseId) values (?, ?)");
                        genPrereq.setInt(1, courseId);
                        genPrereq.setInt(2, preID);

                        genPrereq.executeUpdate();

                    }
                }
            }

        } catch (SQLException ex) {
            System.out.println(ex);
        }
        FacesContext.getCurrentInstance().addMessage(title, new FacesMessage("Success"));
        return null;

    }

    public String addPastCourse() {

        try (Connection conn = ds.getConnection()) {
         
            int userId = 0;
            PreparedStatement state = conn.prepareStatement("select userid from usertable "
                    + "where email=?");
            state.setString(1, username);

            ResultSet results = state.executeQuery();

            while (results.next()) {
                userId = results.getInt("userid");
            }
            for (int x = 0; x < addedCourses.size(); x++) {
                PreparedStatement add = conn.prepareStatement("insert into takencourses "
                        + "(courseid, userid, grade) values (?, ?, ?)");
                add.setInt(1, addedCourses.get(x).getId());
                add.setInt(2, userId);
                add.setString(3, addedCourses.get(x).getGrade());

                add.executeUpdate();

            }
            fillCourseList();

        } catch (SQLException ex) {
            System.out.println(ex);
        }

        return "pastCourses?faces-redirect=true";
    }

    public void fillCourseList() throws SQLException {
        availCourse.clear();
        if (ds == null) {
            throw new SQLException("DataSource is null");
        }

        Connection connection = ds.getConnection();

        if (connection == null) {
            throw new SQLException("Connection");
        }

        try {
            System.out.print("start try");
            PreparedStatement state = connection.prepareStatement("select userid from usertable "
                    + "where email=?");
            System.out.print("made statement");
            state.setString(1, username);
            System.out.print("set string");

            ResultSet results = state.executeQuery();
            System.out.print("execute");
            int userId = 0;
            while (results.next()) {
                userId = results.getInt("userid");
                System.out.print("got user id" + userId);
            }

            PreparedStatement getCourses = connection.prepareStatement(
                    "select title, courseid, hours, dept, coursenumber from course "
                    + "except "
                    + " select title, course.courseid, hours, dept, coursenumber"
                    + " from takenCourses join course on takencourses.courseID = course.courseid where userid = ?");

            getCourses.setInt(1, userId);
            ResultSet results2 = getCourses.executeQuery();
            System.out.print("excuted query");

            if (results2 != null) {
                while (results2.next()) {
                    Course temp = new Course();
                    System.out.print("result next");
                    String courseName = results2.getString(1);
                    System.out.print("got course " + courseName);
                    temp.setTitle(courseName);
                    String dept = results2.getString("dept");
                    temp.setDepartment(dept);
                    int id = results2.getInt("courseid");
                    temp.setId(id);
                    int hours = results2.getInt("hours");
                    temp.setHours(hours);
                    int courseNumber = results2.getInt("coursenumber");
                    temp.setCourseNumber(courseNumber);
                    availCourse.add(temp);

                }
            }
            System.out.print("out loop");

        } finally {
            connection.close();
        }

    }
    
    public List<Course> allCourses() {
        allCourses.clear();
        fillAllCourses.clear();
        getPrereqAddedCourses().clear();
        try (Connection conn = ds.getConnection()) {
            PreparedStatement statement = conn.prepareStatement("select * from Course");

            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                Course course = new Course();
                course.setCourseNumber(rs.getInt("courseNumber"));
                course.setDepartment(rs.getString("dept"));
                course.setTitle(rs.getString("title"));
                course.setHours(rs.getInt("hours"));
                course.setId(rs.getInt("courseid"));
                fillAllCourses.add(course);
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return fillAllCourses;
    }
    
    public List<Course> allElegibleCourses() {
        fillAllCourses.clear();
        getPrereqAddedCourses().clear();
        try (Connection conn = ds.getConnection()) {
            PreparedStatement statement = conn.prepareStatement("select * from "
                    + "Course c join isprereq pr on pr.maincourseid=c.courseid " +
                        "join takencourses tc on tc.courseid=pr.PREREQCOURSEID " +
                           "join usertable ut on ut.USERID=tc.USERID "
                    + "join courseoffering co on co.coursenumber=c.coursenumber" +
                            " where email=? and co.semester=? and co.yearoffered=?");
            statement.setString(1, username);
            statement.setString(2, semester);
            statement.setInt(3, year);

            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                Course course = new Course();
                course.setCourseNumber(rs.getInt("courseNumber"));
                course.setDepartment(rs.getString("dept"));
                course.setTitle(rs.getString("title"));
                course.setHours(rs.getInt("hours"));
                course.setId(rs.getInt("courseid"));
                fillAllCourses.add(course);
            }
            
            PreparedStatement state = conn.prepareStatement("select * from course c "
                    + " join courseoffering co on co.coursenumber=c.coursenumber where"
                    + " co.semester=? and co.yearoffered=? and "
                    + "not exists (select * from isprereq p where c.courseid=p.maincourseid)");
            state.setString(1, semester);
            state.setInt(2, year);
            ResultSet set = state.executeQuery();

            while (set.next()) {
                Course course = new Course();
                course.setCourseNumber(set.getInt("courseNumber"));
                course.setDepartment(set.getString("dept"));
                course.setTitle(set.getString("title"));
                course.setHours(set.getInt("hours"));
                course.setId(set.getInt("courseid"));
                fillAllCourses.add(course);
            }
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return fillAllCourses;
    }

    public List<Course> getAddedPreReqCourses() {
        return addedPreReqCourses;
    }

    public void setAddedPreReqCourses(List<Course> addedPreReqCourses) {
        this.addedPreReqCourses = addedPreReqCourses;
    }
    
    public List<Course> getFillAllCourses() {
        return fillAllCourses;
    }

    public void setFillAllCourses(List<Course> fillAllCourses) {
        this.fillAllCourses = fillAllCourses;
    }

    public List<Course> getCoursesToAddToPreReq() {
        return coursesToAddToPreReq;
    }

    public void setCoursesToAddToPreReq(List<Course> coursesToAddToPreReq) {
        this.coursesToAddToPreReq = coursesToAddToPreReq;
    }

    
    public List<Course> getAddedCourses() {
        return addedCourses;
    }

    public void setAddedCourses(List<Course> addedCourses) {
        for (int x = 0; x < addedCourses.size(); x++) {
            System.out.print("from set added courses name:" + addedCourses.get(x).getTitle());
        }
        this.addedCourses = addedCourses;
    }

    public List<Course> getAvailCourse() throws SQLException {
        return availCourse;
    }

    public void setAvailCourse(List<Course> availCourse) {
        this.availCourse = availCourse;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ArrayList<Course> getCourses() {
        return courses;
    }

    public void setCourses(ArrayList<Course> courses) {
        this.courses = courses;
    }

    public String getCourseToAddPreReqTo() {
        return courseToAddPreReqTo;
    }

    public void setCourseToAddPreReqTo(String courseToAddPreReqTo) throws SQLException {
        fillCurrentPreReq();
        this.courseToAddPreReqTo = courseToAddPreReqTo;
    }

    public boolean isDisplayPreReq() {
        return displayPreReq;
    }

    public void setDisplayPreReq(boolean displayPreReq) {
        this.displayPreReq = displayPreReq;
    }

    public List<Course> getCurrentPreReq() {
        return currentPreReq;
    }

    public void setCurrentPreReq(List<Course> currentPreReq) {
        this.currentPreReq = currentPreReq;
    }
    
    public List<String> getAllCourses() {
        return allCourses;
    }

    public void setAllCourses(List<String> allCourses) {
        this.allCourses = allCourses;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public String getPrereqCourseNumber() {
        return prereqCourseNumber;
    }

    public void setPrereqCourseNumber(String prereqCourseNumber) {
        this.prereqCourseNumber = prereqCourseNumber;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public void setSemesterCourses(List<Course> semesterCourses) {
        this.semesterCourses = semesterCourses;
    }

    public List<Course> getSemesterAddedCourses() {
        return semesterAddedCourses;
    }

    public void setSemesterAddedCourses(List<Course> semesterAddedCourses) {
        this.semesterAddedCourses = semesterAddedCourses;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public List<Course> getPrereqAddedCourses() {
        return prereqAddedCourses;
    }

    public void setPrereqAddedCourses(List<Course> prereqAddedCourses) {
        this.prereqAddedCourses = prereqAddedCourses;
    }
}