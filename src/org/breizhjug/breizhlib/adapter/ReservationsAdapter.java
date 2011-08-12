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
import org.breizhjug.breizhlib.model.Reservation;
import org.breizhjug.breizhlib.utils.ISBNImageCache;

import java.util.List;


public class ReservationsAdapter extends ArrayAdapter<Reservation> {

    SharedPreferences prefs;

    public ReservationsAdapter(Context context, List<Reservation> reservations, SharedPreferences prefs) {
        super(context, 0, reservations);
        this.prefs = prefs;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        final Reservation reservation = getItem(position);

        if (view == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(R.layout.reservation, null);
        }

        TextView text = (TextView) view.findViewById(R.id.livre);
        text.setText(reservation.livre.titre);

        text = (TextView) view.findViewById(R.id.nom);
        text.setText(reservation.nom + " " + reservation.prenom);

        ImageView icone = (ImageView) view.findViewById(R.id.img);
        ISBNImageCache.getIsbnImageFromCache(reservation.livre.iSBN, reservation.livre.imgUrl, icone);

        return view;
    }
}
