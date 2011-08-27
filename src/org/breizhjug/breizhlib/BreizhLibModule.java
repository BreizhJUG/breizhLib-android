package org.breizhjug.breizhlib;

import com.google.inject.AbstractModule;
import org.breizhjug.breizhlib.remote.CommentaireService;
import org.breizhjug.breizhlib.remote.OuvrageService;


public class BreizhLibModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(CommentaireService.class);
        bind(OuvrageService.class);

    }
}
