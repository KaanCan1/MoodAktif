package com.kaancankurt.moodaktif_1;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface UserDao {

    @Insert
    long insertUser(User user);

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    User getUserByEmail(String email);

    @Query("SELECT * FROM users WHERE id = :userId LIMIT 1")
    User getUserById(int userId);

    @Update
    void updateUser(User user);

    @Query("SELECT COUNT(*) FROM users WHERE email = :email")
    int isEmailExists(String email);
}