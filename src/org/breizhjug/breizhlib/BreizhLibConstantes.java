package org.breizhjug.breizhlib;


import java.util.Locale;

public interface BreizhLibConstantes {

    public static final String SERVER_URL = "http://0-1-6.breizh-lib.appspot.com/";

    public static final String MARKET_URL = "market://details?id=org.breizhjug.breizhlib";

    public static final String  UA_ACCOUNT ="UA-25249240-2";
    /**
     * clés pour shared preferences.
     */
    public static final String SHARED_PREFS = "breizhLib".toUpperCase(Locale.FRANCE) + "_PREFS";
    public static final String ACCOUNT_NAME = "accountName";
    public static final String AUTH_COOKIE = "authCookie";
    public static final String USER = "user";
    public static final String USER_ADMIN = "userAdmin";
    public static final String USER_NOM = "user_nom";
    public static final String USER_PRENOM = "user_prenom";
    public static final String GRID = "ouvrages_grid";
}
