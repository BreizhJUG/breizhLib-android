package org.breizhjug.breizhlib.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import greendroid.widget.ActionBar;
import greendroid.widget.ActionBarItem;
import org.breizhjug.breizhlib.BreizhLibConstantes;
import org.breizhjug.breizhlib.R;
import org.breizhjug.breizhlib.activity.compte.CompteList;
import org.breizhjug.breizhlib.activity.common.AbstractGDActivity;

import java.util.ArrayList;
import java.util.List;

public class Menu extends AbstractGDActivity {

    @Override
    public void init(Intent intent) {
        setActionBarContentView(R.layout.main);
        getActionBar().setType(ActionBar.Type.Empty);
        getActionBar().setTitle(getText(R.string.menu_title));
        if (PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean(BreizhLibConstantes.BETA, false)) {
            ActionBarItem item = addActionBarItem(ActionBarItem.Type.Search, R.id.action_bar_search);
        }
        addActionBarItem(ActionBarItem.Type.Info, R.id.action_bar_info);
        loadMenu();
    }

    @Override
    protected void onResume() {
        super.onResume();
       // loadMenu();
    }

    protected void loadMenu() {

        GridView grid = (GridView) findViewById(R.id.grilleBoutons);
        grid.setNumColumns(1);
        List<Bouton> boutons = new ArrayList<Bouton>();
        grid.setAdapter(getBoutonAdapter(boutons));

        Intent intent = null;

        intent = new Intent(getApplicationContext(), OuvragesActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        boutons.add(new Bouton(intent, R.string.ouvrages, R.drawable.book));


        intent = new Intent(getApplicationContext(), CommentairesActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        boutons.add(new Bouton(intent, R.string.commentaires, R.drawable.commentaire));

        if (prefs.getBoolean(BreizhLibConstantes.USER_ADMIN, false)) {
            intent = new Intent(getApplicationContext(), ReservationsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            boutons.add(new Bouton(intent, R.string.reservations, R.drawable.reservation));

            intent = new Intent(getApplicationContext(), EmpruntsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            boutons.add(new Bouton(intent, R.string.emprunts, R.drawable.emprunt));
        }

        intent = new Intent(getApplicationContext(), ScanActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        boutons.add(new Bouton(intent, R.string.scanner, R.drawable.scan));


        intent = new Intent(getApplicationContext(), CompteList.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        boutons.add(new Bouton(intent, R.string.profil, R.drawable.profile));

        if (PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean(BreizhLibConstantes.BETA, false)) {
            //intent = new Intent(getApplicationContext(), SearchActivity.class);
            //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            //boutons.add(new Bouton(intent, R.string.search, android.R.drawable.ic_menu_search));
        }
        Log.i("MENU", "menu loaded");
    }

    @Override
    public boolean onHandleActionBarItemClick(ActionBarItem item, int position) {

        switch (item.getItemId()) {
            case R.id.action_bar_search:
                startActivity(new Intent(this, SearchActivity.class));
                break;
            case R.id.action_bar_info:
                startActivity(new Intent(this, CreditsActivity.class));
                break;
            default:
                return super.onHandleActionBarItemClick(item, position);
        }

        return true;
    }


    class Bouton {
        public int libelle;
        public int icone;
        public Intent intent;

        Bouton(Intent intent, int libelle, int icone) {
            this.intent = intent;
            this.libelle = libelle;
            this.icone = icone;
        }
    }

    protected ListAdapter getBoutonAdapter(List<Bouton> boutons) {
        return new ArrayAdapter<Bouton>(this, 0, boutons) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = convertView;
                if (view == null) {
                    LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    view = vi.inflate(R.layout.bouton, null);
                }

                final Bouton bouton = getItem(position);

                TextView name = (TextView) view.findViewById(R.id.name);
                name.setText(bouton.libelle);
                name.setTextColor(R.color.black_text_color);

                ImageView icone = (ImageView) view.findViewById(R.id.icon);
                icone.setImageResource(bouton.icone);

                view.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {
                        Menu.this.startActivity(bouton.intent);
                    }

                });

                return view;
            }

        };
    }

}
