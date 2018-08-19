package ir.oveissi.search.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Suggestion {
    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "count")
    private Integer count;

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}