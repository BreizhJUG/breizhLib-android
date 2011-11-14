package org.breizhjug.breizhlib.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.inject.Inject;
import com.google.inject.internal.Nullable;
import greendroid.widget.ActionBarItem;
import org.breizhjug.breizhlib.R;
import org.breizhjug.breizhlib.model.Commentaire;
import org.breizhjug.breizhlib.utils.images.ImageCache;
import roboguice.inject.InjectView;


public class CommentaireActivity extends AbstractNavigationActivity<Commentaire> {


    @InjectView(R.id.user)
    @Nullable
    TextView user;
    @InjectView(R.id.description)
    TextView description;
    @InjectView(R.id.img)
    ImageView icone;

    @Inject
    private ImageCache imageCache;

    @Override
    public void init(Intent intent) {
    }

    public void initView() {

        getActionBar().addItem(ActionBarItem.Type.Share,R.id.action_bar_share);
        user.setText(item.nom);
        description.setText(item.commentaire);
        getActionBar().setTitle(item.livre.titre);
        imageCache.getFromCache(item.livre.iSBN, item.livre.imgUrl, icone);

        icone.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LivreActivity.class);
                //intent.putExtra("items", items);
                intent.putExtra("index", 0);
                intent.putExtra("item", item.livre);
                CommentaireActivity.this.startActivity(intent);
                finish();
            }
        });

        initNavigation();

        initStars(item.note);


    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActionBarContentView(R.layout.commentaire);
        initView();
    }

    @Override
    protected Class<? extends Activity> getActivityClass() {
        return CommentaireActivity.class;
    }
}