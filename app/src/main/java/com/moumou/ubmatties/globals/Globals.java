package com.moumou.ubmatties.globals;

/**
 * Created by dennis on 04-10-16.
 */

public class Globals {

    public static final int NUMBER_OF_TABS = 3;
    public static final String INVITE_URL = "";
    public static final String INVITE_IMAGE_URL = "";
    public static final String DB_SESSIONS_URL = "http://ubmatties.dennismouwen.nl/getSessions.php";
    public static final String DB_SESSIONS_FROM_USER_URL = "http://ubmatties.dennismouwen.nl/getSessionsFromHost.php?id=";
    public static final String DB_USER = "http://ubmatties.dennismouwen.nl/getUser.php?id=";
    public static final String DB_INSERT_USER = "http://ubmatties.dennismouwen.nl/insertUser.php?id=";
    public static final String DB_INSERT_SESSION = "http://ubmatties.dennismouwen.nl/insertSession.php?hostid=";
    public static final String DB_UPDATE_SESSION = "http://ubmatties.dennismouwen.nl/updateSession.php?id=";
    public static final String TAG_ID = "id";
    public static final String TAG_NAME = "name";
    public static final String TAG_HOSTID = "hostid";
    public static final String TAG_TYPE = "type";
    public static final String TAG_DATE = "date";
    public static final String TAG_START = "start";
    public static final String TAG_END = "end";
    public static final String TAG_RESULT = "result";
    public static final String TAG_SESSIONS = "sessions";
    public static final String TAG_SESSION = "session";
    public static final String TAG_USER = "user";
    public static final int LOADER_GET_OWN_SESSIONS = 1;
    public static final int LOADER_SESSIONS = 1;
    public static final int LOADER_ADD_SESSION = 2;

    private Globals() {

    }
}
