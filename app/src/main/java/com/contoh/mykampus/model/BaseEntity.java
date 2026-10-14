package com.contoh.mykampus.model;
import androidx.room.PrimaryKey;

public abstract class BaseEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private long createdAt;

    private long updatedAt;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }
}