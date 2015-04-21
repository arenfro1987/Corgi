
package edu.corgi.uco;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.sql.DataSource;
 
import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.event.ScheduleEntryResizeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleModel;


@Named(value = "calendarView")
@SessionScoped
public class CalendarView implements Serializable {
    
    @Resource(name = "jdbc/corgiDatabase")
    private DataSource ds;

    private ScheduleModel eventModel;
 
    private AppointmentEvent event = new AppointmentEvent();
    
 
    @PostConstruct
    public void init() {
        eventModel = new DefaultScheduleModel();
        
        try(Connection conn = ds.getConnection()) {
            String s = "select * from appointment";
            
            PreparedStatement appointmentGetter = conn.prepareStatement(s);
            
            ResultSet rs = appointmentGetter.executeQuery();
            
            while(rs.next()){
                Timestamp sd = rs.getTimestamp("startdate");
                Timestamp ed = rs.getTimestamp("enddate");
                int id = rs.getInt("appointmentid");
                int slots = rs.getInt("slots");
                
                AppointmentEvent ae = new AppointmentEvent("Open Appointment", sd, ed, id);
                ae.setSlots(slots);
                
                String s2 = 
                        "select * from appointment_slots where appointmentid=?";
                PreparedStatement getSlots = 
                        conn.prepareStatement(s2, Statement.RETURN_GENERATED_KEYS);
                getSlots.setInt(1, id);
                
                ResultSet rsSlot = getSlots.executeQuery();
                
                while(rsSlot.next()){
                    int uid = rsSlot.getInt("userid");
                    if(!rsSlot.wasNull()){
                        String studsql = "select * from usertable where userid =?";
                        PreparedStatement getStudent = 
                                conn.prepareStatement(studsql, Statement.RETURN_GENERATED_KEYS);
                        getStudent.setInt(1, uid);
                        ResultSet rsStud = getStudent.executeQuery();
                        rsStud.next();
                        
                        Student stud = new Student();
                        stud.setId(rsStud.getString("ucoid"));
                        stud.setFirstName(rsStud.getString("firstname"));
                        stud.setLastName(rsStud.getString("lastname"));
                        stud.setEmail(rsStud.getString("email"));
                        ae.addStudent(stud);
                        if(ae.getOpenSlots() == 0) ae.setTitle("Full Appointment");
                    }
                    
                }
                
                eventModel.addEvent(ae);
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(CalendarView.class.getName()).log(Level.SEVERE, null, ex);
        }
         
    }
     
     
    public Date getInitialDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), Calendar.FEBRUARY, calendar.get(Calendar.DATE), 0, 0, 0);
         
        return calendar.getTime();
    }
     
    public ScheduleModel getEventModel() {
        return eventModel;
    }
     

 
    private Calendar today() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), 0, 0, 0);
 
        return calendar;
    }
     
    public AppointmentEvent getEvent() {
        return event;
    }
 
    public void setEvent(AppointmentEvent event) {
        this.event = event;
    }
     
    public void addEvent(ActionEvent actionEvent) {
        if(event.getId() == null){
            
            try(Connection conn = ds.getConnection()){
                event.setTitle("Open Appointment");
                
                PreparedStatement add = conn.prepareStatement(
                        "insert into appointment(startdate, enddate, slots) values(?, ?, ?) ", 
                        Statement.RETURN_GENERATED_KEYS);
                
                Calendar c = Calendar.getInstance();
                c.setTime(event.getStartDate());
                Timestamp sd = new Timestamp(c.getTime().getTime());
                add.setTimestamp(1, sd);

                c.setTime(event.getEndDate());
                Timestamp ed = new Timestamp(c.getTime().getTime());
                add.setTimestamp(2, ed);
                
                add.setInt(3, event.getSlots());
                
                add.execute();
                
                ResultSet rid = add.getGeneratedKeys();
                int id = 0;
                if(rid.next()){
                    id = rid.getInt(1);
                }
                
                for(int i=1; i <= event.getSlots(); i++){
                    PreparedStatement addslot = conn.prepareStatement(
                            "insert into appointment_slots(appointmentid) values(?)",
                            Statement.RETURN_GENERATED_KEYS
                    );
                    
                    addslot.setInt(1, id);
                    
                    addslot.execute();
                    
                }
            
                eventModel.addEvent(event);
            } catch (SQLException ex) {
            Logger.getLogger(CalendarView.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else {
            try(Connection conn = ds.getConnection()) {
                
                String s = "update appointment set startdate = ?, enddate = ? "
                        + "where appointmentid = ?";
                
                PreparedStatement update = 
                        conn.prepareStatement(s, Statement.RETURN_GENERATED_KEYS);
                
                Calendar c = Calendar.getInstance();
                c.setTime(event.getStartDate());
                Timestamp sd = new Timestamp(c.getTime().getTime());
                update.setTimestamp(1, sd);
                
                c.setTime(event.getStartDate());
                Timestamp ed = new Timestamp(c.getTime().getTime());
                update.setTimestamp(2, ed);
                
                update.setInt(3, event.getAppointmentID());
                
                update.executeUpdate();
                eventModel.updateEvent(event);
                
            } catch (SQLException ex) {
            Logger.getLogger(CalendarView.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
            

            
        event = new AppointmentEvent();
    }
    
    public void deleteEvent(ActionEvent actionEvent) {
        try(Connection conn = ds.getConnection()){
            String s = "select * from appointment_slots where appointmentid= ?";
            PreparedStatement query = conn.prepareStatement(s, Statement.RETURN_GENERATED_KEYS);
            query.setInt(1, event.getAppointmentID());
            ResultSet rs = query.executeQuery();
            while(rs.next()){
                int uid = rs.getInt("userid");
                if(!rs.wasNull()) {
                    s = "select email, firstname, lastname from usertable where userid = ?";
                    query = conn.prepareStatement(s, Statement.RETURN_GENERATED_KEYS);
                    query.setInt(1, uid);
                    ResultSet rs2 = query.executeQuery();
                    if(rs2.next()){
                        String fn = rs2.getString("firstname");
                        String ln = rs2.getString("lastname");
                        String email = rs2.getString("email");

                        sendEmails.sendAdvisorCancel(email, fn, ln);
                    }
                }
            }
            
            
            s = "delete from appointment_slots where appointmentid=?";
            query = conn.prepareStatement(s, Statement.RETURN_GENERATED_KEYS);
            query.setInt(1, event.getAppointmentID());
            query.execute();
            
            s = "delete from appointment where appointmentid=?";

            query = conn.prepareStatement(s, Statement.RETURN_GENERATED_KEYS);
            query.setInt(1, event.getAppointmentID());
            query.execute();
            
            eventModel.deleteEvent(event);
            
        } catch (SQLException ex) {
            Logger.getLogger(CalendarView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void signUp(ActionEvent actionEvent) {
        
        String user = FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();
        
        try(Connection conn = ds.getConnection()){
            String s = "select * from usertable where email = ?";
            PreparedStatement query = conn.prepareStatement(s, Statement.RETURN_GENERATED_KEYS);
            query.setString(1, user);
            ResultSet rs = query.executeQuery();
            
            if(rs.next()){
                int uid = rs.getInt("userid");
                Student student = new Student();
                student.setEmail(user);
                student.setFirstName(rs.getString("firstname"));
                student.setLastName(rs.getString("lastname"));
                student.setId(rs.getString("ucoid"));
                
                s = "update appointment_slots set userid = null where userid = ?";
                query = conn.prepareStatement(s, Statement.RETURN_GENERATED_KEYS);
                query.setInt(1, uid);
                
                s = "select * from appointment_slots where appointmentid=?";
                query = conn.prepareStatement(s, Statement.RETURN_GENERATED_KEYS);
                query.setInt(1, event.getAppointmentID());
                rs= query.executeQuery();

                boolean found = false;

                
                while(!found && rs.next()){
                    int i = rs.getInt("userid");
                    if(rs.wasNull()){
                        i = rs.getInt("slotid");
                        s = "update appointment_slots set userid = ? where appointmentid = ?";
                        query = conn.prepareStatement(s, Statement.RETURN_GENERATED_KEYS);
                        query.setInt(1, uid);
                        query.setInt(2, event.getAppointmentID());
                        query.execute();
                        found = true;
                    }

                    event.addStudent(student);
                }
                
                sendEmails.sendStudentSignUp(student.getFirstName(), student.getLastName(), event.getStartDate());
            }
        } catch (SQLException ex) {
            Logger.getLogger(CalendarView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
     
    public void onEventSelect(SelectEvent selectEvent) {
        event = (AppointmentEvent) selectEvent.getObject();
    }
     
    public void onDateSelect(SelectEvent selectEvent) {
        event = new AppointmentEvent("", (Date) selectEvent.getObject(), (Date) selectEvent.getObject(), 0);
    }
     
    public void onEventMove(ScheduleEntryMoveEvent event) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Event moved", "Day delta:" + event.getDayDelta() + ", Minute delta:" + event.getMinuteDelta());
         
        addMessage(message);
    }
     
    public void onEventResize(ScheduleEntryResizeEvent event) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Event resized", "Day delta:" + event.getDayDelta() + ", Minute delta:" + event.getMinuteDelta());
         
        addMessage(message);
    }
     
    private void addMessage(FacesMessage message) {
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
    
}
