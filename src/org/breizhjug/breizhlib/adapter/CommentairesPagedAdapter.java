package org.breizhjug.breizhlib.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.inject.Inject;
import greendroid.widget.PagedAdapter;
import org.breizhjug.breizhlib.R;
import org.breizhjug.breizhlib.activity.LivreActivity;
import org.breizhjug.breizhlib.model.Commentaire;
import org.breizhjug.breizhlib.model.Livre;
import org.breizhjug.breizhlib.utils.images.ImageCache;

import java.util.ArrayList;
import java.util.List;

import static org.breizhjug.breizhlib.IntentConstantes.*;


public class CommentairesPagedAdapter extends PagedAdapter {

    public List<Commentaire> commentaires;
    @Inject
    private static ImageCache imageCache;
    Activity activity;

    public CommentairesPagedAdapter(Activity activity, List<Commentaire> commentaires) {
        this.commentaires = commentaires;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return commentaires.size();
    }

    @Override
    public Object getItem(int position) {
        if ((commentaires.size() - 1) < position) {
            commentaires.get(getCount());
        }
        return commentaires.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = activity.getLayoutInflater().inflate(R.layout.commentaire_item, parent, false);
        }
        final Commentaire commentaire = (Commentaire) getItem(position);
        TextView text = (TextView) convertView.findViewById(R.id.titre);
        text.setText(commentaire.livre.titre);

        text.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                startLivreActivity(commentaire);
            }
        });

        text = (TextView) convertView.findViewById(R.id.user);
        text.setText(commentaire.nom);

        text = (TextView) convertView.findViewById(R.id.description);
        text.setText(commentaire.commentaire);

        ImageView icone = (ImageView) convertView.findViewById(R.id.img);
        imageCache.getFromCache(commentaire.livre.iSBN, commentaire.livre.imgUrl, icone);

        return convertView;
    }

    private void startLivreActivity(Commentaire commentaire) {
        Intent intent = new Intent(activity.getApplicationContext(), LivreActivity.class);
        ArrayList<Livre> items = new ArrayList<Livre>();
        items.add(commentaire.livre);
        intent.putExtra(ITEMS, items);
        intent.putExtra(INDEX, 0);
        intent.putExtra(ITEM, commentaire.livre);
        activity.startActivity(intent);
    }


}