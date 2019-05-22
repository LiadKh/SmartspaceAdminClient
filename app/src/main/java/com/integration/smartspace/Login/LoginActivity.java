package com.integration.smartspace.Login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.integration.smartspace.Admin.AdminActivity;
import com.integration.smartspace.Environment.Environment;
import com.integration.smartspace.Environment.Preferences;
import com.integration.smartspace.Layout.UserBoundary;
import com.integration.smartspace.Layout.UserRole;
import com.integration.smartspace.R;
import com.securepreferences.SecurePreferences;

import java.io.IOException;
import java.net.URL;

/**
 * Created by liadkh on 5/22/19.
 */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener, Preferences, Environment {

    private EditText mMail, mSmartspcae;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mSmartspcae = findViewById(R.id.smartspcae_login);
        mMail = findViewById(R.id.mail_login);
        findViewById(R.id.login_button).setOnClickListener(this);
        mProgressDialog = new ProgressDialog(this, R.style.ProgressDialogTheme);
        mProgressDialog.setTitle(R.string.login);
        mProgressDialog.setMessage(getString(R.string.please_wait_login));
        mProgressDialog.setCancelable(false);
    }

    @Override
    public void onClick(View v) {
        String smartspcae = mSmartspcae.getText().toString().trim();
        String mail = mMail.getText().toString().trim();
        if ((!TextUtils.isEmpty(smartspcae) && (!TextUtils.isEmpty(mail)))) {
            mProgressDialog.show();
            logMiIn(smartspcae, mail);
        } else {
            Toast.makeText(this, R.string.empty_field, Toast.LENGTH_LONG).show();
        }
    }

    private void logMiIn(final String adminSmartspace, final String adminEmail) {
        final String url = BASE_URL + LOGIN + "/" + adminSmartspace + "/" + adminEmail;

        new Thread(new Runnable() {
            @Override
            public void run() {

                // Instantiate the RequestQueue.
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

                // Request a string response from the provided URL.
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                ObjectMapper mapper = new ObjectMapper();
                                UserBoundary userBoundary = new UserBoundary();

                                try {
                                    userBoundary = mapper.readValue(response, UserBoundary.class);
                                } catch (IOException e) {
                                    Toast.makeText(LoginActivity.this, R.string.angain_later, Toast.LENGTH_LONG).show();
                                    return;
                                }

                                if (userBoundary != null && userBoundary.getRole() == UserRole.ADMIN) {
                                    savePreference(adminSmartspace, adminEmail);
                                    Intent intent = new Intent(getApplicationContext(), AdminActivity.class);
                                    startActivity(intent);
                                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                    finish();
                                } else {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            mProgressDialog.cancel();
                                            Toast.makeText(LoginActivity.this, R.string.not_admin, Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(final VolleyError error) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(LoginActivity.this, R.string.angain_later, Toast.LENGTH_LONG).show();
                                mProgressDialog.cancel();
                            }
                        });
                    }
                });

                // Add the request to the RequestQueue.
                queue.add(stringRequest);
            }
        }).start();
    }

    private void savePreference(String adminSmartspace, String adminEmail) {
        SharedPreferences prefs = new SecurePreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(USER_SMARTSPACE, adminSmartspace);
        editor.putString(USER_MAIL, adminEmail);
        editor.apply();
        mProgressDialog.cancel();
    }
}
