package com.contoh.mykampus.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.contoh.mykampus.data.repository.AkademikRepository;
import com.contoh.mykampus.model.Dosen;

import java.util.List;

public class DosenViewModel extends ViewModel {
    private final AkademikRepository<Dosen> repository;

    private final MutableLiveData<String> error = new MutableLiveData<>();
    private final MutableLiveData<Boolean> saved = new MutableLiveData<>();

    public DosenViewModel(AkademikRepository<Dosen> repository) {
        this.repository = repository;
    }

    public LiveData<List<Dosen>> getAll() {
        return repository.getAll();
    }

    public LiveData<String> getError() {
        return error;
    }

    public LiveData<Boolean> getSaved() {
        return saved;
    }

    public void simpan(int id, long createdAt, String nama, String email,
                       String nidn, String mataKuliah) {
        try {
            Dosen d = new Dosen();
            d.setId(id);
            d.setNama(nama);
            d.setEmail(email);
            d.setNidn(nidn);
            d.setMataKuliahDiampu(mataKuliah);

            long now = System.currentTimeMillis();
            d.setCreatedAt(createdAt == 0 ? now : createdAt);
            d.setUpdatedAt(now);

            AkademikRepository.ResultCallback cb = new AkademikRepository.ResultCallback() {
                @Override
                public void onSuccess() {
                    saved.setValue(true);
                }

                @Override
                public void onError(String message) {
                    error.setValue(message);
                }
            };

            if (id == 0) {
                repository.insert(d, cb);
            } else {
                repository.update(d, cb);
            }
        } catch (IllegalArgumentException e) {
            error.setValue(e.getMessage());
        }
    }

    public void hapus(Dosen d) {
        repository.delete(d);
    }

    public void clearError() {
        error.setValue(null);
    }

    public void clearSaved() {
        saved.setValue(null);
    }
}
