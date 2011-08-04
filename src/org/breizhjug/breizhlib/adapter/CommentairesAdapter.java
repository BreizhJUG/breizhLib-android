package org.breizhjug.breizhlib.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import org.breizhjug.breizhlib.R;
import org.breizhjug.breizhlib.model.Commentaire;

import java.util.List;


public class CommentairesAdapter extends ArrayAdapter<Commentaire> {


    public CommentairesAdapter(Context context, List<Commentaire> commentaires) {
        super(context, 0, commentaires);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        Commentaire commentaire = getItem(position);

        if (view == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(R.layout.commentaire, null);
        }

        TextView text = (TextView) view.findViewById(R.id.titre);
        text.setText(commentaire.livre.titre);

        text = (TextView) view.findViewById(R.id.user);
        text.setText(commentaire.nom);

        text = (TextView) view.findViewById(R.id.description);
        text.setText(commentaire.commentaire);

        return view;
    }
}
