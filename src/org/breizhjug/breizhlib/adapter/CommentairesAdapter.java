package org.breizhjug.breizhlib.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.inject.Inject;
import org.breizhjug.breizhlib.R;
import org.breizhjug.breizhlib.model.Commentaire;
import org.breizhjug.breizhlib.utils.images.ImageCache;

import java.util.List;


public class CommentairesAdapter extends ArrayAdapter<Commentaire> {

    @Inject
    private static ImageCache imageCache;

    public CommentairesAdapter(Context context, List<Commentaire> commentaires) {
        super(context, 0, commentaires);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        Commentaire commentaire = getItem(position);

        if (view == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(R.layout.commentaire_item, null);
        }

        TextView text = (TextView) view.findViewById(R.id.titre);
        text.setText(commentaire.livre.titre);

        text = (TextView) view.findViewById(R.id.user);
        text.setText(commentaire.nom);

        text = (TextView) view.findViewById(R.id.description);
        text.setText(commentaire.commentaire);

        ImageView icone = (ImageView) view.findViewById(R.id.img);
        imageCache.getFromCache(commentaire.livre.iSBN, commentaire.livre.imgUrl, icone);

        return view;
    }
}
