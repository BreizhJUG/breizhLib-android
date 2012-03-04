package org.breizhjug.breizhlib.utils.authentification;

import android.accounts.Account;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import java.util.List;

/**
 * User: Guernion Sylvain
 * Date: 16/11/11
 * Time: 22:20
 */
public interface Authentification {

    String getToken(Context context, Account account);

    String getAuthCookie(String authToken, Context context);

    List<String> getAccounts(Context context);

    Intent getAuthentificationIntent(Context context);
    
    String getAccountType();
}
