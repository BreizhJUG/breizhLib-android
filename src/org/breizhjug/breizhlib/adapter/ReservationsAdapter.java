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
import org.breizhjug.breizhlib.model.Reservation;
import org.breizhjug.breizhlib.utils.images.ImageCache;

import java.util.List;


public class ReservationsAdapter extends ArrayAdapter<Reservation> {

    @Inject
    private static SharedPreferences prefs;
    @Inject
    private static ImageCache imageCache;

    public ReservationsAdapter(Context context, List<Reservation> reservations) {
        super(context, 0, reservations);
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
        text.setText(reservation.nom);

        ImageView icone = (ImageView) view.findViewById(R.id.img);
        imageCache.getFromCache(reservation.livre.iSBN, reservation.livre.imgUrl, icone);

        return view;
    }
}
