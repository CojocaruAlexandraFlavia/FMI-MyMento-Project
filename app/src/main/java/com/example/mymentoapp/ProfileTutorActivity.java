package com.example.mymentoapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mymentoapp.data.StudentDao;

import com.example.mymentoapp.data.TutorDao;

import com.example.mymentoapp.model.AssignCourse;
import com.example.mymentoapp.model.CourseToTeach;
import com.example.mymentoapp.model.RegisterViewModel;

import com.example.mymentoapp.model.SpecificCourse;
import com.example.mymentoapp.model.Student;
import com.example.mymentoapp.model.StudentViewModel;

import com.example.mymentoapp.model.StudentWithCourse;
import com.example.mymentoapp.model.Tutor;
import com.example.mymentoapp.model.TutorViewModel;
import com.example.mymentoapp.model.TutorWithCourse;
import com.example.mymentoapp.util.MyRoomDatabase;

import java.util.ArrayList;


public class ProfileTutorActivity extends AppCompatActivity {
    EditText lastName, firstName, phoneNumber, email, iban;
    RadioGroup radioGroupStudyYear, radioGroupDomain, radioGroupSpec;
    Button btn_submit_tutor;
    MyRoomDatabase roomDatabase;
    String studyYear1, domain1, specialization1;
    AssignCourse assignCourse;
    LinearLayout linearLayout;
    AssignCourse assignCourse2;
    CheckBox checkBox;
    TextView course_to_teach;

