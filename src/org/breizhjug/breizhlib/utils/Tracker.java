package org.breizhjug.breizhlib.utils;


import android.app.Application;
import android.os.Handler;
import com.google.android.apps.analytics.GoogleAnalyticsTracker;
import com.google.inject.Inject;
import org.breizhjug.breizhlib.guice.UaAccount;
import org.breizhjug.breizhlib.utils.version.Version;

public class Tracker {

    private Handler handler = new Handler();

    private String uaAccount;

    private final GoogleAnalyticsTracker traker;

    private Application application;

    @Inject
    public Tracker(@UaAccount String uaAccount, Application application) {
        this(GoogleAnalyticsTracker.getInstance(), uaAccount, application);
    }

    public Tracker(GoogleAnalyticsTracker traker, String uaAccount, Application application) {
        this.traker = traker;
        this.uaAccount = uaAccount;
        this.application = application;
        initData();
    }

    private void initData() {
        traker.start(uaAccount, application);
        traker.setCustomVar(1, "app_version", "" + Version.getVersionCourante(application), 1);
    }


    public void trackPageView(final String url) {
        handler.post(new Runnable() {
            public void run() {
                traker.trackPageView(url);
                traker.dispatch();
            }
        });
    }
}
