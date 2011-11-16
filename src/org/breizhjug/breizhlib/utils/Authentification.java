package org.breizhjug.breizhlib.utils;

import android.accounts.Account;
import android.content.Context;

import java.util.List;

/**
 * User: Guernion Sylvain
 * Date: 16/11/11
 * Time: 22:20
 */
public interface Authentification {

    String getToken(Context context, Account account);

    String getAuthCookie(String authToken, Context context);

    List<String> getGoogleAccounts(Context context);
}
