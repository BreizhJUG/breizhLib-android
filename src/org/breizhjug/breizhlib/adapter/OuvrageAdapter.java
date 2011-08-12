package org.breizhjug.breizhlib.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import org.breizhjug.breizhlib.R;
import org.breizhjug.breizhlib.model.Livre;
import org.breizhjug.breizhlib.utils.ISBNImageCache;

import java.util.List;


public class OuvrageAdapter extends ArrayAdapter<Livre> {

    SharedPreferences prefs;
    int resource;

    public OuvrageAdapter(Context context, List<Livre> ouvrages, int resource, SharedPreferences prefs) {
        super(context, 0, ouvrages);
        this.prefs = prefs;
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

        ImageView icone = (ImageView) view.findViewById(R.id.img);
        ISBNImageCache.getIsbnImageFromCache(livre.iSBN,livre.imgUrl, icone);

        return view;
    }
}
