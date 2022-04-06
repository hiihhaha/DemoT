package com.superqrcode.scan.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;

import com.superqrcode.scan.dao.FavoriteDao;
import com.superqrcode.scan.dao.HistoryDao;
import com.superqrcode.scan.model.Favourite;
import com.superqrcode.scan.model.History;
import com.superqrcode.scan.model.QRCode;


@Database(entities = {History.class, Favourite.class}, version = 2, exportSchema = false)
public abstract class RoomDatabase extends androidx.room.RoomDatabase {
    public abstract HistoryDao historyDao();

    public abstract FavoriteDao favoriteDao();


    private static RoomDatabase instance;

    public static RoomDatabase getDatabase(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context, RoomDatabase.class, "qrcode_db").allowMainThreadQueries()
                    .fallbackToDestructiveMigration().build();
        }
        return instance;
    }
}
