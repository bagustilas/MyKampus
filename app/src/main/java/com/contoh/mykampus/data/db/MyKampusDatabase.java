package com.contoh.mykampus.data.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.contoh.mykampus.data.dao.DosenDao;
import com.contoh.mykampus.data.dao.MahasiswaDao;
import com.contoh.mykampus.model.Dosen;
import com.contoh.mykampus.model.Mahasiswa;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Mahasiswa.class, Dosen.class}, version = 1, exportSchema = false)
public abstract class MyKampusDatabase extends RoomDatabase {

    public abstract MahasiswaDao MahasiswaDao();

    public abstract DosenDao DosenDao();

    private static volatile MyKampusDatabase INSTANCE;

    public static final ExecutorService executor = Executors.newFixedThreadPool(2);

    public static MyKampusDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (MyKampusDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), MyKampusDatabase.class, "mykampus.db").build();
                }
            }
        }
        return INSTANCE;
    }
}