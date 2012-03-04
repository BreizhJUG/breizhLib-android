package org.breizhjug.breizhlib.model;


import android.database.Cursor;
import org.breizhjug.breizhlib.database.Database;

public interface Model {

    <T extends Model> T from(Cursor cursor);
    
    void onLoad(Database db);
}
