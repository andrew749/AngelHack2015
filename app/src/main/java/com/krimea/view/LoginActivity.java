package com.krimea.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.krimea.R;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            LoginActivity.this.finish();
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
