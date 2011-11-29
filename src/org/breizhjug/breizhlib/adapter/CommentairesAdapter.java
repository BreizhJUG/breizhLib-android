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
    private boolean shortDisplay;
    private int layout;

    public CommentairesAdapter(Context context, List<Commentaire> commentaires) {
        super(context, 0, commentaires);
        this.shortDisplay = true;
        layout = R.layout.commentaire_item;
    }

    public CommentairesAdapter(Context context, List<Commentaire> commentaires, boolean shortDisplay,int layout) {
        super(context, 0, commentaires);
        this.shortDisplay = shortDisplay;
        this.layout = layout;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        Commentaire commentaire = getItem(position);

        if (view == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(layout, null);
        }


        TextView text = (TextView) view.findViewById(R.id.user);
        text.setText(commentaire.nom);

        text = (TextView) view.findViewById(R.id.description);
        text.setText(commentaire.commentaire);

        if (shortDisplay) {

            text = (TextView) view.findViewById(R.id.titre);
            text.setText(commentaire.livre.titre);
            ImageView icone = (ImageView) view.findViewById(R.id.img);
            imageCache.getFromCache(commentaire.livre.iSBN, commentaire.livre.imgUrl, icone);
        }

        return view;
    }
}
