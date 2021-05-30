package com.example.mymentoapp.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.mymentoapp.model.SpecificCourse;
import com.example.mymentoapp.model.Student;
import com.example.mymentoapp.model.TaughtCourse;
import com.example.mymentoapp.util.MyRoomDatabase;

import java.util.List;

public class TaughtCourseRepository {
    private TaughtCourseDao taughtCourseDao;
    private StudentDao studentDao;
    private List<Student> allStudents;
    private List<TaughtCourse> allTaughtCourses;

    public TaughtCourseRepository(Application application){
        MyRoomDatabase db = MyRoomDatabase.getDatabase(application);
        taughtCourseDao = db.taughtCourseDao();
        allTaughtCourses =  taughtCourseDao.getAllTaughtCourses();

        studentDao = db.studentDao();
        allStudents = studentDao.getAllStudents();
    }
    public List<TaughtCourse> getAllData(){
        return allTaughtCourses;
    }

    public List<TaughtCourse> getAllTaughtCoursesForStudent(int idInput){
        return taughtCourseDao.getAllTaughtCoursesForStudent(idInput);
    }

    public void insert(TaughtCourse taughtCourse){
        MyRoomDatabase.databaseWriteExecutor.execute(()->{
            taughtCourseDao.insertTaughtCourse(taughtCourse);
        });
    }
    public void update(){
        MyRoomDatabase.databaseWriteExecutor.execute(()->{
            taughtCourseDao.updateTaughtCourses();
        });
    }
    public void deleteAll(){
        MyRoomDatabase.databaseWriteExecutor.execute(()->{
           taughtCourseDao.deleteAll();
        });
    }

    public void deleteSpecificCourse(int id){
        MyRoomDatabase.databaseWriteExecutor.execute(()->{
            taughtCourseDao.deleteTaughtCourses(id);
        });
    }
}