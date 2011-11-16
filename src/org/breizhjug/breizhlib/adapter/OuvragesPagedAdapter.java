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

import java.util.List;


public class OuvragesPagedAdapter extends PagedAdapter {

    public List<Livre> livres;
    @Inject
    private static ImageCache imageCache;
    Activity activity;

    public OuvragesPagedAdapter(Activity activity, List<Livre> livres) {
        this.livres = livres;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return livres.size();
    }

    @Override
    public Object getItem(int position) {
        if ((livres.size() - 1) < position) {
            return livres.get(getCount());
        }
        return livres.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = activity.getLayoutInflater().inflate(R.layout.livre, parent, false);
        }
        Livre livre = (Livre) getItem(position);
        return convertView;
    }

}