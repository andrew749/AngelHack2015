package com.krimea.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.krimea.R;
import com.krimea.util.Constants;

import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends Activity implements View.OnClickListener {
    private Button login;
    private EditText name;
    private EditText password;
    private EditText email;

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // check Fields For Empty Values
            checkIfEmpty();
        }
    };

    LoginManager manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manager=new LoginManager(getApplicationContext());
        setContentView(R.layout.activity_login);
        login= (Button) findViewById(R.id.login_button);
        name = (EditText) findViewById(R.id.editText_name);
        password = (EditText) findViewById(R.id.editText_pass);
        email = (EditText) findViewById(R.id.editText_email);

        // set listeners
        login.setOnClickListener(this);
        name.addTextChangedListener(textWatcher);
        password.addTextChangedListener(textWatcher);
        email.addTextChangedListener(textWatcher);
        checkIfEmpty();
    }

    @Override
    public void onClick(View v) {
        if (v == findViewById(R.id.login_button)) {
            String n = name.getText().toString();
            String e = password.getText().toString();
            String p = email.getText().toString();
            manager.storeUserData(n,e,p);
            LoginTask exec=new LoginTask(n,e,p);
            exec.execute();
        }
    }

    class LoginTask extends AsyncTask<Void,Void,Boolean>{
        String name,email,password;
        public LoginTask(String name,String email,String password) {
            this.name=name;
            this.email=email;
            this.password=password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            return performPost();
        }

        @Override
        protected void onPostExecute(Boolean aVoid) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            LoginActivity.this.finish();
        }
        private String getB64Auth (String login, String pass) {
            String source=login+":"+pass;
            String ret="Basic "+Base64.encodeToString(source.getBytes(),Base64.URL_SAFE| Base64.NO_WRAP);
            return ret;
        }
        public Boolean performPost() {
            HttpUriRequest request = new HttpPost("http://45.55.212.205:8000/signup");
            HttpParams params=request.getParams();
            params.setParameter("email",email);
            params.setParameter("password",password);
            request.setParams(params);
            HttpClient httpclient = new DefaultHttpClient();
            InputStream is=null;
            try {
                HttpResponse resp=httpclient.execute(request);
                HttpEntity entity=resp.getEntity();
                is=entity.getContent();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            String t=getStringFromInputStream(is);
            return true;
        }
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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
    private void checkIfEmpty(){

        String n = name.getText().toString();
        String e = password.getText().toString();
        String p = email.getText().toString();

        if(n.equals("")|| e.equals("") || p.equals("")){
            login.setEnabled(false);
        } else {
            login.setEnabled(true);
        }
    }

}
