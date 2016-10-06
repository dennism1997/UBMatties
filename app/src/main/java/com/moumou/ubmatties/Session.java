package com.moumou.ubmatties;

import com.moumou.ubmatties.globals.SessionType;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MouMou on 06-10-16
 */

public class Session {

    private SessionType type;
    private LocalDate date;
    private LocalTime dateTimeStart;
    private LocalTime dateTimeEnd;
    private List<User> sessionUsers;

    public Session(SessionType type, LocalDate date, LocalTime dateTimeStart,
                   LocalTime dateTimeEnd, List<User> sessionUsers) {
        this.type = type;
        this.date = date;
        this.dateTimeStart = dateTimeStart;
        this.dateTimeEnd = dateTimeEnd;
        this.sessionUsers = sessionUsers;
    }

    public Session(SessionType type, LocalDate date, LocalTime dateTimeStart,
                   LocalTime dateTimeEnd) {
        this.type = type;
        this.date = date;
        this.dateTimeStart = dateTimeStart;
        this.dateTimeEnd = dateTimeEnd;
        sessionUsers = new ArrayList<User>();
    }

    public SessionType getType() {
        return type;
    }

    public void setType(SessionType type) {
        this.type = type;
    }

    public LocalTime getDateTimeStart() {
        return dateTimeStart;
    }

    public void setDateTimeStart(LocalTime dateTimeStart) {
        this.dateTimeStart = dateTimeStart;
    }

    public LocalTime getDateTimeEnd() {
        return dateTimeEnd;
    }

    public void setDateTimeEnd(LocalTime dateTimeEnd) {
        this.dateTimeEnd = dateTimeEnd;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<User> getSessionUsers() {
        return sessionUsers;
    }

    public void setSessionUsers(List<User> sessionUsers) {
        this.sessionUsers = sessionUsers;
    }
}
