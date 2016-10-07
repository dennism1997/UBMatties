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

    private User host;
    private SessionType type;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private List<User> sessionUsers;

    public Session(SessionType type, LocalDate date, LocalTime startTime,
                   LocalTime endTime, List<User> sessionUsers) {
        this.type = type;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.sessionUsers = sessionUsers;
    }

    public Session(SessionType type, LocalDate date, LocalTime startTime,
                   LocalTime endTime) {
        this.type = type;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        sessionUsers = new ArrayList<User>();
    }

    public SessionType getType() {
        return type;
    }

    public void setType(SessionType type) {
        this.type = type;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
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

    public void addSessionUser(User user) {
        this.sessionUsers.add(user);
    }

    public User getHost() {
        return host;
    }

    public void setHost(User host) {
        this.host = host;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Session) {
            Session other = (Session) obj;

            return this.getType().equals(other.getType()) &&
                    this.getStartTime().equals(other.getStartTime()) &&
                    this.getEndTime().equals(other.getEndTime()) &&
                    this.getDate().equals(other.getDate()) &&
                    this.getSessionUsers().equals(other.getSessionUsers());
        } else {
            return false;
        }


    }
}
