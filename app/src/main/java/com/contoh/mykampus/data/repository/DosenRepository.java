package com.contoh.mykampus.data.repository;

import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;

import com.contoh.mykampus.data.dao.DosenDao;
import com.contoh.mykampus.data.db.MyKampusDatabase;
import com.contoh.mykampus.model.Dosen;

import java.util.List;

public class DosenRepository implements AkademikRepository<Dosen>{
    private final DosenDao dao;
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public DosenRepository(DosenDao dao) {
        this.dao = dao;
    }

    @Override
    public LiveData<List<Dosen>> getAll() {
        return dao.getAll();
    }

    @Override
    public void insert(Dosen item, ResultCallback callback) {
        MyKampusDatabase.executor.execute(() -> {
            dao.insert(item);
            mainHandler.post(callback::onSuccess);
        });
    }

    @Override
    public void update(Dosen item, ResultCallback callback) {
        MyKampusDatabase.executor.execute(() -> {
            dao.update(item);
            mainHandler.post(callback::onSuccess);
        });
    }

    @Override
    public void delete(Dosen item) {
        MyKampusDatabase.executor.execute(() -> dao.delete(item));
    }
}
