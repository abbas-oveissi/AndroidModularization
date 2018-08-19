package ir.oveissi.search.db;

import android.arch.persistence.room.Room;
import android.content.Context;

public class AppDatabaseSingleton {

    private static AppDatabase instance;

    public static AppDatabase getIntance(Context appContext) {
        if (instance == null) {
            instance = Room.databaseBuilder(appContext,
                    AppDatabase.class, "suggestiondb")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
