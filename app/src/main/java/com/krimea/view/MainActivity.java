package com.krimea.view;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;


import com.krimea.R;
import com.krimea.util.Constants;

import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private ImageButton addContact;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Set<String> contacts;
    private Set<String> numbers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addContact = (ImageButton) findViewById(R.id.button_addContact);
        sharedPreferences = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);
        editor = sharedPreferences.edit();

        contacts = sharedPreferences.getStringSet(Constants.KEY_CONTACTLIST, new HashSet<String>());
        numbers = sharedPreferences.getStringSet(Constants.KEY_NUMBERLIST, new HashSet<String>());

        addContact.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, Constants.KEY_PICKCONTACT);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        switch (reqCode) {
            case (Constants.KEY_PICKCONTACT) :
                if (resultCode == Activity.RESULT_OK) {

                    Uri contactData = data.getData();
                    Cursor c =  managedQuery(contactData, null, null, null, null);
                    if(c.moveToFirst()) {
                        contacts.add(c.getString(c.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME)));
                        numbers.add(c.getString(c.getColumnIndex(ContactsContract.PhoneLookup.NUMBER)));
                    }
                    }
                }
    }
    @Override
    public void onPause() {
        if(contacts != null && numbers!=null) {
            editor.putStringSet(Constants.KEY_CONTACTLIST, contacts);
            editor.putStringSet(Constants.KEY_NUMBERLIST, numbers);
    }
    }
}
