package com.inipackage.project.absenuas.model;

import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import java.io.Serializable;

@Entity(tableName = "tbl_absensi")
public class ModelDatabase implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "uid")
    public int uid;

    @ColumnInfo(name = "nama")
    public String nama;

    @ColumnInfo(name = "foto_selfie")
    public String fotoSelfie;

    @ColumnInfo(name = "tanggal")
    public String tanggal;

    @ColumnInfo(name = "lokasi")
    public String lokasi;

    @ColumnInfo(name = "keterangan")
    public String keterangan;
}

