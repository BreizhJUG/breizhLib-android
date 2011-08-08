package org.breizhjug.breizhlib.model;


import org.breizhjug.breizhlib.database.Database;

public interface Model {

    void onLoad(Database db);
}
