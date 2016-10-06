package com.moumou.ubmatties;

import org.joda.time.DateTimeComparator;

import java.util.Comparator;

/**
 * Created by MouMou on 06-10-16.
 */

public class SessionComparator implements Comparator<Session> {

    private static SessionComparator sessionComparator = new SessionComparator();

    private SessionComparator() {
    }

    public static SessionComparator getInstance() {
        return sessionComparator;
    }

    @Override
    public int compare(Session o1, Session o2) {
        DateTimeComparator dc = DateTimeComparator.getInstance();
        DateTimeComparator tc = DateTimeComparator.getInstance();

        //int date = dc.compare(o1.getDate(), o2.getDate());
        int date = -1;
        int time = tc.compare(o1.getDateTimeStart(), o2.getDateTimeStart());
        int res = 1;
        if (date < 0) {
            if (time < 0) {
                res = -1;
            }
        } else if (date == 0 && time == 0) {
            res = 0;
        }

        return res;
    }
}
