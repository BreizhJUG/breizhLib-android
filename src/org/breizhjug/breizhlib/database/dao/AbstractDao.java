package org.breizhjug.breizhlib.database.dao;


import android.database.Cursor;
import com.google.inject.Inject;
import org.breizhjug.breizhlib.database.Database;

public class AbstractDao {

    protected Cursor cursor;
    @Inject
    protected Database db;


    public void closeCursor() {
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
        db.close();
    }
}
