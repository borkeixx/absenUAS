package com.inipackage.project.absenuas.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.inipackage.project.absenuas.model.ModelDatabase;

import java.util.List;

@Dao
public interface DatabaseDao {
    @Query("SELECT * FROM tbl_absensi")
    LiveData<List<ModelDatabase>> getAllHistory();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertData(ModelDatabase... modelDatabases);

    @Query("DELETE FROM tbl_absensi WHERE uid= :uid")
    void deleteHistoryById(int uid);

    @Query("DELETE FROM tbl_absensi")
    void deleteAllHistory();
}

