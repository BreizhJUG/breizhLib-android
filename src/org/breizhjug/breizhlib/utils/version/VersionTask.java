package org.breizhjug.breizhlib.utils.version;

import android.app.Application;
import android.os.AsyncTask;


public class VersionTask extends AsyncTask<Void,Void,Integer> {

        Application app;

        public VersionTask(Application app) {
            this.app = app;
        }

        @Override
        protected Integer doInBackground(Void... params) {
            return Version.getVersionCodeMarket();
        }

        protected void onPostExecute(Integer result) {
            if (result != null && result > Version.getVersionCourante(app)) {
                Version.createNotification(app,app);
            }
        }
    }