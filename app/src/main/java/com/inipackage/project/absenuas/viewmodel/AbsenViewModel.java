package com.inipackage.project.absenuas.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import com.inipackage.project.absenuas.database.DatabaseClient;
import com.inipackage.project.absenuas.database.dao.DatabaseDao;
import com.inipackage.project.absenuas.model.ModelDatabase;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AbsenViewModel extends AndroidViewModel {
    private DatabaseDao databaseDao;

    public AbsenViewModel(Application application) {
        super(application);
        databaseDao = DatabaseClient.getInstance(application).getAppDatabase().databaseDao();
    }

    public void addDataAbsen(String foto, String nama, String tanggal, String lokasi, String keterangan, String s) {
        Completable.fromAction(() -> {
                    ModelDatabase modelDatabase = new ModelDatabase();
                    modelDatabase.fotoSelfie = foto;
                    modelDatabase.nama = nama;
                    modelDatabase.tanggal = tanggal;
                    modelDatabase.lokasi = lokasi;
                    modelDatabase.keterangan = keterangan;
                    databaseDao.insertData(modelDatabase);
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }
}
