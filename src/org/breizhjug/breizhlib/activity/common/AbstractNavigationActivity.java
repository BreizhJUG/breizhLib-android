package org.breizhjug.breizhlib.activity.common;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.google.inject.internal.Nullable;
import org.breizhjug.breizhlib.R;
import org.breizhjug.breizhlib.activity.common.AbstractGDActivity;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;

import java.io.Serializable;
import java.util.ArrayList;


public abstract class AbstractNavigationActivity<T extends Serializable> extends AbstractGDActivity {

    @InjectView(R.id.nav)
    @Nullable
    LinearLayout nav;
    @InjectView(R.id.star1)
    @Nullable
    ImageView star1;
    @InjectView(R.id.star2)
    @Nullable
    ImageView star2;
    @InjectView(R.id.star3)
    @Nullable
    ImageView star3;
    @InjectView(R.id.star4)
    @Nullable
    ImageView star4;
    @InjectView(R.id.star5)
    @Nullable
    ImageView star5;

    @InjectExtra("item")
    T item;
    @InjectExtra("index")
    int index;
    @InjectExtra(value = "items", optional = true)
    ArrayList<T> items;


    protected abstract Class<? extends Activity> getActivityClass();


    protected void initNavigation() {
        Button previous = (Button) nav.getChildAt(0);
        Button next = (Button) nav.getChildAt(1);

        if (items != null) {
            if (index > 0) {
                previous.setOnClickListener(new Button.OnClickListener() {

                    public void onClick(View view) {
                        onPrevious();
                    }
                });
            } else {
                previous.setEnabled(false);
            }


            if (items.size() - 1 > index) {
                next.setOnClickListener(new Button.OnClickListener() {

                    public void onClick(View view) {
                        onNext();
                    }
                });
            } else {
                next.setEnabled(false);
            }
        } else {
            nav.setEnabled(false);
            nav.setVisibility(View.INVISIBLE);
            previous.setEnabled(false);
            next.setEnabled(false);
        }
    }

    private void onPrevious() {
        Intent intent = new Intent(getApplicationContext(), getActivityClass());
        intent.putExtra("item", items.get(index - 1));
        intent.putExtra("items", items);
        intent.putExtra("index", index - 1);
        startActivity(intent);
        finish();
    }

    private void onNext() {
        Intent intent = new Intent(getApplicationContext(), getActivityClass());
        intent.putExtra("item", items.get(index + 1));
        intent.putExtra("items", items);
        intent.putExtra("index", index + 1);
        startActivity(intent);
        finish();
    }

    protected void initStars(int note) {
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