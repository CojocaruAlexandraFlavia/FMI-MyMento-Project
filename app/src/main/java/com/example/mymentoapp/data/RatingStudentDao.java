package com.example.mymentoapp.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.mymentoapp.model.RatingStudent;

import java.util.List;

@Dao
public interface RatingStudentDao {

    @Query("select studentUsername from students_rating_tutors where tutorUsername=:tutorUsername")
    List<String> getStudentsWithRatingForTutor(String tutorUsername);

    @Query("DELETE FROM students_rating_tutors")
    void deleteAll();

    @Insert
    void insertStudentForTutor(RatingStudent ratingStudent);
}
