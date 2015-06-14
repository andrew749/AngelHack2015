package com.krimea.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.telephony.gsm.SmsManager;
import android.util.Base64;
import android.util.Log;
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
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity  {
    private ImageButton addContact;
    private ImageButton panick;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private double longitude;
    private double latitude;

    private String provider;
    private int MIN_UPDATE_TIME = 400;
    private int MIN_UPDATE_DISTANCE = 1000;
    private String message;
    private String number = "4165287547";
    static String panicid;
    private Set<String> contacts;
    private Set<String> numbers;
    String email,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addContact = (ImageButton) findViewById(R.id.button_addContact);
        panick = (ImageButton) findViewById(R.id.button_panick);

        sharedPreferences = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);
        editor = sharedPreferences.edit();

//        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        LocationListener locListener = new LocationListener() {
//            @Override
//            public void onLocationChanged(Location location) {
//                latitude = location.getLatitude();
//                longitude = location.getLongitude();
//                String message = "My current Latitude = " + latitude  + " Longitude = " + longitude;
//            }

//            @Override
//            public void onStatusChanged(String provider, int status, Bundle extras) {
//
//            }
//
//            @Override
//            public void onProviderEnabled(String provider) {
//
//            }
//
//            @Override
//            public void onProviderDisabled(String provider) {
//
//            }
//        };

//        provider = locationManager.getBestProvider(new Criteria(), true);
//        locationManager.requestLocationUpdates(provider , MIN_UPDATE_TIME, MIN_UPDATE_DISTANCE, this);
//        contacts = sharedPreferences.getStringSet(Constants.KEY_CONTACTLIST, new HashSet<String>());
//        numbers = sharedPreferences.getStringSet(Constants.KEY_NUMBERLIST, new HashSet<String>());

        addContact.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, Constants.KEY_PICKCONTACT);
            }
        });
        email=getSharedPreferences("krimeaprefs", Context.MODE_PRIVATE).getString("email","trollolol");
        password=getSharedPreferences("krimeaprefs",Context.MODE_PRIVATE).getString("password","whoa");

        panick.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sendMessage("ayylmao", number);
                postData();
            }
        });
    }
    private String getB64Auth (String login, String pass) {
        String source=login+":"+pass;
        String ret="Basic "+Base64.encodeToString(source.getBytes(),Base64.URL_SAFE| Base64.NO_WRAP);
        return ret;
    }
    public String performPost() {
        URL d= null;
        try {
            d = new URL("http://45.55.212.205:8000/panic");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection)d.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (conn != null) {
            conn.setRequestProperty("Authorization", getB64Auth("andrewcod749@gmail.com","hello"));
        }
        conn.setReadTimeout(30000);
        conn.setInstanceFollowRedirects(true);
        InputStream is=null;
        try {
            is=conn.getInputStream();
            Log.d("please", "fdafdas");
        } catch (IOException e) {
            e.printStackTrace();
        }
        String t=getStringFromInputStream(is);
        return t;
    }


    // convert InputStream to String
    private static String getStringFromInputStream(InputStream is) {

        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        String line;
        try {

            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.toString();

    }
    private void sendLocationData(Location location){
        HttpUriRequest request = new HttpGet("45.55.212.205:8000/panic/"+panicid+"/update"); // Or HttpPost(), depends on your needs
        String credentials = email + ":" + password;
        String base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
        request.addHeader("Authorization", "Basic " + base64EncodedCredentials);
        HttpClient httpclient = new DefaultHttpClient();
        try {
            httpclient.execute(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void postData() {
        HttpUriRequest request = new HttpGet("http://45.55.212.205:8000/panic"); // Or HttpPost(), depends on your needs
        String credentials = email + ":" + password;
        String base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
        request.addHeader("Authorization", "Basic " + base64EncodedCredentials);

        HttpClient httpclient = new DefaultHttpClient();
        try {
           HttpResponse response=httpclient.execute(request);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return;
    }
    class panic extends AsyncTask<Void,Void,String>{
        public panic() {
        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);
            panicid=aVoid;
        }

        @Override
        protected String doInBackground(Void... params) {
            postData();
            return null ;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void sendMessage(String message, String number) {
        SmsManager.getDefault().sendTextMessage(number, null, message, null, null);
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
//    @Override
//    public void onPause() {
//        if(contacts != null && numbers!=null) {
//            editor.putStringSet(Constants.KEY_CONTACTLIST, contacts);
//            editor.putStringSet(Constants.KEY_NUMBERLIST, numbers);
//    }
//    }
}
