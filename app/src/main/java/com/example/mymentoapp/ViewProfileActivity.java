package com.example.mymentoapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mymentoapp.data.StudentDao;
import com.example.mymentoapp.model.SpecificCourse;
import com.example.mymentoapp.model.Student;
import com.example.mymentoapp.util.MyRoomDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ViewProfileActivity extends AppCompatActivity {

    TextView firstName, lastName, phoneNumber, email, studyYear, domain;
    Button editProfile;
    ListView list;
    TextView textView;
    private ArrayAdapter<String> adapter;
    MyRoomDatabase roomDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        Bundle bundle = getIntent().getExtras();
        int idStudent = bundle.getInt("idStudent");

        //System.out.println("id student view profile = "+ idStudent);
        //list = (ListView) findViewById(R.id.list_view);
        textView = (TextView) findViewById(R.id.text_view_course);
        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        phoneNumber = findViewById(R.id.phoneNumber);
        email = findViewById(R.id.email_view);
        studyYear = findViewById(R.id.studyYear);
        domain = findViewById(R.id.domain);
        editProfile = findViewById(R.id.edit_btn);

        AtomicInteger studentId = new AtomicInteger();
        roomDatabase = MyRoomDatabase.getDatabase(getApplicationContext());
        StudentDao studentDao = roomDatabase.studentDao();

        //Bundle extras = getIntent().getExtras();
        ArrayList<SpecificCourse> extractedCourses = (ArrayList<SpecificCourse>) bundle.getSerializable("lista_cursuri");

        System.out.println("extras" +bundle.get("lista_cursuri").toString());
        //ArrayAdapter<String> courseList = new ArrayAdapter<>(this, R.layout.activity_view_profile, R.id.text_view_list, extractedCourses);

        new Thread(() -> {
            Student student = studentDao.getStudent(idStudent);
            studentId.set(student.getIdStudent());
            this.runOnUiThread(() -> {
//                for(String s : extractedCourses){
//                    textView.append(s);
//                    textView.append("\n");
//                }
                //textView.setText(extras.get("lista_cursuri").toString());
                for(SpecificCourse course : extractedCourses){
                    textView.append(course.getCourseName());
                    textView.append("\n");
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
            newIntent.putExtra("studentId", idStudent);
            startActivity(newIntent);
        });
    }
}