package com.example.mymentoapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymentoapp.data.CourseToTeachDao;
import com.example.mymentoapp.data.SpecificCourseDao;
import com.example.mymentoapp.data.StudentDao;
import com.example.mymentoapp.data.TutorDao;
import com.example.mymentoapp.model.CourseToTeach;
import com.example.mymentoapp.model.CourseToTeachViewModel;
import com.example.mymentoapp.model.SpecificCourse;
import com.example.mymentoapp.model.SpecificCourseViewModel;
import com.example.mymentoapp.model.Student;
import com.example.mymentoapp.model.StudentViewModel;
import com.example.mymentoapp.model.TaughtCourseViewModel;
import com.example.mymentoapp.model.Tutor;
import com.example.mymentoapp.model.TutorViewModel;
import com.example.mymentoapp.util.MyRoomDatabase;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class ViewProfileActivity extends AppCompatActivity {
    private StudentViewModel studentViewModel;
    private CourseToTeachViewModel courseToTeachViewModel;
    private TutorViewModel tutorViewModel;
    private SpecificCourseViewModel specificCourseViewModel;
    private TaughtCourseViewModel taughtCourseViewModel;


    TextView firstName, lastName, phoneNumber, email, studyYear, domain;
    Button editProfile;

    TextView textViewSpecificCourse;
    TextView textViewToTeachCourse;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        Bundle bundle = getIntent().getExtras();
        String studentName = bundle.getString("studentName");

        //System.out.println("id student view profile = "+ idStudent);
        //list = (ListView) findViewById(R.id.list_view_courses);
        textViewSpecificCourse = (TextView) findViewById(R.id.text_view_course);
        textViewToTeachCourse = (TextView) findViewById(R.id.teach_courses);
        textView = (TextView) findViewById(R.id.text_view_course2);
        //recycler = findViewById(R.id.recycle_course_view);
        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        phoneNumber = findViewById(R.id.phoneNumber);
        email = findViewById(R.id.email_view);
        studyYear = findViewById(R.id.studyYear);
        domain = findViewById(R.id.domain);
        editProfile = findViewById(R.id.edit_btn);

        textViewToTeachCourse.setVisibility(View.VISIBLE);

        new Thread(() -> {

            studentViewModel = new StudentViewModel(this.getApplication());
            tutorViewModel = new TutorViewModel(this.getApplication());
            courseToTeachViewModel = new CourseToTeachViewModel(this.getApplication());
            specificCourseViewModel = new SpecificCourseViewModel(this.getApplication());
            Student student = studentViewModel.getStudentByUsername(studentName);

            ArrayList<SpecificCourse> courses =(ArrayList<SpecificCourse>) (specificCourseViewModel.getAllSpecificCoursesForStudent(student.getIdStudent()));
            System.out.println("LISTA DE CURSURI ADUSE PE VIEW PROFILE");
            Tutor tutor = tutorViewModel.getTutor(student.getUsername());
            ArrayList<CourseToTeach> courseToTeachArrayList = new ArrayList<CourseToTeach>();
            if(tutor != null){
                courseToTeachArrayList = (ArrayList<CourseToTeach>) courseToTeachViewModel.getAllToTeachCourses(tutor.getIdStudent());
            }
//            ArrayList<String> specificCourseNames = new ArrayList<>();
//            for(SpecificCourse specificCourse : courses){
//                specificCourseNames.add(specificCourse.getCourseName());
//            }
//
//            ArrayList<String> toTeachCourseNames = new ArrayList<>();
//            for(CourseToTeach courseToTeach: courseToTeachArrayList){
//                toTeachCourseNames.add(courseToTeach.getCourseName());
//            }


//            System.out.println("id student este " + student.getIdStudent());

//            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
//                    this,
//                    android.R.layout.simple_list_item_1, courseNames);
//
//            list.setAdapter(arrayAdapter);


            ArrayList<CourseToTeach> finalCourseToTeachArrayList = courseToTeachArrayList;
            this.runOnUiThread(() -> {


                if(tutor != null){
                    System.out.println("diferit de null");

                    textView.setVisibility(View.VISIBLE);
                    for(CourseToTeach courseToTeach : finalCourseToTeachArrayList){
                        textViewToTeachCourse.append(courseToTeach.getCourseName());
                        textViewToTeachCourse.append("\n");
                    }
                }
                for(SpecificCourse course : courses){
                    textViewSpecificCourse.append(course.getCourseName());
                    textViewSpecificCourse.append("\n");
                }


                firstName.setText(student.getFirstName());
                lastName.setText(student.getLastName());
                phoneNumber.setText(student.getPhoneNumber());
                email.setText(student.getEmail());
                studyYear.setText(student.getStudyYear());
                domain.setText(student.getStudyDomain());
            });
        }).start();


        editProfile.setOnClickListener(v -> {
            Intent newIntent = new Intent (ViewProfileActivity.this, EditProfileActivity.class);
            newIntent.putExtra("studentName", studentName);
            startActivity(newIntent);
        });
    }
}
