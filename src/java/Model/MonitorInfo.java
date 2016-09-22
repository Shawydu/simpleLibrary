/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Model;

import java.sql.Time;

/**
 *
 * @author kezhang
 */
public class MonitorInfo {

    private int LogID;
    private String EventType;
    private String Message;
    private Time EventTime;
    
    public int getLogID() {
        return LogID;
    }

    public void setLogID(int LogID) {
        this.LogID = LogID;
    }

    public String getEventType() {
        return EventType;
    }

    public void setEventType(String EventType) {
        this.EventType = EventType;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }

    public Time getEventTime() {
        return EventTime;
    }

    public void setEventTime(Time EventTime) {
        this.EventTime = EventTime;
    }
    
    
}
