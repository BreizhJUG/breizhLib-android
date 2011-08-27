package org.breizhjug.breizhlib;

import android.util.Log;
import com.google.inject.Inject;
import com.google.inject.Module;
import org.acra.ACRA;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;
import org.breizhjug.breizhlib.database.Database;
import org.breizhjug.breizhlib.utils.version.VersionTask;
import roboguice.application.RoboInjectableApplication;

import java.util.List;


@ReportsCrashes(formKey = "dGFpb0t5YXF3a3J3Ui1GZjBVY1ROMGc6MQ",
        mode = ReportingInteractionMode.TOAST, resToastText = R.string.resToastText)
public class BreizhLib extends RoboInjectableApplication {

    @Inject
    private Database databaseHelper;

    @Inject
    private VersionTask checkVersion;

    protected void addApplicationModules(List<Module> modules) {
        modules.add(new BreizhLibModule(this));
    }


    @Override
    public void onCreate() {
        ACRA.init(this);
        super.onCreate();

        databaseHelper.onUpgrade(databaseHelper.getReadableDatabase(), 0, databaseHelper.getReadableDatabase().getVersion());
        Log.d("BreizhLib", "db version " + databaseHelper.getReadableDatabase().getVersion());

        checkVersion.execute();
    }
}
