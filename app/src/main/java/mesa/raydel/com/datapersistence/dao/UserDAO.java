package mesa.raydel.com.datapersistence.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import java.util.List;
import mesa.raydel.com.datapersistence.model.User;

@Dao
public interface UserDAO {

//    @Query("SELECT * FROM User")
//    List      getAll();

//    @Query("SELECT * FROM User where name LIKE :name")
//    User findByName(String name);
//
    @Query("SELECT * FROM User where id = :ID")
    User findByID(int ID);
//
//    @Query("SELECT COUNT(*) from User")
//    int count();
//
//    @Insert
//    void insertAll(User... users);
//
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(User user);

//    @Update
//    void update(User user);
//
//    @Delete
//    void delete(User user);
//
//    @Query("DELETE FROM User")
//    void deleteAll();

}
