package com.integration.smartspace;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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
import com.integration.smartspace.Login.LoginActivity;
import com.securepreferences.SecurePreferences;

import java.io.IOException;

/**
 * Created by liadkh on 5/22/19.
 */
public class MainActivity extends AppCompatActivity implements Preferences, Environment {

    final int SPLASH_TIME_OUT = 1500;

    @Override
    protected void onStart() {
        super.onStart();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                login();
            }

        }, SPLASH_TIME_OUT);
    }

    private void login() {
        SharedPreferences prefs = new SecurePreferences(this);
        String smartspace = prefs.getString(USER_SMARTSPACE, null);
        String mail = prefs.getString(USER_MAIL, null);
        final String baseURL = prefs.getString(BASE_URL, null);
        if (smartspace != null && mail != null && baseURL != null) {

            final String url = baseURL + LOGIN + "/" + smartspace + "/" + mail;

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
                                        Intent intent = new Intent(getBaseContext(), AdminActivity.class);
                                        intent.putExtra(AdminActivity.USER_BOUNDARY, userBoundary);
                                        intent.putExtra(BASE_URL, baseURL);
                                        startActivity(intent);
                                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                        finish();
                                        return;
                                    } catch (IOException e) {
                                        clearSharedPreferences();
                                        return;
                                    }

                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(final VolleyError error) {
                            clearSharedPreferences();
                            return;
                        }
                    });

                    // Add the request to the RequestQueue.
                    queue.add(stringRequest);
                }
            }).start();

        } else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }
    }

    private void clearSharedPreferences() {
//        SharedPreferences prefs = new SecurePreferences(this);
//        SharedPreferences.Editor editor = prefs.edit();
//        editor.clear();
//        editor.apply();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
