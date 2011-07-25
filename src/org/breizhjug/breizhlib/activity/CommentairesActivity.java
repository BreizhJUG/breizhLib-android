package org.breizhjug.breizhlib.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import org.breizhjub.breizhlib.R;
import org.breizhjug.breizhlib.model.Commentaire;
import org.breizhjug.breizhlib.remote.CommentaireService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CommentairesActivity extends Activity {

    private ListView commentairesListView;



    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.commentaire_list);


        commentairesListView = (ListView) findViewById(R.id.commentaires);

        ArrayList<HashMap<String, String>> listItem = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> map;

        CommentaireService remoteCall = new CommentaireService();

        List<Commentaire> commentaires = remoteCall.load();

        for (Commentaire commentaire : commentaires) {
            map = new HashMap<String, String>();
            map.put("user", commentaire.getNom());
            map.put("titre", commentaire.getLivre());
            map.put("description", commentaire.getCommentaire());
            map.put("img", "");
            listItem.add(map);
        }

        SimpleAdapter mSchedule = new SimpleAdapter(this.getBaseContext(), listItem, R.layout.commentaire,
                new String[]{"img", "user", "titre","description"}, new int[]{R.id.img, R.id.user, R.id.titre, R.id.description});
        commentairesListView.setAdapter(mSchedule);

        commentairesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        	@SuppressWarnings("unchecked")
         	public void onItemClick(AdapterView<?> a, View v, int position, long id) {
        		HashMap<String, String> map = (HashMap<String, String>) commentairesListView.getItemAtPosition(position);
        		AlertDialog.Builder adb = new AlertDialog.Builder(CommentairesActivity.this);
        		adb.setTitle("Commentaire de l'ouvrage "+map.get("titre"));

        		adb.setMessage(map.get("description"));
        		adb.setPositiveButton("Ok", null);
        		adb.show();
        	}
         });


    }
}