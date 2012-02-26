package org.breizhjug.breizhlib.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import com.google.inject.Inject;
import greendroid.widget.*;
import org.breizhjug.breizhlib.R;
import org.breizhjug.breizhlib.activity.common.AbstractGDActivity;
import org.breizhjug.breizhlib.activity.common.AbstractPagednActivity;
import org.breizhjug.breizhlib.adapter.CommentairesPagedAdapter;
import org.breizhjug.breizhlib.model.Commentaire;
import org.breizhjug.breizhlib.model.Livre;
import org.breizhjug.breizhlib.remote.AsyncPageViewRemoteTask;
import org.breizhjug.breizhlib.remote.CommentaireService;
import org.breizhjug.breizhlib.utils.images.ImageCache;

import java.util.List;


public class CommentaireActivity extends AbstractPagednActivity<Commentaire> {

    @Inject
    private CommentaireService service;
    @Inject
    private ImageCache imageCache;


    @Override
    protected Class<? extends Activity> getActivityClass() {
        return CommentaireActivity.class;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setTitle(getText(R.string.commentaires_title));
        addActionBarItem(ActionBarItem.Type.Refresh, R.id.action_bar_refresh);
    }

    @Override
    public boolean onHandleActionBarItemClick(ActionBarItem item, int position) {
        switch (item.getItemId()) {
            case R.id.action_bar_refresh:
                final LoaderActionBarItem loaderItem = (LoaderActionBarItem) item;
                service.clearDBCache();
                initView(loaderItem);
                return true;
            default:

                return super.onHandleActionBarItemClick(item, position);
        }
    }

    public void init(Intent intent) {
    }

    public void initView(final LoaderActionBarItem loaderItem) {
        final PagedView pagedView = (PagedView) findViewById(R.id.paged_view);
        final AsyncTask<Void, Void, Boolean> initTask = new AsyncPageViewRemoteTask<Commentaire>(this, service, pagedView, prefs) {

            @Override
            public PagedAdapter getAdapter() {
                List<Commentaire> commentaires = CommentaireActivity.this.items;
                if(commentaires == null){
                    commentaires = items;
                }
                return new CommentairesPagedAdapter(CommentaireActivity.this, commentaires);
            }

            @Override
            protected void onPostExecute(Boolean result) {
                super.onPostExecute(result);
                //mPageIndicatorOther.setDotCount(getAdapter().getCount());
                pagedView.smoothScrollToPage(index);
            }


            public void onClick(int position) {

            }
        };
        initTask.execute((Void) null);

    }
}