package org.breizhjug.breizhlib.adapter;

import android.accounts.Account;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import org.breizhjub.breizhlib.R;
import org.breizhjug.breizhlib.BreizhLib;
import org.breizhjug.breizhlib.utils.ImageDownloader;
import org.breizhjug.breizhlib.utils.Utils;


public class AccountsAdapter extends ArrayAdapter<Account> {

    private final ImageDownloader imageDownloader = BreizhLib.getImageDownloader();

    public AccountsAdapter(Context context, Account[] list) {
        super(context, 0, list);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        final Account item = getItem(position);

        if (view == null) {
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(R.layout.app_info, null);
        }

        TextView text = (TextView) view.findViewById(R.id.welcomme);
        text.setText(item.name);

        ImageView icone = (ImageView) view.findViewById(R.id.avatar);
        imageDownloader.download(Utils.getGravatarImage(item.name), icone);
        return view;
    }
}
