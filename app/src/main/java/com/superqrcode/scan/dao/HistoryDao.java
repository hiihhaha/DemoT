package com.superqrcode.scan.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.superqrcode.scan.model.History;

import java.util.List;


@Dao
public interface HistoryDao {

    @Query("SELECT * FROM history_table ORDER BY id DESC")
    List<History> getList();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void add(History history);

    @Query("DELETE FROM history_table WHERE id =:id")
    void delete(int id);

    @Query("DELETE FROM history_table ")
    void deleteAll();
}
