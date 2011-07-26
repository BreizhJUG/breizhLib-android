package org.breizhjug.breizhlib.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import org.breizhjub.breizhlib.R;
import org.breizhjug.breizhlib.model.Livre;
import org.breizhjug.breizhlib.utils.ImageUtils;

import java.util.List;


public class OuvrageAdapter extends ArrayAdapter<Livre> {


    public OuvrageAdapter(Context context, List<Livre> ouvrages) {
        super(context, 0,ouvrages);
    }


    @Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		final Livre livre = getItem(position);

        if (view == null) {
			LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = vi.inflate(R.layout.ouvrage, null);
		}

        TextView text= (TextView) view.findViewById(R.id.titre);
        text.setText(livre.getTitre());

         text= (TextView) view.findViewById(R.id.editeur);
         text.setText(livre.getEditeur());

        ImageView icone = (ImageView) view.findViewById(R.id.img);
		icone.setImageDrawable(ImageUtils.imageOperations(livre.getImgUrl()));

		return view;
	}
}
