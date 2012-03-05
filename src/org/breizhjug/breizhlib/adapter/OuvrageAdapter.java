package org.breizhjug.breizhlib.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.inject.Inject;
import org.breizhjug.breizhlib.R;
import org.breizhjug.breizhlib.model.Livre;
import org.breizhjug.breizhlib.utils.images.ImageCache;

import java.util.List;


public class OuvrageAdapter extends ArrayAdapter<Livre> {

    @Inject
    private static SharedPreferences prefs;
    @Inject
    private static ImageCache imageCache;
    int resource;

    public OuvrageAdapter(Context context, List<Livre> ouvrages, int resource) {
        super(context, 0, ouvrages);
        this.resource = resource;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        final Livre livre = getItem(position);

        if (view == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(resource, null);
        }

        TextView text = (TextView) view.findViewById(R.id.titre);
        if (text != null) {
            text.setText(livre.titre);
        }

        text = (TextView) view.findViewById(R.id.editeur);
        if (text != null) {
            text.setText(livre.editeur);
        }

        ImageView etat = (ImageView) view.findViewById(R.id.etat);
        if (etat != null) {
            if (livre.etat.equals("RESERVE")) {
                etat.setImageResource(R.drawable.voyant_rouge);
            } else if (livre.etat.equals("DISP0NIBLE")) {
                etat.setImageResource(R.drawable.voyant_vert);
            } else {
                etat.setImageResource(R.drawable.voyant_vert);
            }
        }

        if (livre.nbCommentaire > 0) {
            ImageView comm = (ImageView) view.findViewById(R.id.com_img);
            if (comm != null) {
                comm.setVisibility(View.VISIBLE);
            }
            TextView nb_commentaire = (TextView) view.findViewById(R.id.nb_commentaire);
            if (nb_commentaire != null) {
                nb_commentaire.setText(livre.nbCommentaire);
            }
        } else {
            ImageView comm = (ImageView) view.findViewById(R.id.com_img);
            if (comm != null) {
                comm.setVisibility(View.INVISIBLE);
            }
            TextView nb_commentaire = (TextView) view.findViewById(R.id.nb_commentaire);
            if (nb_commentaire != null) {
                nb_commentaire.setText("");
            }
        }


        ImageView icone = (ImageView) view.findViewById(R.id.img);
        imageCache.getFromCache(livre.iSBN, livre.imgUrl, icone);

        return view;
    }
}
