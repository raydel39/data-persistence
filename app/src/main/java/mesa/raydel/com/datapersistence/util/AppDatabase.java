package mesa.raydel.com.datapersistence.util;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.util.Log;

import java.io.File;

import mesa.raydel.com.datapersistence.dao.UserDAO;
import mesa.raydel.com.datapersistence.model.User;


@Database(entities = {User.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static final String TAG = "--AppDatabase";

    private static AppDatabase INSTANCE;
    private static final String DATABASE_NAME = "cop4656Labs.db";

    public abstract UserDAO userModel();

    public static AppDatabase getInstance(Context context){
        if (INSTANCE == null){
            final File dbPath = context.getDatabasePath(DATABASE_NAME);

            if (!dbPath.exists()) {// If the database file does not exist
                // Make sure we have a path to the file
                Log.d(TAG, "!dbPath.exists() copying");
                dbPath.getParentFile().mkdirs();
            } else {
                Log.d(TAG, "dbPath.exists()");
            }

            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, DATABASE_NAME)
                    .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}
