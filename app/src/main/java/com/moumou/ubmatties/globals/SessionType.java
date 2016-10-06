package com.moumou.ubmatties.globals;

/**
 * Created by MouMou on 06-10-16
 */

public enum SessionType {
    STUDY, COFFEE, LUNCH;

    public String toPrettyString() {
        return this.toString().substring(0, 1).toUpperCase() + this.toString().substring(1).toLowerCase();
    }
}
