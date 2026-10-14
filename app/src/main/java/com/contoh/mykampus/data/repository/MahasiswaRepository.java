package com.contoh.mykampus.data.repository;

import android.database.sqlite.SQLiteConstraintException;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;

import com.contoh.mykampus.data.dao.MahasiswaDao;
import com.contoh.mykampus.data.db.MyKampusDatabase;
import com.contoh.mykampus.model.Mahasiswa;

import java.util.List;

public class MahasiswaRepository implements AkademikRepository<Mahasiswa>{
    private final MahasiswaDao dao;
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public MahasiswaRepository(MahasiswaDao dao) {
        this.dao = dao;
    }

    @Override
    public LiveData<List<Mahasiswa>> getAll() {
        return dao.getAll();
    }

    @Override
    public void insert(Mahasiswa item, ResultCallback callback) {
        MyKampusDatabase.executor.execute(() -> {
            try {
                dao.insert(item);
                mainHandler.post(callback::onSuccess);
            } catch (SQLiteConstraintException e) {
                mainHandler.post(() -> callback.onError("NIM sudah terdaftar"));
            }
        });
    }

    @Override
    public void update(Mahasiswa item, ResultCallback callback) {
        MyKampusDatabase.executor.execute(() -> {
            try {
                dao.update(item);
                mainHandler.post(callback::onSuccess);
            } catch (SQLiteConstraintException e) {
                mainHandler.post(() -> callback.onError("NIM sudah terdaftar"));
            }
        });
    }

    @Override
    public void delete(Mahasiswa item) {
        MyKampusDatabase.executor.execute(() -> dao.delete(item));
    }
}
