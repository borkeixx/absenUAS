package com.inipackage.project.absenuas.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.inipackage.project.absenuas.database.DatabaseClient;
import com.inipackage.project.absenuas.database.dao.DatabaseDao;
import com.inipackage.project.absenuas.model.ModelDatabase;
import java.util.List;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class HistoryViewModel extends AndroidViewModel {
    private LiveData<List<ModelDatabase>> dataLaporan;
    private DatabaseDao databaseDao;

    public HistoryViewModel(Application application) {
        super(application);
        databaseDao = DatabaseClient.getInstance(application).getAppDatabase().databaseDao();
        dataLaporan = databaseDao.getAllHistory();
    }

    public void deleteDataById(int uid) {
        Completable.fromAction(() -> databaseDao.deleteHistoryById(uid))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    public LiveData<List<ModelDatabase>> getDataLaporan() {
        return dataLaporan;
    }
}
