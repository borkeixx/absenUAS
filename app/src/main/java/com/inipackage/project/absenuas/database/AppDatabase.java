package com.inipackage.project.absenuas.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import com.inipackage.project.absenuas.database.dao.DatabaseDao;
import com.inipackage.project.absenuas.model.ModelDatabase;

@Database(entities = {ModelDatabase.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract DatabaseDao databaseDao();
}

