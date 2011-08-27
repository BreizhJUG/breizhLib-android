package org.breizhjug.breizhlib.utils.version;

import android.app.Application;
import android.os.AsyncTask;
import com.google.inject.Inject;


public class VersionTask extends AsyncTask<Void, Void, Integer> {

    private Application app;

    @Inject
    public VersionTask(Application app) {
        this.app = app;
    }

    @Override
    protected Integer doInBackground(Void... params) {
        return Version.getVersionCodeMarket();
    }

    protected void onPostExecute(Integer result) {
        if (result != null && result > Version.getVersionCourante(app)) {
            Version.createNotification(app, app);
        }
    }
}