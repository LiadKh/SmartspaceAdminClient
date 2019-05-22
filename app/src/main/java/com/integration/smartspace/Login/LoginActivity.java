package com.integration.smartspace.Login;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.integration.smartspace.R;
import com.securepreferences.SecurePreferences;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText mMail, mPass;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mMail = findViewById(R.id.mail_login);
        mPass = findViewById(R.id.password_login);
        findViewById(R.id.login_button).setOnClickListener(this);
        mProgressDialog = new ProgressDialog(this, R.style.ProgressDialogTheme);
        mProgressDialog.setTitle(R.string.login);
        mProgressDialog.setMessage(getString(R.string.please_wait_login));
        mProgressDialog.setCancelable(false);
    }

    @Override
    public void onClick(View v) {
        String mail = mMail.getText().toString().trim();
        String pass = mPass.getText().toString().trim();
        if ((!TextUtils.isEmpty(mail)) && (!TextUtils.isEmpty(pass))) {
            mProgressDialog.show();
            logMiIn(mail,pass);
        }
        else{
            Toast.makeText(this, R.string.empty_field, Toast.LENGTH_LONG).show();
        }
    }

    private void logMiIn(String mail, String pass) {
        



//        SharedPreferences prefs = new SecurePreferences(this);
//
//        SharedPreferences prefsPass = new SecurePreferences(this, USER_PASSWORD, USER_PASSWORD_xml);


    }
}
