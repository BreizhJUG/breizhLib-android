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
import org.breizhjug.breizhlib.model.Emprunt;
import org.breizhjug.breizhlib.utils.images.ImageCache;

import java.util.List;


public class EmpruntsAdapter extends ArrayAdapter<Emprunt> {

    SharedPreferences prefs;
    @Inject
    private static ImageCache imageCache;

    public EmpruntsAdapter(Context context, List<Emprunt> emprunts, SharedPreferences prefs) {
        super(context, 0, emprunts);
        this.prefs = prefs;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        final Emprunt emprunt = getItem(position);

        if (view == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(R.layout.reservation, null);
        }

        TextView text = (TextView) view.findViewById(R.id.livre);
        text.setText(emprunt.livre.titre);

        text = (TextView) view.findViewById(R.id.nom);
        text.setText(emprunt.nom + " " + emprunt.prenom);

        ImageView icone = (ImageView) view.findViewById(R.id.img);
        imageCache.getFromCache(emprunt.livre.iSBN, emprunt.livre.imgUrl, icone);

        return view;
    }
}
