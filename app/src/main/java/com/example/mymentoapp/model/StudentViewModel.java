package com.example.mymentoapp.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.mymentoapp.data.StudentRepository;

import java.util.List;

public class StudentViewModel extends AndroidViewModel {
    public static StudentRepository repository;
    public final LiveData<List<Student>> allStudents;

    public StudentViewModel(@NonNull Application application) {
        super(application);
        repository =  new StudentRepository(application);
        allStudents = repository.getAllData();

    }
    private StudentRepository studentRepository;

    public void insertStudentWithCourses(StudentWithCourse studentWithCourse){
        studentRepository.insertStudentWithCourses(studentWithCourse);
    }

    public LiveData<List<Student>> getAllStudents(){
        return allStudents;
    }
    public static void insert(Student student){
        repository.insertStudent(student);
    }
}