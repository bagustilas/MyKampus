package com.contoh.mykampus.data.repository;

import androidx.lifecycle.LiveData;

import com.contoh.mykampus.model.Person;

import java.util.List;

public interface AkademikRepository<T extends Person> {
    LiveData<List<T>> getAll();
    void insert(T item, ResultCallback callback);

    void update(T item, ResultCallback callback);

    void delete(T item);

    interface ResultCallback {
        void onSuccess();

        void onError(String message);
    }
}
