package com.moumou.ubmatties;

import com.moumou.ubmatties.globals.SessionType;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by MouMou on 06-10-16
 */

public class Session implements Serializable {

    private int id;
    private User host;
    private SessionType type;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private ArrayList<User> sessionUsers;

    /**
     * Creates a new session from the list with self as host
     */
    public Session(SessionType type, LocalDate date, LocalTime startTime, LocalTime endTime, ArrayList<User> sessionUsers) {
        this.type = type;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.sessionUsers = sessionUsers;
        this.host = MainActivity.getSelf();
    }

    //    /**
    //     * Creates a new session from a new empty list with self as host
    //     */
    //    public Session(SessionType type, LocalDate date, LocalTime startTime,
    //                   LocalTime endTime) {
    //        this.type = type;
    //        this.date = date;
    //        this.startTime = startTime;
    //        this.endTime = endTime;
    //        sessionUsers = new ArrayList<>();
    //        this.host = MainActivity.getSelf();
    //    }

    /**
     * Creates a new session from a new empty list with self as host
     */
    public Session(int id, SessionType type, LocalDate date, LocalTime startTime, LocalTime endTime) {
        this.id = id;
        this.type = type;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        sessionUsers = new ArrayList<>();
        this.host = MainActivity.getSelf();
    }

    public Session(SessionType type, LocalDate date, LocalTime startTime, LocalTime endTime) {
        this.type = type;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        sessionUsers = new ArrayList<>();
        this.host = MainActivity.getSelf();
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

    public ArrayList<User> getSessionUsers() {
        return sessionUsers;
    }

    public void setSessionUsers(ArrayList<User> sessionUsers) {
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

    public int getId() {
        return id;
    }
}
