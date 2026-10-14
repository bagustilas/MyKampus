package com.contoh.mykampus.viewmodel;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.contoh.mykampus.data.repository.AkademikRepository;
import com.contoh.mykampus.model.Dosen;

import org.jspecify.annotations.NonNull;

public class DosenVMFactory implements ViewModelProvider.Factory {

    private final AkademikRepository<Dosen> repository;

    public DosenVMFactory(AkademikRepository<Dosen> repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new DosenViewModel(repository);
    }
}
