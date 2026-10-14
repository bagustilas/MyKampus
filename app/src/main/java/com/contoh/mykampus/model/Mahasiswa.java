package com.contoh.mykampus.model;
import androidx.room.Entity;
import androidx.room.Index;
import java.util.Objects;

@Entity(tableName = "mahasiswa", indices = {@Index(value = {"nim"}, unique = true)})
public class Mahasiswa extends Person{
    private String nim;
    private String jurusan;
    private double ipk;
    public  Mahasiswa(){

    }

    public String getNim() {
        return nim;
    }

    public void setNim(String nim) {
        if(nim == null || !nim.matches("\\d{10}")) {
            throw new IllegalArgumentException("NIM harus tepat 10 digit angka");
        }
        this.nim = nim;
    }

    public String getJurusan(){
        return jurusan;
    }

    public void setJurusan(String jurusan){
        this.jurusan = jurusan;
    }

    public double getIpk(){
        return ipk;

    }

    public void setIpk(double ipk){
        if(ipk < 0.0 || ipk > 4.0){
            throw new IllegalArgumentException("IPK harus 0.0 = 4.0");
        }
        this.ipk =ipk;
    }

    @Override
    public String getRole(){
        return "Mahasiswa";
    }
    @Override
    public String getStatusAkademik() {
        if (ipk > 3.5) return "Cumlaude";
        if (ipk > 3.0) return "Memuaskan";
        if (ipk < 2.0) return "Peringatan";
        return "Cukup";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Mahasiswa)) return false;
        Mahasiswa m = (Mahasiswa) o;
        return getId() == m.getId()
                && Double.compare(ipk, m.ipk) == 0
                && Objects.equals(getNama(), m.getNama())
                && Objects.equals(getEmail(), m.getEmail())
                && Objects.equals(nim, m.nim)
                && Objects.equals(jurusan, m.jurusan);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getNama(), getEmail(), nim, jurusan, ipk);
    }
}
