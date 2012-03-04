package org.breizhjug.breizhlib.utils.authentification;

import android.accounts.Account;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import java.util.List;

public interface Authentification {

    String getToken(Context context, Account account);

    String getAuthCookie(String authToken, Context context);

    List<String> getAccounts(Context context);

    Intent getAuthentificationIntent(Context context);
    
    String getAccountType();
}
