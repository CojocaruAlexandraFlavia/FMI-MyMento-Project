package com.example.mymentoapp.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.mymentoapp.model.CourseToTeach;
import com.example.mymentoapp.model.Notification;
import com.example.mymentoapp.model.Tutor;
import java.util.List;

@Dao
public interface TutorDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertTutor(Tutor tutor);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertToTeachCourses(List<CourseToTeach> courseToTeaches);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertNotifications(List<Notification> notifications);

    @Query("DELETE FROM tutor_table")
    void deleteAll();

    @Query("SELECT * FROM tutor_table WHERE username =:userName")
    Tutor getTutorByUserName(String userName);

    @Query("SELECT * FROM tutor_table WHERE lastName=:lastName and firstName=:firstName")
    Tutor getTutorByName(String lastName, String firstName);

    @Query("SELECT * FROM tutor_table")
    List<Tutor> getAllTutors();

    @Query("SELECT * FROM tutor_table WHERE idStudent=:studentIdInput")
    Tutor getTutor(int studentIdInput);

    @Update
    void updateTutor(Tutor tutor);
}