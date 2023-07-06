package com.inipackage.project.absenuas.view.history;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.inipackage.project.absenuas.R;
import com.inipackage.project.absenuas.model.ModelDatabase;
import com.inipackage.project.absenuas.viewmodel.HistoryViewModel;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity implements HistoryAdapter.HistoryAdapterCallback {
    private List<ModelDatabase> modelDatabaseList = new ArrayList<>();
    private HistoryAdapter historyAdapter;
    private HistoryViewModel historyViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        setInitLayout();
        setViewModel();
    }

    private void setInitLayout() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        TextView tvNotFound = findViewById(R.id.tvNotFound);
        tvNotFound.setVisibility(View.GONE);

        RecyclerView rvHistory = findViewById(R.id.rvHistory);
        historyAdapter = new HistoryAdapter(this, modelDatabaseList, this);
        rvHistory.setHasFixedSize(true);
        rvHistory.setLayoutManager(new LinearLayoutManager(this));
        rvHistory.setAdapter(historyAdapter);
    }

    private void setViewModel() {
        historyViewModel = new ViewModelProvider(this).get(HistoryViewModel.class);
        historyViewModel.getDataLaporan().observe(this, modelDatabases -> {
            if (modelDatabases.isEmpty()) {
                findViewById(R.id.tvNotFound).setVisibility(View.VISIBLE);
                findViewById(R.id.rvHistory).setVisibility(View.GONE);
            } else {
                findViewById(R.id.tvNotFound).setVisibility(View.GONE);
                findViewById(R.id.rvHistory).setVisibility(View.VISIBLE);
            }
            historyAdapter.setDataAdapter(modelDatabases);
        });
    }

    @Override
    public void onDelete(ModelDatabase modelDatabase) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Hapus riwayat ini?");
        alertDialogBuilder.setPositiveButton("Ya, Hapus", (dialogInterface, i) -> {
            int uid = modelDatabase.uid; // Menggunakan atribut uid langsung dari objek modelDatabase
            historyViewModel.deleteDataById(uid);
            Toast.makeText(this, "Yeay! Data yang dipilih sudah dihapus", Toast.LENGTH_SHORT).show();
        });
        alertDialogBuilder.setNegativeButton("Batal", (dialogInterface, i) -> dialogInterface.cancel());
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
