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
import android.os.Bundle;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.telephony.gsm.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;


import com.krimea.R;
import com.krimea.adapter.CustomAdapter;
import com.krimea.util.Constants;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends Activity  {
    private ImageButton addContact;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private double longitude;
    private double latitude;

    private ImageButton panick;
    private String provider;
    private int MIN_UPDATE_TIME = 400;
    private int MIN_UPDATE_DISTANCE = 1000;
    private String message;
    public static boolean isButtonPressed = false;
    private String number = "5197812611";
    private ListView lv;

    public static int[] colors = {R.color.color_turquoise, R.color.color_sun_flower, R.color.color_emerald, R.color.color_wet_asphalt
            , R.color.color_alizarin};

    private Set<String> contacts;
    private Set<String> numbers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addContact = (ImageButton) findViewById(R.id.button_addContact);

        panick = (ImageButton) findViewById(R.id.button_panick);

        sharedPreferences = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);
        editor = sharedPreferences.edit();

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        LocationListener locListener = new MyLocationListener();

        provider = locationManager.getBestProvider(new Criteria(), true);
        locationManager.requestLocationUpdates(provider , MIN_UPDATE_TIME, MIN_UPDATE_DISTANCE, locListener);
        contacts = sharedPreferences.getStringSet(Constants.KEY_CONTACTLIST, new HashSet<String>());
        numbers = sharedPreferences.getStringSet(Constants.KEY_NUMBERLIST, new HashSet<String>());

        lv = (ListView) findViewById(R.id.list);
        lv.setAdapter(new CustomAdapter(this, contacts ,colors));

        addContact.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, Constants.KEY_PICKCONTACT);
            }
        });

        panick.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sendMessage("ayylmao", number);
//                panic p=new panic();
//                p.execute();
                locationthread t=new locationthread();
                t.execute();
            }
        });

    }
    public class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location loc) {
            if(loc != null && isButtonPressed) {
                latitude = loc.getLatitude();
                longitude = loc.getLongitude();
                String message = "My current Latitude = " + latitude + " Longitude = " + longitude;
                sendMessage(message, number);
            }
        }

        @Override
        public void onProviderDisabled(String provider) {
        }


        @Override
        public void onProviderEnabled(String provider) {
        }


        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

    }/* End of Class MyLocationListener */

    public void sendMessage(String message, String number) {
        SmsManager.getDefault().sendTextMessage(number, null, message, null, null);
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
                        numbers.add(c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));

                        lv.setAdapter(new CustomAdapter(this, contacts, colors));
                    }
                    }
                }
    }
    @Override
    public void onStop() {
        super.onPause();
       if(contacts != null && numbers!=null) {
            editor.putStringSet(Constants.KEY_CONTACTLIST, contacts);
            editor.putStringSet(Constants.KEY_NUMBERLIST, numbers);
            editor.commit();
    }
    }
}
