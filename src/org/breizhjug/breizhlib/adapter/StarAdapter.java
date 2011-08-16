package org.breizhjug.breizhlib.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import org.breizhjug.breizhlib.R;

import java.util.List;

public class StarAdapter extends ArrayAdapter<String> {


    public StarAdapter(Context context, List<String> list) {
        super(context, R.array.stars, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.stars, null);
        }


        initStars(convertView, position);

        return convertView;
    }


    private void initStars(View convertView, int note) {
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
