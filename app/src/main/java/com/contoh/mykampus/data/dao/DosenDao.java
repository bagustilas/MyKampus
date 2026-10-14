package com.contoh.mykampus.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.contoh.mykampus.model.Dosen;

import java.util.List;

@Dao
public interface DosenDao {

    @Query("SELECT * FROM dosen ORDER BY nama")
    LiveData<List<Dosen>> getAll();

    @Insert
    void insert(Dosen dosen);

    @Update
    void update(Dosen dosen);

    @Delete
    void delete(Dosen dosen);
}
