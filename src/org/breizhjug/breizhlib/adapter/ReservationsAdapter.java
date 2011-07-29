package org.breizhjug.breizhlib.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import org.breizhjub.breizhlib.R;
import org.breizhjug.breizhlib.BreizhLib;
import org.breizhjug.breizhlib.model.Reservation;

import java.util.List;


public class ReservationsAdapter extends ArrayAdapter<Reservation> {


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
        text.setText(reservation.getLivre());

        text = (TextView) view.findViewById(R.id.nom);
        text.setText(reservation.getNom() + " " + reservation.getPrenom());

        ImageView icone = (ImageView) view.findViewById(R.id.img);
        BreizhLib.getInstance(null).getImageDownloader().download(reservation.getImgUrl(), icone);

        return view;
    }
}
