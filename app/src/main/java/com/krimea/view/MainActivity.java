package com.krimea.view;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
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

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private ImageButton addContact;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Set<String> contacts;
    private Set<String> numbers;
    String email,password;
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
        email=getSharedPreferences("krimeaprefs", Context.MODE_PRIVATE).getString("email","trollolol");
        password=getSharedPreferences("krimeaprefs",Context.MODE_PRIVATE).getString("password","whoa");
    }
    private void sendLocationData(){

    }
    public void postData() {
        RequestParams params = new RequestParams();
        params.put("username", email);
        params.put("password", password);

        AsyncHttpClient client = new AsyncHttpClient();
        client.post("https://url.com/getdata", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String response) {
                // success
                //start service to send data to node server
            }

            @Override
            public void onFailure(Throwable error, String content) {
                // something went wrong
                //lolz gg
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
