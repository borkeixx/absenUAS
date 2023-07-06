package com.inipackage.project.absenuas.database;

import android.content.Context;
import androidx.room.Room;

public class DatabaseClient {

    private AppDatabase appDatabase;

    private DatabaseClient(Context context) {
        appDatabase = Room.databaseBuilder(context, AppDatabase.class, "absensi_db")
                .fallbackToDestructiveMigration()
                .build();
    }

    private static DatabaseClient mInstance;

    public static synchronized DatabaseClient getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DatabaseClient(context);
        }
        return mInstance;
    }

    public AppDatabase getAppDatabase() {
        return appDatabase;
    }
}
