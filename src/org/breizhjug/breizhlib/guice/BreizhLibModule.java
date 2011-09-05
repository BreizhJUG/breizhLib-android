package org.breizhjug.breizhlib.guice;

import org.breizhjug.breizhlib.BreizhLib;
import org.breizhjug.breizhlib.BreizhLibConstantes;
import org.breizhjug.breizhlib.adapter.*;
import org.breizhjug.breizhlib.database.Database;
import org.breizhjug.breizhlib.database.dao.CommentaireDAO;
import org.breizhjug.breizhlib.database.dao.LivreDAO;
import org.breizhjug.breizhlib.database.dao.ReservationDAO;
import org.breizhjug.breizhlib.remote.*;
import org.breizhjug.breizhlib.utils.CacheManager;
import org.breizhjug.breizhlib.utils.GoogleAuthentification;
import org.breizhjug.breizhlib.utils.Tracker;
import org.breizhjug.breizhlib.utils.images.ImageCache;
import org.breizhjug.breizhlib.utils.version.VersionTask;
import org.breizhjug.breizhlib.view.CacheDialogPreference;
import roboguice.config.AbstractAndroidModule;


public class BreizhLibModule extends AbstractAndroidModule {

   private  BreizhLib application;

    public BreizhLibModule(BreizhLib breizhLib) {
        super();
        this.application = breizhLib;
    }

    @Override
    protected void configure() {

        bindConstant().annotatedWith(AppPath.class).to("breizhlib");
        bindConstant().annotatedWith(UaAccount.class).to(BreizhLibConstantes.UA_ACCOUNT);
        bindConstant().annotatedWith(ServerUrl.class).to(BreizhLibConstantes.SERVER_URL);


        bind(CommentaireService.class);
        bind(OuvrageService.class);
        bind(ReservationService.class);
        bind(UtilisateurService.class);
        bind(EmpruntService.class);
        bind(JsonConverter.class);
        bind(ImageCache.class);
        bind(CacheManager.class);
        bind(Database.class);
        bind(Tracker.class);
        bind(VersionTask.class);
        bind(GoogleAuthentification.class);
        bind(ReservationDAO.class);
        bind(LivreDAO.class);
        bind(CommentaireDAO.class);
        //bind(SharedPreferences.class).toInstance(PreferenceManager.getDefaultSharedPreferences(application));

        requestStaticInjection(CacheDialogPreference.class);
        requestStaticInjection(AccountsAdapter.class);
        requestStaticInjection(CommentairesAdapter.class);
        requestStaticInjection(OuvrageAdapter.class);
        requestStaticInjection(ReservationsAdapter.class);
        requestStaticInjection(EmpruntsAdapter.class);


    }
}
