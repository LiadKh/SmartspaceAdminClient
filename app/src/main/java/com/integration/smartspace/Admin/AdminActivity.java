package com.integration.smartspace.Admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.integration.smartspace.Environment.Environment;
import com.integration.smartspace.Layout.UserBoundary;
import com.integration.smartspace.Login.LoginActivity;
import com.integration.smartspace.MainActivity;
import com.integration.smartspace.R;
import com.securepreferences.SecurePreferences;

import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by liadkh on 5/22/19.
 */
public class AdminActivity extends AppCompatActivity implements Environment {

    public static final String USER_BOUNDARY = "userBoundary";
    public final String SMARTSPACE_KEY = "smartspace";
    public final String MAIL_KEY = "email";

    private String mSmartspace, mMail;
    private ProgressDialog mProgressDialog;
    private UserBoundary mUserBoundary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        mProgressDialog = new ProgressDialog(this, R.style.ProgressDialogTheme);
        mProgressDialog.setTitle(R.string.please_wait);
        mProgressDialog.setCancelable(false);
        mUserBoundary = (UserBoundary) getIntent().getSerializableExtra(USER_BOUNDARY);
        if (mUserBoundary != null) {

            if (mUserBoundary.getKey() != null) {
                Map key = mUserBoundary.getKey();
                mSmartspace = (String) key.get(SMARTSPACE_KEY);
                if (mSmartspace != null) {
                    ((TextView) findViewById(R.id.smartspaceName)).setText(mSmartspace);
                }
                mMail = (String) key.get(MAIL_KEY);
            }

            if (mUserBoundary.getAvatar() != null) {
                CircleImageView circleImageView = findViewById(R.id.profile_image);
                if (URLUtil.isValidUrl(mUserBoundary.getAvatar())) {
                    Glide.with(this).load(mUserBoundary.getAvatar()).into(circleImageView);
                }
            }

            if (mUserBoundary.getUsername() != null) {
                ((TextView) findViewById(R.id.username)).setText(mUserBoundary.getUsername());
            }

            if (mUserBoundary.getPoints() != null) {
                ((TextView) findViewById(R.id.pointsText)).setText(new String(getString(R.string.points) + mUserBoundary.getPoints()));
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

        // Create new fragment and transaction
        Fragment newFragment = new AdminDataFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out, android.R.animator.fade_in, android.R.animator.fade_out);
        Bundle bundle = new Bundle();
        bundle.putSerializable(AdminDataFragment.TYPE, FuncTypeEnum.EXPORT_DATA);
        bundle.putString(AdminDataFragment.SMARTSPACE, mSmartspace);
        bundle.putString(AdminDataFragment.MAIL, mMail);
        newFragment.setArguments(bundle);
        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack
        transaction.replace(R.id.tools_admin, newFragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }

    private void importData() {
        // Create new fragment and transaction
        Fragment newFragment = new AdminDataFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out, android.R.animator.fade_in, android.R.animator.fade_out);
        Bundle bundle = new Bundle();
        bundle.putSerializable(AdminDataFragment.TYPE, FuncTypeEnum.IMPORT_DATA);
        bundle.putSerializable(USER_BOUNDARY, mUserBoundary);
        newFragment.setArguments(bundle);
        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack
        transaction.replace(R.id.tools_admin, newFragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }
}
