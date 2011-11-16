package org.breizhjug.breizhlib.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import greendroid.widget.LoaderActionBarItem;
import greendroid.widget.PageIndicator;
import greendroid.widget.PagedView;
import org.breizhjug.breizhlib.R;
import org.breizhjug.breizhlib.activity.gd.AbstractGDActivity;
import roboguice.inject.InjectExtra;

import java.io.Serializable;
import java.util.ArrayList;


public abstract class AbstractPagednActivity<T extends Serializable> extends AbstractGDActivity {

    @InjectExtra("item")
    T item;
    @InjectExtra("index")
    int index;
    @InjectExtra(value = "items", optional = true)
    ArrayList<T> items;

    protected PageIndicator mPageIndicatorOther;


    protected abstract Class<? extends Activity> getActivityClass();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActionBarContentView(R.layout.paged_view);

        final PagedView pagedView = (PagedView) findViewById(R.id.paged_view);
        pagedView.setOnPageChangeListener(mOnPagedViewChangedListener);
        mPageIndicatorOther = (PageIndicator) findViewById(R.id.page_indicator_other);
        setActivePage(index);

        initView(null);
        pagedView.smoothScrollToPage(index);

    }

    public abstract void initView(final LoaderActionBarItem loaderItem);

    private void setActivePage(int page) {
        mPageIndicatorOther.setActiveDot(page);
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