    private ArrayList<String> courseNameList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tutor_profile);

        lastName = (EditText) findViewById(R.id.last_name_edit);
        firstName = (EditText) findViewById(R.id.first_name_edit);
        phoneNumber = (EditText) findViewById(R.id.phone_edit);
        email = (EditText) findViewById(R.id.email_edit);
        iban = findViewById(R.id.iban_id);
        linearLayout = findViewById(R.id.to_teach_course);
        course_to_teach = findViewById(R.id.courses_to_teach);

        radioGroupSpec = findViewById(R.id.radio_group_spec_info);
        radioGroupStudyYear = (RadioGroup) findViewById(R.id.radio_year_edit);
        radioGroupDomain = (RadioGroup) findViewById(R.id.radio_domain_edit);
        btn_submit_tutor = (Button) findViewById(R.id.btn_edit);
        courseNameList = new ArrayList<String>();
        specialization1 = "" ;
        studyYear1 = "";
        domain1 = "";

        radioGroupStudyYear.setOnCheckedChangeListener((RadioGroup.OnCheckedChangeListener) (group, checkedId) -> {
            RadioButton rb2 = (RadioButton)findViewById(checkedId);
            studyYear1 = rb2.getText().toString();
            linearLayout.removeAllViews();
        });


        radioGroupDomain.setOnCheckedChangeListener((RadioGroup.OnCheckedChangeListener) (group, checkedId) -> {

            linearLayout.removeAllViews();
            course_to_teach.setVisibility(View.VISIBLE);
            // checkedId is the RadioButton selected
            RadioButton rb = (RadioButton)findViewById(checkedId);
            domain1 = rb.getText().toString();
            System.out.println("study+year" +rb.getText());

            radioGroupStudyYear.setOnCheckedChangeListener((RadioGroup.OnCheckedChangeListener) (group1, checkedId2) -> {

                linearLayout.removeAllViews();
                RadioButton rb2 = (RadioButton)findViewById(checkedId2);
                studyYear1 = rb2.getText().toString();
                if(domain1.equals("Mathematics") && (studyYear1.equals("II") || studyYear1.equals("III"))){
                    radioGroupSpec.setVisibility(View.VISIBLE);
                    radioGroupSpec.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        public void onCheckedChanged(RadioGroup group1, int checkedId3) {
                            RadioButton rb3 = (RadioButton)findViewById(checkedId3);
                            specialization1 = rb3.getText().toString();
                            System.out.println(studyYear1 + domain1);
                            assignCourse2 = new AssignCourse(studyYear1, domain1, specialization1);
                        }
                    });
                    assignCourse2 = new AssignCourse(studyYear1, domain1, specialization1);
                }
                System.out.println(studyYear1 + domain1);
                assignCourse2 = new AssignCourse(studyYear1, domain1, specialization1);
                linearLayout.removeAllViews();
                for(SpecificCourse specificCourse: assignCourse2.getSpecificCourseList()) {
                    CheckBox checkBox = new CheckBox(ProfileTutorActivity.this);
                    checkBox.setText(specificCourse.getCourseName());
                    checkBox.setVisibility(View.VISIBLE);
                    linearLayout.addView(checkBox);
                }

            });
            if(domain1.equals("Mathematics") && (studyYear1.equals("II") || studyYear1.equals("III"))){
                linearLayout.removeAllViews();
                radioGroupSpec.setVisibility(View.VISIBLE);
                radioGroupSpec.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    public void onCheckedChanged(RadioGroup group, int checkedId3) {
                        RadioButton rb3 = (RadioButton)findViewById(checkedId3);
                        specialization1 = rb3.getText().toString();
                        assignCourse2 = new AssignCourse(studyYear1, domain1, specialization1);
                        linearLayout.removeAllViews();
                        for(SpecificCourse specificCourse: assignCourse2.getSpecificCourseList()) {
                            CheckBox checkBox = new CheckBox(ProfileTutorActivity.this);
                            checkBox.setText(specificCourse.getCourseName());
                            checkBox.setVisibility(View.VISIBLE);
                            linearLayout.addView(checkBox);
                        }
                    }
                });
                assignCourse2 = new AssignCourse(studyYear1, domain1, specialization1);
            }


            if(radioGroupSpec.getVisibility() == View.VISIBLE){
                linearLayout.removeAllViews();
                radioGroupSpec.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    public void onCheckedChanged(RadioGroup group, int checkedId3) {
                        RadioButton rb3 = (RadioButton)findViewById(checkedId3);
                        specialization1 = rb3.getText().toString();
                        assignCourse2 = new AssignCourse(studyYear1, domain1, specialization1);
                        linearLayout.removeAllViews();
                        for(SpecificCourse specificCourse: assignCourse2.getSpecificCourseList()) {
                            CheckBox checkBox = new CheckBox(ProfileTutorActivity.this);
                            checkBox.setText(specificCourse.getCourseName());
                            checkBox.setVisibility(View.VISIBLE);
                            linearLayout.addView(checkBox);
                        }
                    }
                });
            }
            if(!(domain1.equals("Mathematics") && (studyYear1.equals("II") || studyYear1.equals("III")))){
                radioGroupSpec.setVisibility(View.GONE);
            }


            System.out.println(studyYear1 + domain1);
            assignCourse2 = new AssignCourse(studyYear1, domain1, specialization1);
            linearLayout.removeAllViews();
            for(SpecificCourse specificCourse: assignCourse2.getSpecificCourseList()) {
                CheckBox checkBox = new CheckBox(ProfileTutorActivity.this);
                checkBox.setText(specificCourse.getCourseName());
                checkBox.setVisibility(View.VISIBLE);
                linearLayout.addView(checkBox);

            }


        });

        if(radioGroupSpec.getVisibility() == View.VISIBLE){
            radioGroupSpec.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                public void onCheckedChanged(RadioGroup group, int checkedId3) {
                    RadioButton rb3 = (RadioButton)findViewById(checkedId3);
                    specialization1 = rb3.getText().toString();
                    System.out.println(studyYear1 + domain1);
                    assignCourse2 = new AssignCourse(studyYear1, domain1, specialization1);
                    linearLayout.removeAllViews();
                    for(SpecificCourse specificCourse: assignCourse2.getSpecificCourseList()) {
                        CheckBox checkBox = new CheckBox(ProfileTutorActivity.this);
                        checkBox.setText(specificCourse.getCourseName());
                        checkBox.setVisibility(View.VISIBLE);
                        linearLayout.addView(checkBox);
                    }
                }
            });
        }
