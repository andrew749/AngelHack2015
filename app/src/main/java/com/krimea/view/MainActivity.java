package com.krimea.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.telephony.gsm.SmsManager;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import com.krimea.R;
import com.krimea.adapter.CustomAdapter;
import com.krimea.util.Constants;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import io.branch.referral.Branch;
import io.branch.referral.BranchError;

public class MainActivity extends Activity {
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
    private Uri uriContact;
    private String contactID;
    public static int[] colors = {R.color.color_turquoise, R.color.color_sun_flower, R.color.color_emerald, R.color.color_wet_asphalt
            , R.color.color_alizarin};

    static String panicid;
    private Set<String> contacts;
    private Set<String> numbers;
    String email, password;
    //state of 0 is panic, 1 is clear
    int state=0;
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
        locationManager.requestLocationUpdates(provider, MIN_UPDATE_TIME, MIN_UPDATE_DISTANCE, locListener);
        contacts = sharedPreferences.getStringSet(Constants.KEY_CONTACTLIST, new HashSet<String>());
        numbers = sharedPreferences.getStringSet(Constants.KEY_NUMBERLIST, new HashSet<String>());

        lv = (ListView) findViewById(R.id.list);
        lv.setAdapter(new CustomAdapter(this, contacts, colors));

        addContact.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, Constants.KEY_PICKCONTACT);
            }
        });
        email = getSharedPreferences("krimeaprefs", Context.MODE_PRIVATE).getString("email", "trollolol");
        password = getSharedPreferences("krimeaprefs", Context.MODE_PRIVATE).getString("password", "whoa");

        panick.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(state==0) {
                    panic p = new panic();
                    p.execute();
                    handler.postDelayed(runnable,1000);
                    state=1;
                    panick.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.ic_assignment_turned_in_white_48dp));
                }else{
                    sendMessage("All Clear", number);
                    allclear clear=new allclear();
                    clear.execute();
                    handler.removeCallbacks(runnable);
                    state=0;
                    panick.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.ic_report_problem_white_48dp));
                }
            }
        });

    }
    Handler handler=new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Random random = new Random();
            String location="" + (43.6469 + random.nextFloat() / 100)+ ", " + (-79.3872 + random.nextFloat() / 100);
            sendMessage("Location: "+location,number);
            locationthread t = new locationthread("" + (43.6469 + random.nextFloat() / 100), "" + (-79.3872 + random.nextFloat() / 100));
            t.execute();
            if(state==1){
                handler.postDelayed(this,10000);
            }
        }
    };

    public String performPost() {
        HttpUriRequest request = new HttpPost("http://45.55.212.205:8000/panic"); // Or HttpPost(), depends on your needs
        String credentials = "andrewcod749@gmail.com" + ":" + "hello";
        String base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
        request.addHeader("Authorization", "Basic " + base64EncodedCredentials);
        HttpParams params = request.getParams();
        request.setParams(params);
        HttpClient httpclient = new DefaultHttpClient();
        InputStream is = null;
        try {
            HttpResponse resp = httpclient.execute(request);
            HttpEntity entity = resp.getEntity();
            is = entity.getContent();
            Log.d("please", "pleaes");
        } catch (IOException e) {
            e.printStackTrace();
        }
        String t = getStringFromInputStream(is);
        Log.d("cmon", t);
        JSONObject obj = null;
        try {
            obj = new JSONObject(t);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            return obj.getString("_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        panicid = t;
        Log.d("d", t);
        return "";
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

    private void sendLocationData(String lat, String lon) {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("lat", lat));
        nameValuePairs.add(new BasicNameValuePair("lon", lon));

        HttpPost request = new HttpPost("http://45.55.212.205:8000/panic/" + panicid + "/update"); // Or HttpPost(), depends on your needs
        String credentials = "andrewcod749@gmail.com" + ":" + "hello";
        String base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
        request.addHeader("Authorization", "Basic " + base64EncodedCredentials);
        try {
            request.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        HttpClient httpclient = new DefaultHttpClient();
        InputStream is = null;
        try {
            HttpResponse resp = httpclient.execute(request);
            HttpEntity entity = resp.getEntity();
            is = entity.getContent();
            Log.d("please", "pleaes");
        } catch (IOException e) {
            e.printStackTrace();
        }
        String t = getStringFromInputStream(is);
        return;
    }

    private String getContactName() {

        String contactName = null;

        // querying contact data store
        Cursor cursor = getContentResolver().query(uriContact, null, null, null, null);

        if (cursor.moveToFirst()) {
            contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
        }

        cursor.close();
        return contactName;
    }

    private String getContactNumber() {

        String contactNumber = null;

        // getting contacts ID
        Cursor cursorID = getContentResolver().query(uriContact,
                new String[]{ContactsContract.Contacts._ID},
                null, null, null);

        if (cursorID.moveToFirst()) {

            contactID = cursorID.getString(cursorID.getColumnIndex(ContactsContract.Contacts._ID));
        }

        cursorID.close();

        Cursor cursorPhone = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},

                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND " +
                        ContactsContract.CommonDataKinds.Phone.TYPE + " = " +
                        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE,

                new String[]{contactID},
                null);

        if (cursorPhone.moveToFirst()) {
            contactNumber = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
        }

        cursorPhone.close();
        return contactNumber;
    }
    private void clear() {
        HttpPost request = new HttpPost("http://45.55.212.205:8000/panic/" + panicid + "/allclear"); // Or HttpPost(), depends on your needs
        String credentials = "andrewcod749@gmail.com" + ":" + "hello";
        String base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
        request.addHeader("Authorization", "Basic " + base64EncodedCredentials);
        HttpClient httpclient = new DefaultHttpClient();
        InputStream is = null;
        try {
            HttpResponse resp = httpclient.execute(request);
            HttpEntity entity = resp.getEntity();
            is = entity.getContent();
            Log.d("please", "pleaes");
        } catch (IOException e) {
            e.printStackTrace();
        }
        String t = getStringFromInputStream(is);
        return;
    }

    class allclear extends AsyncTask<Void, Void, Void> {
        public allclear() {
        }

        @Override
        protected Void doInBackground(Void... params) {
            clear();
            return null;
        }
    }

    class locationthread extends AsyncTask<Void, Void, Void> {
        String clat;
        String clon;

        public locationthread(String clat, String clon) {
            this.clat = clat;
            this.clon = clon;
        }

        @Override
        protected Void doInBackground(Void... params) {
            sendLocationData(clat, clon);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

    class panic extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);
            panicid = aVoid;
            sendMessage("Panicking.\n Open at http://45.55.212.205:8000/panic/"+panicid, number);

        }

        @Override
        protected String doInBackground(Void... params) {
            return performPost();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location loc) {
            if (loc != null && isButtonPressed) {
                Log.d("loc changed", "cmon");
                latitude = loc.getLatitude();
                longitude = loc.getLongitude();
                locationthread t = new locationthread(latitude + "", longitude + "");
                t.execute();
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
        for(String s : numbers) {
            SmsManager.getDefault().sendTextMessage(s, null, message, null, null);
        }
    }

    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        if (reqCode == Constants.KEY_PICKCONTACT && resultCode == Activity.RESULT_OK) {
            uriContact = data.getData();
            String contact = getContactName();
            String number = getContactNumber();
            contacts.add(contact);
            numbers.add(number);
            lv.setAdapter(new CustomAdapter(this, contacts, colors));
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Branch.getInstance(getApplicationContext()).closeSession();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Branch branch = Branch.getInstance(getApplicationContext());
        branch.initSession(new Branch.BranchReferralInitListener() {
            @Override
            public void onInitFinished(JSONObject referringParams, BranchError error) {
                if (error == null) {
                    // params are the deep linked params associated with the link that the user clicked before showing up
                    Log.i("BranchConfigTest", "t link data: " + referringParams.toString());
                }
            }
        }, this.getIntent().getData(), this);
    }

    @Override
    public void onPause() {
        super.onStop();
        if (contacts != null && numbers != null) {
            editor.putStringSet(Constants.KEY_CONTACTLIST, contacts);
            editor.putStringSet(Constants.KEY_NUMBERLIST, numbers);
            editor.commit();
        }
    }
}
