package rw.benax.offguard;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class DBHandler extends SQLiteOpenHelper{
    static String DATABASE_NAME = "my.db";
    public static final String TBL_WHITELIST = "whitelist";
    public static final String col_id = "id";
    public static final String col_phone_number = "phone_number";
    public static final String col_contact_person = "contact_person";

    public DBHandler(Context context){
        super(context, DATABASE_NAME, (SQLiteDatabase.CursorFactory) null, 1);
    }

    public void onCreate(SQLiteDatabase sQLiteDatabase) {
        sQLiteDatabase.execSQL(String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, %s TEXT, %s TEXT)", TBL_WHITELIST, col_id, col_phone_number, col_contact_person));
    /***
        String initial_whitelist_phones[] = {"0788301945", "0788548000", "0791255485", "0783070801"};
        String initial_whitelist_persons[] = {"Jean Claude, NGA", "Dr. Papias, RCA", "Gabriel-Super-Private", "DalYoung-Public"};

        for(int k=0; k<initial_whitelist_phones.length; k++){
            this.insertContact(sQLiteDatabase, initial_whitelist_phones[k], initial_whitelist_persons[k]);
        }
    ***/
     }

    public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        sQLiteDatabase.execSQL(String.format("DROP TABLE IF EXISTS %s", TBL_WHITELIST));
        onCreate(sQLiteDatabase);
    }

    public static void insertContact(SQLiteDatabase db, String phone, String person){
        db.execSQL(String.format("INSERT INTO %s(%s, %s) VALUES('%s', '%s')", TBL_WHITELIST, col_phone_number, col_contact_person, phone, person));
    }

    public static void deleteContact(SQLiteDatabase db, String row_id){
        db.execSQL(String.format("DELETE FROM %s WHERE id='%s'", TBL_WHITELIST, row_id));
    }

    @SuppressLint("Range")
    public ArrayList<HashMap<String, String>> getExistingWhitelist(){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<HashMap<String, String>> userList = new ArrayList<>();
        String query = "SELECT * FROM "+ TBL_WHITELIST;
        Cursor cursor = db.rawQuery(query,null);
        while (cursor.moveToNext()){
            HashMap<String,String> user = new HashMap<>();
            user.put(col_id,cursor.getString(cursor.getColumnIndex(col_id)));
            user.put(col_phone_number,cursor.getString(cursor.getColumnIndex(col_phone_number)));
            user.put(col_contact_person,cursor.getString(cursor.getColumnIndex(col_contact_person)));
            userList.add(user);
        }
        return  userList;
    }

}