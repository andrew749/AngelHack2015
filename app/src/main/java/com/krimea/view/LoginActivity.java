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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.krimea.R;
import com.krimea.util.Constants;

import org.apache.http.HttpRequest;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

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

            performPost("email="+email+"&password="+password);
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aVoid) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            LoginActivity.this.finish();
        }
        public void performPost(String encodedData) {
            HttpURLConnection urlc = null;
            OutputStreamWriter out = null;
            DataOutputStream dataout = null;
            BufferedReader in = null;
            try {
                URL url = new URL(":8000/signup");
                urlc = (HttpURLConnection) url.openConnection();
                urlc.setRequestMethod("POST");
                urlc.setDoOutput(true);
                urlc.setDoInput(true);
                urlc.setUseCaches(false);
                urlc.setAllowUserInteraction(false);
                urlc.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
                dataout = new DataOutputStream(urlc.getOutputStream());
                // perform POST operation
                dataout.writeBytes(encodedData);
                int responseCode = urlc.getResponseCode();
                in = new BufferedReader(new InputStreamReader(urlc.getInputStream()),8096);
                String response;
                // write html to System.out for debug
                while ((response = in.readLine()) != null) {
                    System.out.println(response);
                }
                in.close();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
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
