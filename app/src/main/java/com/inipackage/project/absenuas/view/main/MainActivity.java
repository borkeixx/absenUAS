package com.inipackage.project.absenuas.view.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.inipackage.project.absenuas.R;
import com.inipackage.project.absenuas.utils.SessionLogin;
import com.inipackage.project.absenuas.view.absen.AbsenActivity;
import com.inipackage.project.absenuas.view.history.HistoryActivity;

public class MainActivity extends AppCompatActivity {

    private String strTitle;
    private SessionLogin session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setInitLayout();
    }

    private void setInitLayout() {
        session = new SessionLogin(this);
        session.checkLogin();

        findViewById(R.id.cvAbsenMasuk).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strTitle = "Absen Masuk";
                Intent intent = new Intent(MainActivity.this, AbsenActivity.class);
                intent.putExtra(AbsenActivity.DATA_TITLE, strTitle);
                startActivity(intent);
            }
        });

        findViewById(R.id.cvAbsenKeluar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strTitle = "Absen Keluar";
                Intent intent = new Intent(MainActivity.this, AbsenActivity.class);
                intent.putExtra(AbsenActivity.DATA_TITLE, strTitle);
                startActivity(intent);
            }
        });

        findViewById(R.id.cvPerizinan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strTitle = "Izin";
                Intent intent = new Intent(MainActivity.this, AbsenActivity.class);
                intent.putExtra(AbsenActivity.DATA_TITLE, strTitle);
                startActivity(intent);
            }
        });

        findViewById(R.id.cvHistory).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
                startActivity(intent);
            }
        });

        ImageView imageLogout = findViewById(R.id.imageLogout);
        imageLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("Yakin Anda ingin Logout?");
                builder.setCancelable(true);
                builder.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        session.logoutUser();
                        finishAffinity();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }
}
