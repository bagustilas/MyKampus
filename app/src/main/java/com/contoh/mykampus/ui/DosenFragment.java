package com.contoh.mykampus.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.contoh.mykampus.R;
import com.contoh.mykampus.data.db.MyKampusDatabase;
import com.contoh.mykampus.data.repository.AkademikRepository;
import com.contoh.mykampus.data.repository.DosenRepository;
import com.contoh.mykampus.model.Dosen;
import com.contoh.mykampus.model.Person;
import com.contoh.mykampus.viewmodel.DosenVMFactory;
import com.contoh.mykampus.viewmodel.DosenViewModel;

import java.util.ArrayList;

public class DosenFragment extends Fragment {

    private static final String KEY_EDIT_ID = "edit_id";
    private static final String KEY_EDIT_CREATED = "edit_created";

    private DosenViewModel vm;

    private EditText etNama, etEmail, etNidn, etMataKuliah;
    private TextView tvError;
    private Button btnSimpan, btnBatal;

    private int editId = 0;
    private long editCreatedAt = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dosen, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etNama = view.findViewById(R.id.etNama);
        etEmail = view.findViewById(R.id.etEmail);
        etNidn = view.findViewById(R.id.etNidn);
        etMataKuliah = view.findViewById(R.id.etMataKuliah);
        tvError = view.findViewById(R.id.tvError);
        btnSimpan = view.findViewById(R.id.btnSimpan);
        btnBatal = view.findViewById(R.id.btnBatal);
        RecyclerView rv = view.findViewById(R.id.rvDosen);

        if (savedInstanceState != null) {
            editId = savedInstanceState.getInt(KEY_EDIT_ID, 0);
            editCreatedAt = savedInstanceState.getLong(KEY_EDIT_CREATED, 0);
        }
        updateMode();

        MyKampusDatabase db = MyKampusDatabase.getInstance(requireContext());
        AkademikRepository<Dosen> repo = new DosenRepository(db.DosenDao());
        vm = new ViewModelProvider(this, new DosenVMFactory(repo))
                .get(DosenViewModel.class);

        PersonAdapter adapter = new PersonAdapter(new PersonAdapter.Listener() {
            @Override
            public void onClick(Person person) {
                isiFormUntukEdit((Dosen) person);
            }

            @Override
            public void onLongClick(Person person) {
                konfirmasiHapus((Dosen) person);
            }
        });
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));
        rv.setAdapter(adapter);

        vm.getAll().observe(getViewLifecycleOwner(),
                list -> adapter.submitList(new ArrayList<Person>(list)));

        vm.getError().observe(getViewLifecycleOwner(), message -> {
            if (message == null) {
                tvError.setVisibility(View.GONE);
            } else {
                tvError.setText(message);
                tvError.setVisibility(View.VISIBLE);
            }
        });

        vm.getSaved().observe(getViewLifecycleOwner(), saved -> {
            if (Boolean.TRUE.equals(saved)) {
                kosongkanForm();
                vm.clearSaved();
                vm.clearError();
            }
        });

        btnSimpan.setOnClickListener(v -> {
            vm.clearError();
            vm.simpan(
                    editId,
                    editCreatedAt,
                    etNama.getText().toString(),
                    etEmail.getText().toString(),
                    etNidn.getText().toString(),
                    etMataKuliah.getText().toString()
            );
        });

        btnBatal.setOnClickListener(v -> {
            kosongkanForm();
            vm.clearError();
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_EDIT_ID, editId);
        outState.putLong(KEY_EDIT_CREATED, editCreatedAt);
    }

    private void isiFormUntukEdit(Dosen d) {
        editId = d.getId();
        editCreatedAt = d.getCreatedAt();
        etNama.setText(d.getNama());
        etEmail.setText(d.getEmail());
        etNidn.setText(d.getNidn());
        etMataKuliah.setText(d.getMataKuliahDiampu());
        updateMode();
    }

    private void konfirmasiHapus(Dosen d) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Hapus data")
                .setMessage("Hapus " + d.getNama() + "?")
                .setPositiveButton("Hapus", (dlg, w) -> vm.hapus(d))
                .setNegativeButton("Batal", null)
                .show();
    }

    private void kosongkanForm() {
        etNama.setText("");
        etEmail.setText("");
        etNidn.setText("");
        etMataKuliah.setText("");
        editId = 0;
        editCreatedAt = 0;
        updateMode();
    }

    private void updateMode() {
        boolean editing = editId != 0;
        btnBatal.setVisibility(editing ? View.VISIBLE : View.GONE);
        btnSimpan.setText(editing ? "Update" : "Simpan");
    }
}