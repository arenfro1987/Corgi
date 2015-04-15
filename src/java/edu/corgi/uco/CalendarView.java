
package edu.corgi.uco;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
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
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.LazyScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;


@Named(value = "calendarView")
@SessionScoped
public class CalendarView implements Serializable {
    
    @Resource(name = "jdbc/corgiDatabase")
    private DataSource ds;

    private ScheduleModel eventModel;
 
    private ScheduleEvent event = new AppointmentEvent();
    
 
    @PostConstruct
    public void init() {
        eventModel = new DefaultScheduleModel();
        
        try(Connection conn = ds.getConnection()) {
            String s = "select * from appointment left outer join usertable on appointment.userid = usertable.userid";
            
            PreparedStatement appointmentGetter = conn.prepareStatement(s);
            
            ResultSet rs = appointmentGetter.executeQuery();
            
            while(rs.next()){
                Timestamp sd = rs.getTimestamp("startdate");
                Timestamp ed = rs.getTimestamp("enddate");
                int id = rs.getInt("appointmentid");
                
                AppointmentEvent ae = new AppointmentEvent("Open Appointment", sd, ed, id);
                
                String userid = rs.getString("userid");
                if(!rs.wasNull()) {
                    Student stud = new Student();
                    stud.setId(rs.getString("ucoid"));
                    stud.setFirstName(rs.getString("firstname"));
                    stud.setLastName(rs.getString("lastname"));
                    stud.setEmail(rs.getString("email"));
                    
                    ae.setStudent(stud);
                    ae.setTitle(stud.getFirstName() + " " + stud.getLastName());
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
     
    public ScheduleEvent getEvent() {
        return event;
    }
 
    public void setEvent(ScheduleEvent event) {
        this.event = event;
    }
     
    public void addEvent(ActionEvent actionEvent) {
        if(event.getId() == null){
            
            try(Connection conn = ds.getConnection()){
                
                PreparedStatement add = conn.prepareStatement(
                        "insert into appointment(startdate, enddate) values(?, ?)", 
                        Statement.RETURN_GENERATED_KEYS);
                
                Calendar c = Calendar.getInstance();
                c.setTime(event.getStartDate());
                Timestamp sd = new Timestamp(c.getTime().getTime());
                add.setTimestamp(1, sd);

                c.setTime(event.getEndDate());
                Timestamp ed = new Timestamp(c.getTime().getTime());
                add.setTimestamp(2, ed);
                
                add.execute();
            
            } catch (SQLException ex) {
            Logger.getLogger(CalendarView.class.getName()).log(Level.SEVERE, null, ex);
            }
            eventModel.addEvent(event);
        }
        else
            eventModel.updateEvent(event);
         
        event = new DefaultScheduleEvent();
    }
     
    public void onEventSelect(SelectEvent selectEvent) {
        event = (ScheduleEvent) selectEvent.getObject();
    }
     
    public void onDateSelect(SelectEvent selectEvent) {
        event = new DefaultScheduleEvent("", (Date) selectEvent.getObject(), (Date) selectEvent.getObject());
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
