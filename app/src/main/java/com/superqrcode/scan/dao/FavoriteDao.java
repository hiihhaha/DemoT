package com.superqrcode.scan.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.superqrcode.scan.model.Favourite;
import com.superqrcode.scan.model.History;

import java.util.List;


@Dao
public interface FavoriteDao {

    @Query("SELECT * FROM favourite_table ORDER BY id DESC")
    List<Favourite> getList();


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void add(Favourite favourite);

    @Query("SELECT * FROM history_table WHERE id =:idHistory")
    History getHistory(int idHistory);

    @Query("DELETE FROM favourite_table WHERE historyId =:historyId")
    void delete(int historyId);

    @Query("DELETE FROM favourite_table ")
    void deleteAll();
}
