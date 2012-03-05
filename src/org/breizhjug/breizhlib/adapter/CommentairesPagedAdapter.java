package org.breizhjug.breizhlib.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.inject.Inject;
import greendroid.widget.PagedAdapter;
import org.breizhjug.breizhlib.R;
import org.breizhjug.breizhlib.model.Commentaire;
import org.breizhjug.breizhlib.model.Livre;
import org.breizhjug.breizhlib.utils.images.ImageCache;

import java.util.ArrayList;
import java.util.List;

import static org.breizhjug.breizhlib.utils.IntentSupport.newLivreIntent;


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
            convertView = activity.getLayoutInflater().inflate(R.layout.commentaire, parent, false);
        }
        final Commentaire commentaire = (Commentaire) getItem(position);
        TextView text = (TextView) convertView.findViewById(R.id.titre);
        text.setText(commentaire.livre.titre);

        text.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ArrayList<Livre> items = new ArrayList<Livre>();
                items.add(commentaire.livre);
                activity.startActivity(newLivreIntent(activity.getApplicationContext(), items, 0, commentaire.livre));
            }
        });

        text = (TextView) convertView.findViewById(R.id.user);
        text.setText(commentaire.nom);

        text = (TextView) convertView.findViewById(R.id.description);
        text.setText(commentaire.commentaire);

        ImageView icone = (ImageView) convertView.findViewById(R.id.img);
        imageCache.getFromCache(commentaire.livre.iSBN, commentaire.livre.imgUrl, icone);

        initStars(convertView, commentaire.note);

        return convertView;
    }

    protected void initStars(View convertView, int note) {
        ImageView star1 = (ImageView) convertView.findViewById(R.id.star1);

        ImageView star2 = (ImageView) convertView.findViewById(R.id.star2);

        ImageView star3 = (ImageView) convertView.findViewById(R.id.star3);

        ImageView star4 = (ImageView) convertView.findViewById(R.id.star4);

        ImageView star5 = (ImageView) convertView.findViewById(R.id.star5);
        switch (note) {
            case 0:
                star1.setVisibility(View.INVISIBLE);
            case 1:
                star2.setVisibility(View.INVISIBLE);
            case 2:
                star3.setVisibility(View.INVISIBLE);
            case 3:
                star4.setVisibility(View.INVISIBLE);
            case 4:
                star5.setVisibility(View.INVISIBLE);
            case 5:
                break;
        }
    }
}