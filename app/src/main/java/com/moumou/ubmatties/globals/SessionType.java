package com.moumou.ubmatties.globals;

/**
 * Created by MouMou on 06-10-16
 */

public enum SessionType {
    STUDY, COFFEE, LUNCH;

    public String toPrettyString() {
        return this.toString().substring(0, 1).toUpperCase() + this.toString().substring(1).toLowerCase();
    }

    public static SessionType fromInt(int i) {
        switch (i) {
            case 1:
                return STUDY;
            case 2:
                return COFFEE;
            case 3:
                return LUNCH;
            default:
                return null;
        }
    }

    public static int toInt(SessionType type) {
        switch (type) {
            case STUDY:
                return 1;
            case COFFEE:
                return 2;
            case LUNCH:
                return 3;
            default:
                return 0;
        }
    }
}
