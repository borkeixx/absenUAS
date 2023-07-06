package com.inipackage.project.absenuas.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.inipackage.project.absenuas.view.login.LoginActivity;

public class SessionLogin {
    private Context context;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private int PRIVATE_MODE = 0;

    public SessionLogin(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createLoginSession(String nama) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_NAMA, nama);
        editor.commit();
    }

    public void checkLogin() {
        if (!isLoggedIn()) {
            Intent intent = new Intent(context, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

    public void logoutUser() {
        editor.clear();
        editor.commit();
        Intent intent = new Intent(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }

    private static final String PREF_NAME = "AbsensiPref";
    private static final String IS_LOGIN = "IsLoggedIn";
    public static final String KEY_NAMA = "NAMA";
}
