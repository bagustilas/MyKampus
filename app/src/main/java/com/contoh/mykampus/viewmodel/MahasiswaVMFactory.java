package com.contoh.mykampus.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.contoh.mykampus.data.repository.AkademikRepository;
import com.contoh.mykampus.model.Mahasiswa;

public class MahasiswaVMFactory implements ViewModelProvider.Factory {

    private final AkademikRepository<Mahasiswa> repository;

    public MahasiswaVMFactory(AkademikRepository<Mahasiswa> repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MahasiswaViewModel(repository);
    }
}