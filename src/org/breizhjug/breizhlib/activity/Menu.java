package org.breizhjug.breizhlib.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import org.breizhjug.breizhlib.R;
import org.breizhjug.breizhlib.activity.compte.CompteList;

import java.util.ArrayList;
import java.util.List;

public class Menu extends AbstractActivity {

    @Override
    public void init(Intent intent) {
        loadMenu();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

    }

    protected void loadMenu() {
        SharedPreferences prefs = breizhLib.getSharedPreferences(this);

        GridView grid = (GridView) findViewById(R.id.grilleBoutons);
        List<Bouton> boutons = new ArrayList<Bouton>();
        grid.setAdapter(getBoutonAdapter(boutons));


        Intent intent = new Intent(getApplicationContext(), OuvragesActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        boutons.add(new Bouton(intent, R.string.ouvrages, R.drawable.book));


        intent = new Intent(getApplicationContext(), CommentairesActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        boutons.add(new Bouton(intent, R.string.commentaires, R.drawable.commentaire));

        if (prefs.getBoolean(breizhLib.USER_ADMIN, false)) {
            intent = new Intent(getApplicationContext(), ReservationsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            boutons.add(new Bouton(intent, R.string.reservations, R.drawable.book));

        }
        intent = new Intent(getApplicationContext(), ScanActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        boutons.add(new Bouton(intent, R.string.scanner, R.drawable.scan));


        intent = new Intent(getApplicationContext(), CompteList.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        boutons.add(new Bouton(intent, R.string.profil, android.R.drawable.ic_menu_preferences));

        Log.i("MENU", "menu loaded");
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
