package org.breizhjug.breizhlib.account;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * User: Guernion Sylvain
 * Date: 26/02/12
 * Time: 22:56
 */
public class AuthenticatationService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return new AccountAuthenticator(this).getIBinder();
    }
}
