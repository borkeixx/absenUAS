package com.inipackage.project.absenuas.view.login;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.inipackage.project.absenuas.R;
import com.inipackage.project.absenuas.utils.SessionLogin;
import com.inipackage.project.absenuas.view.main.MainActivity;

public class LoginActivity extends AppCompatActivity {
    private SessionLogin session;
    private String strNama;
    private String strPassword;
    private int REQ_PERMISSION = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setPermission();
        setInitLayout();
    }

    private void setPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQ_PERMISSION);
        }
    }

    private void setInitLayout() {
        session = new SessionLogin(getApplicationContext());

        if (session.isLoggedIn()) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }

        findViewById(R.id.btnLogin).setOnClickListener(view -> {
            strNama = ((EditText) findViewById(R.id.inputNama)).getText().toString();
            strPassword = ((EditText) findViewById(R.id.inputPassword)).getText().toString();

            if (strNama.isEmpty() || strPassword.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Form tidak boleh kosong!",
                        Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                session.createLoginSession(strNama);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_GRANTED) {
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
