package org.breizhjug.breizhlib.guice;

import com.google.inject.Singleton;
import org.breizhjug.breizhlib.BreizhLib;
import org.breizhjug.breizhlib.BreizhLibConstantes;
import org.breizhjug.breizhlib.adapter.*;
import org.breizhjug.breizhlib.database.Database;
import org.breizhjug.breizhlib.database.dao.CommentaireDAO;
import org.breizhjug.breizhlib.database.dao.EmpruntDAO;
import org.breizhjug.breizhlib.database.dao.LivreDAO;
import org.breizhjug.breizhlib.database.dao.ReservationDAO;
import org.breizhjug.breizhlib.remote.*;
import org.breizhjug.breizhlib.utils.*;
import org.breizhjug.breizhlib.utils.images.ImageCache;
import org.breizhjug.breizhlib.utils.version.VersionTask;
import org.breizhjug.breizhlib.view.CacheDialogPreference;
import roboguice.config.AbstractAndroidModule;


public class BreizhLibModule extends AbstractAndroidModule {

    private BreizhLib application;

    public BreizhLibModule(BreizhLib breizhLib) {
        super();
        this.application = breizhLib;
    }

    @Override
    protected void configure() {

        bindConstant().annotatedWith(AppPath.class).to("breizhlib");
        bindConstant().annotatedWith(UaAccount.class).to(BreizhLibConstantes.UA_ACCOUNT);
        bindConstant().annotatedWith(ServerUrl.class).to(BreizhLibConstantes.SERVER_URL);


        bind(CommentaireService.class).in(Singleton.class);
        bind(OuvrageService.class).in(Singleton.class);
        bind(ReservationService.class).in(Singleton.class);
        bind(UtilisateurService.class).in(Singleton.class);
        bind(EmpruntService.class).in(Singleton.class);
        bind(JsonConverter.class).in(Singleton.class);
        bind(ImageCache.class).in(Singleton.class);
        bind(CacheManager.class).in(Singleton.class);
        bind(Database.class).in(Singleton.class);
        bind(Tracker.class).in(Singleton.class);
        bind(VersionTask.class).in(Singleton.class);
        bind(Authentification.class).to(BreizhLibAuthentification.class).in(Singleton.class);
        bind(ReservationDAO.class).in(Singleton.class);
        bind(LivreDAO.class).in(Singleton.class);
        bind(CommentaireDAO.class).in(Singleton.class);
        bind(EmpruntDAO.class).in(Singleton.class);
        //bind(SharedPreferences.class).toInstance(PreferenceManager.getDefaultSharedPreferences(application));


        requestStaticInjection(OuvragesPagedAdapter.class);

        requestStaticInjection(CommentairesPagedAdapter.class);
        requestStaticInjection(CacheDialogPreference.class);
        requestStaticInjection(AccountsAdapter.class);
        requestStaticInjection(CommentairesAdapter.class);
        requestStaticInjection(OuvrageAdapter.class);
        requestStaticInjection(ReservationsAdapter.class);
        requestStaticInjection(EmpruntsAdapter.class);


    }
}
