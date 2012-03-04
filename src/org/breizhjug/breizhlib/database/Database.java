package org.breizhjug.breizhlib.database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.google.inject.Inject;
import fr.ybo.database.DataBaseHelper;
import org.acra.ErrorReporter;
import org.breizhjug.breizhlib.model.Commentaire;
import org.breizhjug.breizhlib.model.Emprunt;
import org.breizhjug.breizhlib.model.Livre;
import org.breizhjug.breizhlib.model.Reservation;

import java.util.HashMap;
import java.util.Map;

public class Database extends DataBaseHelper {

    private static final String TAG = "Breizhlib.Database";

    private static final String DATABASE_NAME = "breizhlib.db";
    private static final int DATABASE_VERSION = 3;

    private Map<Integer, UpgradeDatabase> mapUpgrades;

    DBContext context;

    @Inject
    public Database(Context context) {
        this(new DBContext(context));
    }

    public Database(DBContext context) {
        super(context, Constantes.LIST_CLASSES_DATABASE, DATABASE_NAME, DATABASE_VERSION);
        this.context = context;
        this.context.clean();
    }


    private static abstract class UpgradeDatabaseWithError implements UpgradeDatabase {

        final public void upgrade(SQLiteDatabase arg0) {
            try {
                Log.e(TAG, "update db");
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
            mapUpgrades = new HashMap<Integer, UpgradeDatabase>(4);
            mapUpgrades.put(0, new UpgradeDatabaseWithError() {
                public void myUpgrade(SQLiteDatabase db) {
                    getBase().getTable(Livre.class).createTable(db);
                    getBase().getTable(Commentaire.class).createTable(db);
                    getBase().getTable(Emprunt.class).createTable(db);
                    getBase().getTable(Reservation.class).createTable(db);
                }
            });
            mapUpgrades.put(1, new UpgradeDatabaseWithError() {
                public void myUpgrade(SQLiteDatabase db) {
                    getBase().getTable(Commentaire.class).dropTable(db);
                    getBase().getTable(Commentaire.class).createTable(db);
                }
            });
            mapUpgrades.put(2, new UpgradeDatabaseWithError() {
                public void myUpgrade(SQLiteDatabase db) {
                    getBase().getTable(Emprunt.class).dropTable(db);
                    getBase().getTable(Emprunt.class).createTable(db);
                }
            });
            mapUpgrades.put(3, new UpgradeDatabaseWithError() {
                public void myUpgrade(SQLiteDatabase db) {
                    getBase().getTable(Reservation.class).dropTable(db);
                    getBase().getTable(Reservation.class).createTable(db);
                }
            });
        }
        return mapUpgrades;
    }
}
