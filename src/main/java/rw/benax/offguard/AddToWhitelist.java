package rw.benax.offguard;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddToWhitelist extends Activity {
    private SQLiteDatabase sqlitedb;
    EditText inputPhone, inputPerson;
    Button saveBtn;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DBHandler dbh = new DBHandler(AddToWhitelist.this);
        this.sqlitedb = dbh.getWritableDatabase();

        setContentView(R.layout.activity_addtowhitelist);
        inputPhone = (EditText)findViewById(R.id.txtPhone);
        inputPerson = (EditText)findViewById(R.id.txtPerson);

        saveBtn = (Button)findViewById(R.id.btnSave);
        saveBtn.setOnClickListener(v -> {
            String phone = inputPhone.getText().toString().trim();
            String person = inputPerson.getText().toString().trim();

            // Check for empty data in the form
            if (phone.isEmpty() || person.isEmpty()){
                // Prompt user to enter credentials
                Toast.makeText(getApplicationContext(),"Please enter data", Toast.LENGTH_LONG).show();
            }
            else{
                DBHandler.insertContact(this.sqlitedb,phone,person);
                intent = new Intent(AddToWhitelist.this, ViewWhitelist.class);
                startActivity(intent);
                Toast.makeText(getApplicationContext(), "Details Inserted Successfully",Toast.LENGTH_SHORT).show();
                finish();
            }

        });
    }
}