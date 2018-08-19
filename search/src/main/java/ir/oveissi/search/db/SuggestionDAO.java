package ir.oveissi.search.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface SuggestionDAO {
    @Query("SELECT * FROM suggestion")
    Flowable<List<Suggestion>> getAll();

    @Query("SELECT * FROM suggestion WHERE title LIKE :title Order by count")
    Flowable<List<Suggestion>> getAllOrderByCount(String title);

    @Insert
    void insertAll(Suggestion... suggestions);

    @Delete
    void delete(Suggestion suggestion);

    @Update
    void update(Suggestion suggestion);
}