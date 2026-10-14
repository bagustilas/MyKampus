package com.contoh.mykampus.model;
import androidx.room.Entity;

import java.util.Objects;
@Entity(tableName = "dosen")
public class Dosen extends Person {

    private String nidn;

    private String mataKuliahDiampu;

    public Dosen() {
    }

    public String getNidn() {
        return nidn;
    }

    public void setNidn(String nidn) {
        this.nidn = nidn;
    }

    public String getMataKuliahDiampu() {
        return mataKuliahDiampu;
    }

    public void setMataKuliahDiampu(String mataKuliahDiampu) {
        this.mataKuliahDiampu = mataKuliahDiampu;
    }

    @Override
    public String getRole() {
        return "Dosen";
    }

    @Override
    public String getStatusAkademik() {
        int jumlah = 0;
        if (mataKuliahDiampu != null && !mataKuliahDiampu.trim().isEmpty()) {
            jumlah = mataKuliahDiampu.split(",").length;
        }
        return jumlah > 3 ? "Beban Lebih" : "Normal";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Dosen)) return false;
        Dosen d = (Dosen) o;
        return getId() == d.getId()
                && Objects.equals(getNama(), d.getNama())
                && Objects.equals(getEmail(), d.getEmail())
                && Objects.equals(nidn, d.nidn)
                && Objects.equals(mataKuliahDiampu, d.mataKuliahDiampu);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getNama(), getEmail(), nidn, mataKuliahDiampu);
    }
}
