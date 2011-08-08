package org.breizhjug.breizhlib.database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import fr.ybo.database.DataBaseHelper;
import org.acra.ErrorReporter;
import org.breizhjug.breizhlib.model.Livre;

import java.util.HashMap;
import java.util.Map;

public class Database extends DataBaseHelper {

    private static final String DATABASE_NAME = "breizhlib.db";
    private static final int DATABASE_VERSION = 1;

    private Context context;

    private Map<Integer, UpgradeDatabase> mapUpgrades;

    public Database(Context context) {
        super(context, Constantes.LIST_CLASSES_DATABASE, DATABASE_NAME, DATABASE_VERSION);
        this.context = context;
    }


    private static abstract class UpgradeDatabaseWithError implements UpgradeDatabase {

        final public void upgrade(SQLiteDatabase arg0) {
            try {
                myUpgrade(arg0);
            } catch (Exception exception) {
                ErrorReporter.getInstance().handleException(exception);
            }
        }

        abstract void myUpgrade(SQLiteDatabase db);

    }


    @Override
    protected Map<Integer, UpgradeDatabase> getUpgrades() {
        if (mapUpgrades == null) {
            mapUpgrades = new HashMap<Integer, UpgradeDatabase>(1);
            mapUpgrades.put(0, new UpgradeDatabaseWithError() {
                public void myUpgrade(SQLiteDatabase db) {
                    getBase().getTable(Livre.class).createTable(db);
                }
            });
        }
        return mapUpgrades;
    }
}
