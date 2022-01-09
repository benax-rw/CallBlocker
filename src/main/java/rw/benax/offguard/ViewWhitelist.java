package rw.benax.offguard;

import static rw.benax.offguard.DBHandler.*;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class ViewWhitelist extends Activity {
    private SQLiteDatabase sqlitedb;
    private ArrayList<HashMap<String, String>> userList = new ArrayList<>();
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewwhitelist);

        DBHandler dbh = new DBHandler(ViewWhitelist.this);
        this.sqlitedb = dbh.getWritableDatabase();
        userList = dbh.getExistingWhitelist();
        ListView lv = (ListView) findViewById(R.id.user_list);

        ListAdapter adapter = new SimpleAdapter(ViewWhitelist.this, userList, R.layout.list_row,new String[]{"contact_person","phone_number"}, new int[]{R.id.person, R.id.phone});
        lv.setAdapter(adapter);

        lv.setOnItemLongClickListener((arg0, v, index, arg3) -> {
            HashMap<String, String> data = userList.get(index);
            String record_id = data.get("id");

            deleteContact(sqlitedb,record_id);
            Toast.makeText(getApplicationContext(), "Record Deleted",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), ViewWhitelist.class);
            startActivity(intent);
            finish();
            return false;
        });

        Button add = (Button)findViewById(R.id.btnAdd);
        add.setOnClickListener(v -> {
            intent = new Intent(ViewWhitelist.this,AddToWhitelist.class);
            startActivity(intent);
            finish();
        });

        Button home = (Button)findViewById(R.id.btnHome);
        home.setOnClickListener(v -> {
            intent = new Intent(ViewWhitelist.this,MainActivity.class);
            startActivity(intent);
            finish();
        });

    }
}