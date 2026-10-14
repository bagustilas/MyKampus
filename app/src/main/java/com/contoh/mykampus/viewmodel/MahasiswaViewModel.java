package com.contoh.mykampus.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.contoh.mykampus.data.repository.AkademikRepository;
import com.contoh.mykampus.model.Mahasiswa;

import java.util.List;

public class MahasiswaViewModel extends ViewModel {
    private final AkademikRepository<Mahasiswa> repository;

    private final MutableLiveData<String> error = new MutableLiveData<>();
    private final MutableLiveData<Boolean> saved = new MutableLiveData<>();

    public MahasiswaViewModel(AkademikRepository<Mahasiswa> repository) {
        this.repository = repository;
    }

    public LiveData<List<Mahasiswa>> getAll() {
        return repository.getAll();
    }

    public LiveData<String> getError() {
        return error;
    }

    public LiveData<Boolean> getSaved() {
        return saved;
    }

    // id = 0 -> data baru, id > 0 -> edit data lama
    // createdAt = 0 untuk data baru; saat edit, kirim createdAt data lama
    public void simpan(int id, long createdAt, String nama, String email,
                       String nim, String jurusan, String ipkText) {
        try {
            Mahasiswa m = new Mahasiswa();
            m.setId(id);
            m.setNama(nama);
            m.setEmail(email);
            m.setNim(nim);
            m.setJurusan(jurusan);
            m.setIpk(parseIpk(ipkText));

            long now = System.currentTimeMillis();
            m.setCreatedAt(createdAt == 0 ? now : createdAt);
            m.setUpdatedAt(now);

            if (id == 0) {
                repository.insert(m, callback());
            } else {
                repository.update(m, callback());
            }
        } catch (IllegalArgumentException e) {
            error.setValue(e.getMessage());
        }
    }

    public void hapus(Mahasiswa m) {
        repository.delete(m);
    }

    public void clearError() {
        error.setValue(null);
    }

    public void clearSaved() {
        saved.setValue(null);
    }

    private double parseIpk(String text) {
        try {
            return Double.parseDouble(text.trim().replace(',', '.'));
        } catch (Exception e) {
            throw new IllegalArgumentException("IPK harus berupa angka");
        }
    }

    private AkademikRepository.ResultCallback callback() {
        return new AkademikRepository.ResultCallback() {
            @Override
            public void onSuccess() {
                saved.setValue(true);
            }

            @Override
            public void onError(String message) {
                error.setValue(message);
            }
        };
    }
}