//        linearLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
        btn_submit_tutor.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String firstname = firstName.getText().toString();
                String lastname = lastName.getText().toString();
                String phonenumber = phoneNumber.getText().toString();
                String email1 = email.getText().toString();
                String iban1 = iban.getText().toString();
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                String phonePattern = "^\\+[0-9]{10,13}$";

                if (firstname.equals("") || lastname.equals("") || phonenumber.equals("") || email1.equals("") || radioGroupDomain.getCheckedRadioButtonId() == -1 || radioGroupStudyYear.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(getApplicationContext(), "Fields Required", Toast.LENGTH_SHORT).show();
                } else {
                    if (!(email1.matches(emailPattern))) {
                        Toast.makeText(getApplicationContext(), "INVALID MAIL", Toast.LENGTH_SHORT).show();
                    }
                    if (!(phonenumber.matches(phonePattern))) {
                        Toast.makeText(getApplicationContext(), "INVALID PHONE NUMBER", Toast.LENGTH_SHORT).show();
                    }
//                    if (!(iban1.substring(0, 2).equals("RO")) || !(iban1.substring(2, iban1.length()).matches("[0-9]+")) || iban1.length() != 24) {//                        Toast.makeText(getApplicationContext(), "INVALID IBAN", Toast.LENGTH_SHORT).show();//                    }                    else                    {

                    Bundle bundle = getIntent().getExtras();
                    String username = bundle.getString("username");
                    System.out.println("username student: " + username);
                    roomDatabase = MyRoomDatabase.getDatabase(getApplicationContext());
                    StudentDao studentDao = roomDatabase.studentDao();

                    ArrayList<CourseToTeach> courseToTeachArrayList = new ArrayList<>();
                    ArrayList<SpecificCourse> specificCourses = (ArrayList<SpecificCourse>) assignCourse2.getSpecificCourseList();

                    for (int i = 0; i < specificCourses.size(); i++) {
                        CheckBox checkBox = (CheckBox) linearLayout.getChildAt(i);
                        if (checkBox.isChecked()) {
                            System.out.println("is checked" + i);
                            CourseToTeach courseToTeach = new CourseToTeach(specificCourses.get(i).getCourseName(), specificCourses.get(i).getDescription());
                            courseToTeachArrayList.add(courseToTeach);
                        }
                    }
                    if (courseToTeachArrayList.size() == 0) {
                        Toast.makeText(getApplicationContext(), "You have to choose at least one course", Toast.LENGTH_SHORT).show();
                    } else {
                        new Thread(() -> {
                            System.out.println("in thread");
                            Student student = studentDao.getStudentByUsername(username);
                            student.setStudyDomain(domain1);
                            student.setStudyYear(studyYear1);
                            student.setPhoneNumber(phonenumber);
                            student.setEmail(email1);
                            student.setLastName(lastname);
                            student.setFirstName(firstname);

                            System.out.println("specialization" + specialization1);
                            ;
                            System.out.println("domain" + domain1);
                            System.out.println("year" + studyYear1);
                            assignCourse = new AssignCourse(studyYear1, domain1, specialization1);
                            assignCourse.setCourseToTeachList(courseToTeachArrayList);


                            courseNameList.clear();
                            for (SpecificCourse specificCourse : assignCourse.getSpecificCourseList()) {
                                courseNameList.add(specificCourse.getCourseName());
                            }
                            System.out.println("CURSURILE");
                            for (SpecificCourse specificCourse : assignCourse.getSpecificCourseList()) {
                                System.out.println((specificCourse.getCourseName()));
                            }


                            studentDao.updateStudent(student);
                            Tutor tutor = new Tutor(firstname, lastname, studyYear1, domain1, phonenumber, email1, student.getUsername(), student.getPassword(), 0, iban1);
                            TutorViewModel.repository.insertTutor(tutor);

                            StudentWithCourse studentWithCourse = new StudentWithCourse(tutor, assignCourse.getSpecificCourseList());
                            StudentViewModel.insertStudentWithCourses(studentWithCourse);


                            TutorWithCourse tutorWithCourse = new TutorWithCourse(tutor, assignCourse.getCourseToTeachList());
                            TutorViewModel.insertTutorWithCourses(tutorWithCourse);

                            Intent intent = new Intent(ProfileTutorActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }).start();
                    }
                }
            }

        });
    }
}