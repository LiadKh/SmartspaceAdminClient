package com.integration.smartspace.Admin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.integration.smartspace.Environment.Environment;
import com.integration.smartspace.Login.LoginActivity;
import com.integration.smartspace.R;
import com.securepreferences.SecurePreferences;

/**
 * Created by liadkh on 5/23/19.
 */

public class AdminMenuFragment extends Fragment {

    private String mSmartspace, mMail, mBaseUrl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_menu, container, false);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            mSmartspace = bundle.getString(AdminActivity.SMARTSPACE_KEY);
            mMail = bundle.getString(AdminActivity.MAIL_KEY);
            mBaseUrl = bundle.getString(Environment.BASE_URL);
        }
        view.findViewById(R.id.import_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                importData();
            }
        });

        view.findViewById(R.id.export_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exportData();
            }
        });

        view.findViewById(R.id.logout_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        return view;
    }

    private void logout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialogTheme)
                .setTitle(R.string.logout).setMessage(R.string.confirm_logout)
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SharedPreferences prefs = new SecurePreferences(getContext());
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.clear();
                        editor.apply();
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                        getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        getActivity().finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void exportData() {

        // Create new fragment and transaction
        Fragment newFragment = new AdminDataFragment();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out, android.R.animator.fade_in, android.R.animator.fade_out);
        Bundle bundle = new Bundle();
        bundle.putSerializable(AdminDataFragment.TYPE, Environment.FuncTypeEnum.EXPORT_DATA);
        bundle.putString(AdminActivity.SMARTSPACE_KEY, mSmartspace);
        bundle.putString(AdminActivity.MAIL_KEY, mMail);
        bundle.putString(Environment.BASE_URL, mBaseUrl);
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
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out, android.R.animator.fade_in, android.R.animator.fade_out);
        Bundle bundle = new Bundle();
        bundle.putSerializable(AdminDataFragment.TYPE, Environment.FuncTypeEnum.IMPORT_DATA);
        bundle.putString(AdminActivity.SMARTSPACE_KEY, mSmartspace);
        bundle.putString(AdminActivity.MAIL_KEY, mMail);
        bundle.putString(Environment.BASE_URL, mBaseUrl);
        newFragment.setArguments(bundle);
        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack
        transaction.replace(R.id.tools_admin, newFragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }
}
