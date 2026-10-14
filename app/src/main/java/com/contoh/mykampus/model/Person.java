package com.contoh.mykampus.model;

public abstract class Person extends BaseEntity implements EvaluasiAkademik {
    private String nama;
    private String email;

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        if(nama == null || nama.trim().length() < 3){
            throw new IllegalArgumentException("Nama minmal 3 Karakter");
        }
        this.nama = nama;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return null;
    }
}
