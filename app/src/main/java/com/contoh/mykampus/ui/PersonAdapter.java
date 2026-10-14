package com.contoh.mykampus.ui;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.contoh.mykampus.R;
import com.contoh.mykampus.model.Person;

public class PersonAdapter extends ListAdapter<Person, PersonAdapter.VH> {

    public interface Listener {
        void onClick(Person person);

        void onLongClick(Person person);
    }

    private final Listener listener;

    public PersonAdapter(Listener listener) {
        super(new DiffUtil.ItemCallback<Person>() {
            @Override
            public boolean areItemsTheSame(@NonNull Person a, @NonNull Person b) {
                return a.getId() == b.getId() && a.getRole().equals(b.getRole());
            }

            @SuppressLint("DiffUtilEquals")
            @Override
            public boolean areContentsTheSame(@NonNull Person a, @NonNull Person b) {
                return a.equals(b);
            }
        });
        this.listener = listener;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_person, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        Person p = getItem(position);
        holder.tvNama.setText(p.getNama());
        holder.tvRole.setText(p.getRole());
        holder.tvStatus.setText("Status: " + p.getStatusAkademik());
        holder.itemView.setOnClickListener(v -> listener.onClick(p));
        holder.itemView.setOnLongClickListener(v -> {
            listener.onLongClick(p);
            return true;
        });
    }

    static class VH extends RecyclerView.ViewHolder {
        final TextView tvNama;
        final TextView tvRole;
        final TextView tvStatus;

        VH(@NonNull View itemView) {
            super(itemView);
            tvNama = itemView.findViewById(R.id.tvNama);
            tvRole = itemView.findViewById(R.id.tvRole);
            tvStatus = itemView.findViewById(R.id.tvStatus);
        }
    }
}