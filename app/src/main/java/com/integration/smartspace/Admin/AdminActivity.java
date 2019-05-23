package com.integration.smartspace.Admin;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.integration.smartspace.Environment.Environment;
import com.integration.smartspace.Layout.UserBoundary;
import com.integration.smartspace.Layout.UserRole;
import com.integration.smartspace.Login.LoginActivity;
import com.integration.smartspace.MainActivity;
import com.integration.smartspace.R;
import com.securepreferences.SecurePreferences;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdminActivity extends AppCompatActivity implements Environment {

    public static final String USER_BOUNDARY = "userBoundary";
    public final String SMARTSPACE_KEY = "smartspace";
    public final String MAIL_KEY = "email";

    private String mSmartspace, mMail;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        mProgressDialog = new ProgressDialog(this, R.style.ProgressDialogTheme);
        mProgressDialog.setTitle(R.string.please_wait);
        mProgressDialog.setCancelable(false);
        UserBoundary userBoundary = (UserBoundary) getIntent().getSerializableExtra(USER_BOUNDARY);
        if (userBoundary != null) {

            if (userBoundary.getKey() != null) {
                Map key = userBoundary.getKey();
                mSmartspace = (String) key.get(SMARTSPACE_KEY);
                if (mSmartspace != null) {
                    ((TextView) findViewById(R.id.smartspaceName)).setText(mSmartspace);
                }
                mMail = (String) key.get(MAIL_KEY);
            }

            if (userBoundary.getAvatar() != null) {
                CircleImageView circleImageView = findViewById(R.id.profile_image);
                if (URLUtil.isValidUrl(userBoundary.getAvatar())) {
                    Glide.with(this).load(userBoundary.getAvatar()).into(circleImageView);
                }
            }

            if (userBoundary.getUsername() != null) {
                ((TextView) findViewById(R.id.username)).setText(userBoundary.getUsername());
            }

            if (userBoundary.getPoints() != null) {
                ((TextView) findViewById(R.id.pointsText)).setText(new String(getString(R.string.points) + userBoundary.getPoints()));
            } else {
                ((TextView) findViewById(R.id.pointsText)).setText(new String(getString(R.string.points) + 0));
            }

            findViewById(R.id.import_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    importData();
                }
            });

            findViewById(R.id.export_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    exportData();
                }
            });

            findViewById(R.id.logout_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    logout();
                }
            });
        } else {
            SharedPreferences prefs = new SecurePreferences(this);
            SharedPreferences.Editor editor = prefs.edit();
            editor.clear();
            editor.apply();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }
    }

    private void logout() {
        mProgressDialog.setMessage(getString(R.string.logout));
        mProgressDialog.show();
        SharedPreferences prefs = new SecurePreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();
        Intent intent = new Intent(this, MainActivity.class);
        mProgressDialog.cancel();
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();

    }

    private void exportData() {
        mProgressDialog.setMessage(getString(R.string.export_data));
        mProgressDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = BASE_URL + EXPORT + "/" + mSmartspace + "/" + mMail;
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

                // Request a string response from the provided URL.
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                File file;
                                try {
                                    file = File.createTempFile(System.currentTimeMillis() + "", ".jason", getBaseContext().getCacheDir());
                                    FileOutputStream stream = new FileOutputStream(file);
                                    stream.write(response.getBytes());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                mProgressDialog.cancel();
                                Intent sendIntent = new Intent();
                                sendIntent.setAction(Intent.ACTION_SEND);
                                sendIntent.putExtra(Intent.EXTRA_TEXT, R.string.send_to);
                                sendIntent.setType("text/plain");
                                startActivity(Intent.createChooser(sendIntent, getResources().getText(R.string.send_to)));
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(final VolleyError error) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(AdminActivity.this, R.string.again_later, Toast.LENGTH_LONG).show();
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

    private void importData() {
        mProgressDialog.setMessage(getString(R.string.import_data));
        mProgressDialog.show();
    }
}
