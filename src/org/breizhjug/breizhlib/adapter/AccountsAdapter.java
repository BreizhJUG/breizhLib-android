package org.breizhjug.breizhlib.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import org.breizhjug.breizhlib.BreizhLib;
import org.breizhjug.breizhlib.R;
import org.breizhjug.breizhlib.utils.Gravatar;

import java.util.List;


public class AccountsAdapter extends ArrayAdapter<String> {

    public AccountsAdapter(Context context, List<String> list) {
        super(context, 0, list);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        String email = getItem(position);

        if (view == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(R.layout.app_info, null);
        }

        TextView text = (TextView) view.findViewById(R.id.welcomme);
        text.setText(email);

        ImageView icone = (ImageView) view.findViewById(R.id.avatar);
        BreizhLib.getImageCache().getFromCache("avatar",Gravatar.getImage(email), icone);
        return view;
    }
}
