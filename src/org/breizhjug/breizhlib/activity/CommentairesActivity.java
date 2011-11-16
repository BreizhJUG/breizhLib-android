package org.breizhjug.breizhlib.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import com.google.inject.Inject;
import greendroid.widget.*;
import org.breizhjug.breizhlib.R;
import org.breizhjug.breizhlib.activity.gd.AbstractGDActivity;
import org.breizhjug.breizhlib.adapter.CommentairesPagedAdapter;
import org.breizhjug.breizhlib.model.Commentaire;
import org.breizhjug.breizhlib.remote.AsyncPageViewRemoteTask;
import org.breizhjug.breizhlib.remote.CommentaireService;
import org.breizhjug.breizhlib.utils.images.ImageCache;

public class CommentairesActivity extends AbstractGDActivity {

    private PageIndicator mPageIndicatorOther;

    @Inject
    private CommentaireService service;
    @Inject
    private ImageCache imageCache;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setActionBarContentView(R.layout.paged_view);

        final PagedView pagedView = (PagedView) findViewById(R.id.paged_view);
        pagedView.setOnPageChangeListener(mOnPagedViewChangedListener);
        mPageIndicatorOther = (PageIndicator) findViewById(R.id.page_indicator_other);

        setActivePage(pagedView.getCurrentPage());

        initView(null);
        getActionBar().setTitle("Commentaires");
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
                return new CommentairesPagedAdapter(CommentairesActivity.this, items);
            }

            @Override
            protected void onPostExecute(Boolean result) {
                super.onPostExecute(result);
                mPageIndicatorOther.setDotCount(getAdapter().getCount());
            }


            public void onClick(int position) {
                Commentaire commentaire = (Commentaire) getAdapter().getItem(position);
                Intent intent = new Intent(getApplicationContext(), CommentaireActivity.class);
                intent.putExtra("item", commentaire);
                intent.putExtra("items", items);
                intent.putExtra("index", position);
                startActivity(intent);
            }
        };
        initTask.execute((Void) null);
    }


    private void setActivePage(int page) {
        mPageIndicatorOther.setActiveDot(page);
    }

    private PagedView.OnPagedViewChangeListener mOnPagedViewChangedListener = new PagedView.OnPagedViewChangeListener() {

        @Override
        public void onStopTracking(PagedView pagedView) {
        }

        @Override
        public void onStartTracking(PagedView pagedView) {
        }

        @Override
        public void onPageChanged(PagedView pagedView, int previousPage, int newPage) {
            setActivePage(newPage);
        }
    };
}