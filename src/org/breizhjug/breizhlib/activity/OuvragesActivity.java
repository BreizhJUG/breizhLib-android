package org.breizhjug.breizhlib.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import org.breizhjub.breizhlib.R;
import org.breizhjug.breizhlib.model.Livre;
import org.breizhjug.breizhlib.remote.OuvrageService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class OuvragesActivity extends Activity {

    private ListView ouvragesListView;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.items);

        ouvragesListView = (ListView) findViewById(R.id.items);

        ArrayList<HashMap<String, String>> listItem = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> map;

        OuvrageService remoteCall = new OuvrageService();
        List<Livre> books = remoteCall.load();

        for (Livre livre : books) {
            map = new HashMap<String, String>();
            map.put("titre", livre.getTitre());
            map.put("editeur", livre.getEditeur());
            map.put("img", livre.getImgUrl());
            listItem.add(map);
        }

        SimpleAdapter mSchedule = new SimpleAdapter(this.getBaseContext(), listItem, R.layout.ouvrage,
                new String[]{"img", "titre", "editeur"}, new int[]{R.id.img, R.id.titre, R.id.editeur});

        ouvragesListView.setAdapter(mSchedule);

        ouvragesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressWarnings("unchecked")
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {

                HashMap<String, String> map = (HashMap<String, String>) ouvragesListView.getItemAtPosition(position);
                Intent intent = new Intent(getApplicationContext(), LivreActivity.class);
                intent.putExtra("titre",map.get("titre"));
                intent.putExtra("editeur",map.get("editeur"));
                intent.putExtra("img",map.get("img"));
                OuvragesActivity.this.startActivity(intent);
            }
        });

    }


}
