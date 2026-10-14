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
import com.contoh.mykampus.data.repository.MahasiswaRepository;
import com.contoh.mykampus.model.Mahasiswa;
import com.contoh.mykampus.model.Person;
import com.contoh.mykampus.viewmodel.MahasiswaVMFactory;
import com.contoh.mykampus.viewmodel.MahasiswaViewModel;

import java.util.ArrayList;

public class MahasiswaFragment extends Fragment {

    private static final String KEY_EDIT_ID = "edit_id";
    private static final String KEY_EDIT_CREATED = "edit_created";

    private MahasiswaViewModel vm;

    private EditText etNama, etEmail, etNim, etJurusan, etIpk;
    private TextView tvError;
    private Button btnSimpan, btnBatal;

    private int editId = 0;
    private long editCreatedAt = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mahasiswa, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etNama = view.findViewById(R.id.etNama);
        etEmail = view.findViewById(R.id.etEmail);
        etNim = view.findViewById(R.id.etNim);
        etJurusan = view.findViewById(R.id.etJurusan);
        etIpk = view.findViewById(R.id.etIpk);
        tvError = view.findViewById(R.id.tvError);
        btnSimpan = view.findViewById(R.id.btnSimpan);
        btnBatal = view.findViewById(R.id.btnBatal);
        RecyclerView rv = view.findViewById(R.id.rvMahasiswa);

        if (savedInstanceState != null) {
            editId = savedInstanceState.getInt(KEY_EDIT_ID, 0);
            editCreatedAt = savedInstanceState.getLong(KEY_EDIT_CREATED, 0);
        }
        updateMode();

        // ViewModel lewat interface repository
        MyKampusDatabase db = MyKampusDatabase.getInstance(requireContext());
        AkademikRepository<Mahasiswa> repo = new MahasiswaRepository(db.MahasiswaDao());
        vm = new ViewModelProvider(this, new MahasiswaVMFactory(repo))
                .get(MahasiswaViewModel.class);

        // RecyclerView + adapter polymorphic
        PersonAdapter adapter = new PersonAdapter(new PersonAdapter.Listener() {
            @Override
            public void onClick(Person person) {
                isiFormUntukEdit((Mahasiswa) person);
            }

            @Override
            public void onLongClick(Person person) {
                konfirmasiHapus((Mahasiswa) person);
            }
        });
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));
        rv.setAdapter(adapter);

        // Observasi data, UI update otomatis
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
                    etNim.getText().toString(),
                    etJurusan.getText().toString(),
                    etIpk.getText().toString()
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

    private void isiFormUntukEdit(Mahasiswa m) {
        editId = m.getId();
        editCreatedAt = m.getCreatedAt();
        etNama.setText(m.getNama());
        etEmail.setText(m.getEmail());
        etNim.setText(m.getNim());
        etJurusan.setText(m.getJurusan());
        etIpk.setText(String.valueOf(m.getIpk()));
        updateMode();
    }

    private void konfirmasiHapus(Mahasiswa m) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Hapus data")
                .setMessage("Hapus " + m.getNama() + "?")
                .setPositiveButton("Hapus", (d, w) -> vm.hapus(m))
                .setNegativeButton("Batal", null)
                .show();
    }

    private void kosongkanForm() {
        etNama.setText("");
        etEmail.setText("");
        etNim.setText("");
        etJurusan.setText("");
        etIpk.setText("");
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