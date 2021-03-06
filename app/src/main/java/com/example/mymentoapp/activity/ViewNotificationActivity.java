package com.example.mymentoapp.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymentoapp.R;
import com.example.mymentoapp.model.CourseToTeach;
import com.example.mymentoapp.model.CourseToTeachViewModel;
import com.example.mymentoapp.model.Notification;
import com.example.mymentoapp.model.NotificationViewModel;
import com.example.mymentoapp.model.SpecificCourseViewModel;
import com.example.mymentoapp.model.Student;
import com.example.mymentoapp.model.StudentNotification;
import com.example.mymentoapp.model.StudentNotificationViewModel;
import com.example.mymentoapp.model.StudentViewModel;
import com.example.mymentoapp.model.StudentWithNotifications;
import com.example.mymentoapp.model.StudentWithTaughtCourses;
import com.example.mymentoapp.model.TaughtCourse;
import com.example.mymentoapp.model.Tutor;
import com.example.mymentoapp.model.TutorViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ViewNotificationActivity extends AppCompatActivity {

    private StudentViewModel studentViewModel;
    private TutorViewModel tutorViewModel;
    private NotificationViewModel notificationViewModel;
    private StudentNotificationViewModel studentNotificationViewModel;
    private CourseToTeachViewModel courseToTeachViewModel;
    private List<Notification> notificationArrayList = new ArrayList<>();
    private List<StudentNotification> studentNotifications  = new ArrayList<>();
    private Student student;
    private Tutor tutor;

    LinearLayout linearLayout, linearLayout1;
    TextView textView;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @SuppressLint({"RtlHardcoded", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_notifications);

        linearLayout = findViewById(R.id.layout_notifications);

        Bundle bundle = getIntent().getExtras();
        String studentName = bundle.getString("studentName");

        new Thread(() -> {

            studentViewModel = new StudentViewModel(this.getApplication());
            tutorViewModel = new TutorViewModel(this.getApplication());
            notificationViewModel = new NotificationViewModel(this.getApplication());
            studentNotificationViewModel = new StudentNotificationViewModel(this.getApplication());
            courseToTeachViewModel = new CourseToTeachViewModel(this.getApplication());

            student = studentViewModel.getStudentByUsername(studentName);
            tutor = tutorViewModel.getTutor(studentName);

            if(tutor != null){

                studentNotifications = studentNotificationViewModel.getAllNotificationsForStudent(tutor.getUsername());
                Collections.sort(studentNotifications, (a, b) -> a.getStatus().equals("New") && b.getStatus().equals("Old") ? -1
                        : a.getStatus().equals("Old") && b.getStatus().equals("New") ? 1
                        :0);

                notificationArrayList = notificationViewModel.getAllNotificationsForTutor(tutor.getIdStudent());

                if(studentNotifications.size() == 0 && notificationArrayList.size() == 0){
                    this.runOnUiThread(() -> {
                        textView = new TextView(this.getApplicationContext());
                        textView.setPadding(10, 10, 5, 5);
                        textView.setTextColor(Color.rgb(255, 255, 255));
                        textView.setGravity(Gravity.CENTER);
                        textView.setTextSize(18);
                        textView.setText("YOU HAVE 0 NOTIFICATIONS");
                        textView.setBackgroundColor(Color.rgb(25, 55, 106));
                        linearLayout.addView(textView);

                    });
                }else{
                    for(Notification n : notificationArrayList){
                        CourseToTeach c = courseToTeachViewModel.getCourseById((int) n.getId_FkCourseToTeach());
                        Student student1 = studentViewModel.getStudent((int) n.getId_FkStudent());
                        String text = n.getDescription()  + student1.getFirstName() + " " + student1.getLastName() + " for "+
                                c.getCourseName() + " course \n" ;
                        Button btn = new Button(this.getApplicationContext());

                        btn.setText("ACCEPT REQUEST");
                        btn.setTextColor(Color.rgb(0,0,0));
                        btn.setTextSize(18);

                        this.runOnUiThread(() ->{
                            textView = new TextView(this.getApplicationContext());
                            textView.setText(text);
                            textView.setPadding(10, 10, 5, 5);
                            textView.setTextColor(Color.rgb(255, 255, 255));
                            textView.setGravity(Gravity.CENTER);
                            textView.setTextSize(18);

                            linearLayout1 = new LinearLayout(this.getApplicationContext());

                            linearLayout1.setOrientation(LinearLayout.VERTICAL);
                            linearLayout1.setBackgroundColor(Color.rgb(25, 55, 106));
                            TextView textView1 =  new TextView(this.getApplicationContext());
                            textView1.setBackgroundColor(Color.rgb(196, 201, 208));

                            if(btn.getParent() !=null){
                                ((ViewGroup)btn.getParent()).removeView(btn);
                            }

                            TextView textView3 = new TextView(this.getApplicationContext());
                            textView3.setBackgroundColor(Color.rgb(1,24,71));

                            linearLayout1.addView(textView3);
                            linearLayout1.addView(textView);
                            linearLayout1.addView(btn);
                            linearLayout1.addView(textView1);
                            linearLayout.addView(linearLayout1);

                            btn.setOnClickListener(v -> new Thread(() ->{

                                CourseToTeach t = courseToTeachViewModel.getCourseById((int)n.getId_FkCourseToTeach());
                                TaughtCourse t2 = new TaughtCourse(t.getCourseName(), t.getDescription());
                                t2.setId_FkTutor(tutor.getIdStudent());
                                t2.setIdCourseToTeach((int) n.getId_FkCourseToTeach());
                                List<TaughtCourse> taughtCourseList = new ArrayList<>();
                                taughtCourseList.add(t2);
                                StudentWithTaughtCourses studentWithTaughtCourses = new StudentWithTaughtCourses(student1, taughtCourseList);
                                studentViewModel.insertStudentWithTaughtCourses(studentWithTaughtCourses);

                                StudentNotification studentNotification = new StudentNotification();
                                studentNotification.setDescription("Tutor " + tutor.getFirstName() + " " + tutor.getLastName() + " accepted your request " +
                                        "for " + t2.getCourseName() + " course");
                                studentNotification.setId_FkCourseToTeach(t.getIdCourseToTeach());
                                studentNotification.setId_FkTutor(tutor.getIdStudent());
                                studentNotification.setStatus("New");

                                List<StudentNotification> studentNotificationsList =  new ArrayList<>();
                                studentNotificationsList.add(studentNotification);

                                StudentWithNotifications studentWithNotifications = new StudentWithNotifications(student1, studentNotificationsList);
                                studentViewModel.insertStudentWithNotifications(studentWithNotifications);
                                notificationViewModel.deleteNotification(n.getIdNotification());

                                finish();
                                startActivity(getIntent());

                            }).start());
                        });

                    }

                    this.runOnUiThread(() ->{
                        for(StudentNotification sn : studentNotifications){
                            TextView textView1 = new TextView(this.getApplicationContext());
                            textView1.setText(sn.getDescription());
                            textView1.setBackgroundColor(Color.rgb(25, 55, 106));
                            textView1.setPadding(30, 30, 30, 30);
                            textView1.setTextColor(Color.rgb(255,255,255));
                            textView1.setTextSize(18);

                            if(sn.getStatus().equals("New")){
                                textView1.setTypeface(textView1.getTypeface(), Typeface.BOLD);
                            }

                            textView1.setOnClickListener(u ->{
                                sn.setStatus("Old");
                                studentNotificationViewModel.updateNotification(sn);
                                textView1.setTypeface(textView1.getTypeface(), Typeface.NORMAL);
                                finish();
                                startActivity(getIntent());
                            });

                            TextView textView2 = new TextView(this.getApplicationContext());
                            textView2.setBackgroundColor(Color.rgb(196,201,208));
                            textView2.setPadding(25, 5, 25, 5);

                            TextView textView3 = new TextView(this.getApplicationContext());
                            textView3.setBackgroundColor(Color.rgb(1,24,71));
                            textView3.setPadding(25, 1, 25, 0);

                            linearLayout.addView(textView3);
                            linearLayout.addView(textView1);
                            linearLayout.addView(textView2);
                        }
                    });
                }
                }

            else{

                studentNotifications = studentNotificationViewModel.getAllNotificationsForStudent(student.getUsername());

                if(studentNotifications.size() == 0 ) {
                    this.runOnUiThread(() -> {
                        textView = new TextView(this.getApplicationContext());
                        textView.setPadding(10, 10, 5, 5);
                        textView.setTextColor(Color.rgb(255, 255, 255));
                        textView.setGravity(Gravity.CENTER);
                        textView.setTextSize(18);
                        textView.setText("YOU HAVE 0 NOTIFICATIONS");
                        textView.setBackgroundColor(Color.rgb(25, 55, 106));
                        linearLayout.addView(textView);

                    });
                }
                else{
                    Collections.sort(studentNotifications, (a, b) -> a.getStatus().equals("New") && b.getStatus().equals("Old") ? -1
                            : a.getStatus().equals("Old") && b.getStatus().equals("New") ? 1
                            :0);
                    this.runOnUiThread(() ->{

                        for(StudentNotification sn : studentNotifications){
                            TextView textView1 = new TextView(this.getApplicationContext());
                            textView1.setText(sn.getDescription());
                            textView1.setBackgroundColor(Color.rgb(25, 55, 106));
                            textView1.setPadding(30, 30, 30, 30);
                            textView1.setTextColor(Color.rgb(255,255,255));
                            textView1.setTextSize(18);

                            if(sn.getStatus().equals("New")){
                                textView1.setTypeface(textView1.getTypeface(), Typeface.BOLD);
                            }

                            textView1.setOnClickListener(u ->{
                                sn.setStatus("Old");
                                studentNotificationViewModel.updateNotification(sn);
                                textView1.setTypeface(textView1.getTypeface(), Typeface.NORMAL);
//                                textView1.setClickable(false);
                                finish();
                                startActivity(getIntent());
                            });

                            TextView textView2 = new TextView(this.getApplicationContext());
                            textView2.setBackgroundColor(Color.rgb(196,201,208));
                            textView2.setPadding(25, 5, 25, 5);

                            TextView textView3 = new TextView(this.getApplicationContext());
                            textView3.setBackgroundColor(Color.rgb(1,24,71));
                            textView3.setPadding(25, 1, 25, 0);

                            linearLayout.addView(textView3);
                            linearLayout.addView(textView1);
                            linearLayout.addView(textView2);
                        }


                    });
                }

            }

        }).start();



    }
}
