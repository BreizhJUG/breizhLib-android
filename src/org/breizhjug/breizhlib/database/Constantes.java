package org.breizhjug.breizhlib.database;

import org.breizhjug.breizhlib.model.Commentaire;
import org.breizhjug.breizhlib.model.Livre;
import org.breizhjug.breizhlib.model.Reservation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class Constantes {

    public static final List<Class<?>> LIST_CLASSES_DATABASE = new ArrayList<Class<?>>(1);

    static {
        LIST_CLASSES_DATABASE.add(Livre.class);
        LIST_CLASSES_DATABASE.add(Reservation.class);
        LIST_CLASSES_DATABASE.add(Commentaire.class);
    }

    public static final Collection<Class<?>> CLASSES_DB_TO_DELETE_ON_UPDATE = new ArrayList<Class<?>>(1);

    static {
        CLASSES_DB_TO_DELETE_ON_UPDATE.add(Livre.class);
        CLASSES_DB_TO_DELETE_ON_UPDATE.add(Reservation.class);
        CLASSES_DB_TO_DELETE_ON_UPDATE.add(Commentaire.class);
    }

    private Constantes() {
    }
}
