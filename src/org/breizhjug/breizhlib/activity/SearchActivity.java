package org.breizhjug.breizhlib.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.google.inject.Inject;
import org.breizhjug.breizhlib.BreizhLibConstantes;
import org.breizhjug.breizhlib.R;
import org.breizhjug.breizhlib.activity.common.AbstractGDActivity;
import org.breizhjug.breizhlib.adapter.OuvrageAdapter;
import org.breizhjug.breizhlib.model.Livre;
import org.breizhjug.breizhlib.remote.AsyncRemoteTask;
import org.breizhjug.breizhlib.remote.OuvrageService;
import org.breizhjug.breizhlib.remote.Service;
import org.breizhjug.breizhlib.utils.images.ImageCache;
import roboguice.inject.InjectView;

import java.util.HashMap;
import java.util.List;

import static org.breizhjug.breizhlib.utils.IntentSupport.newLivreIntent;

public class SearchActivity extends AbstractGDActivity {

    private String[] from;
    private int[] to;
    private List<HashMap<String, String>> data;
    @Inject
    private OuvrageService service;
    @Inject
    private ImageCache imageCache;

    @InjectView(R.id.editText)
    private EditText mSearchBox;
    @InjectView(R.id.button)
    private ImageButton mSearchButton;
    @InjectView(R.id.items)
    private ListView ouvragesListView;


    private String mSearchTerm;

    private SearchTask mTask;

    @Override
    public void init(Intent intent) {
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        setActionBarContentView(R.layout.items_search);
        getActionBar().setTitle(getText(R.string.search_title));

        mSearchBox.setOnEditorActionListener(mOnEditorAction);
        mSearchButton.setOnClickListener(mOnSearchButtonClick);

        mTask = (SearchTask) getLastNonConfigurationInstance();
        if ((mTask == null) || (mTask.getStatus() == AsyncTask.Status.FINISHED)) {
            mTask = new SearchTask(this, service, ouvragesListView, prefs);
        }
        mTask.activity = this;
    }

    private final View.OnClickListener mOnSearchButtonClick = new View.OnClickListener() {
        public void onClick(final View v) {
            mSearchBox.clearFocus();
            final InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            mSearchTerm = mSearchBox.getText().toString();
            if (mTask.getStatus() == AsyncTask.Status.FINISHED) {
                mTask = new SearchTask(SearchActivity.this, service, ouvragesListView, prefs);
                mTask.activity = SearchActivity.this;
            }
            if (mTask.getStatus() == AsyncTask.Status.PENDING) {
                mTask.execute();
            }
        }
    };


    private final TextView.OnEditorActionListener mOnEditorAction = new TextView.OnEditorActionListener() {
        public boolean onEditorAction(final TextView v, final int actionId, final KeyEvent event) {
            mSearchBox.clearFocus();
            final InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            mSearchTerm = mSearchBox.getText().toString();
            if (mTask.getStatus() == AsyncTask.Status.FINISHED) {
                mTask = new SearchTask(SearchActivity.this, service, ouvragesListView, prefs);
                mTask.activity = SearchActivity.this;
            }
            if (mTask.getStatus() == AsyncTask.Status.PENDING) {
                mTask.execute();
            }
            return false;
        }
    };


    private class SearchTask extends AsyncRemoteTask<Livre> {

        public SearchActivity activity;

        public SearchTask(Activity context, Service<Livre> service, AbsListView listView, SharedPreferences prefs) {
            super(context, service, listView, prefs);
        }

        @Override
        protected Boolean doInBackground(Void... objects) {
            items.addAll(service.search(prefs.getString(BreizhLibConstantes.AUTH_COOKIE, null), mSearchTerm));
            return true;
        }

        @Override
        public ArrayAdapter<Livre> getAdapter() {
            return new OuvrageAdapter(SearchActivity.this.getBaseContext(), items, R.layout.ouvrage);
        }

        public void onClick(int position) {
            Livre livre = (Livre) ouvragesListView.getItemAtPosition(position);
            startActivity(newLivreIntent(getApplicationContext(), items, position, livre));
        }
    }
}