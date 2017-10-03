package com.sarafinmahtab.tnsassistant.teacher;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.sarafinmahtab.tnsassistant.R;
import com.sarafinmahtab.tnsassistant.teacher.examsetup.ExamSetupActivity;
import com.sarafinmahtab.tnsassistant.teacher.marksheet.MarkSheetActivity;
import com.sarafinmahtab.tnsassistant.teacher.studentlist.StudentList;

public class TeacherDashboard extends AppCompatActivity {

    String courseCode, courseID, teacherID;
    ImageButton students, sendMail, shareFiles, markSheet, examSetup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_dashboard);

        Toolbar myToolbar = findViewById(R.id.activity_teacher_dashboard__toolbar);
        setSupportActionBar(myToolbar);

        myToolbar.setTitleTextColor(0xFFFFFFFF);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        Bundle bundle = getIntent().getExtras();

        //Bundle Datas
        courseCode = bundle.getString("course_code");
        courseID = bundle.getString("course_id");
        teacherID = bundle.getString("teacher_id");
        getSupportActionBar().setTitle("Dashboard: " + courseCode);

        //Grid ImageButtons
        students = findViewById(R.id.student_list);
        sendMail = findViewById(R.id.teacher_inbox);
        shareFiles = findViewById(R.id.share_files);
        markSheet = findViewById(R.id.result_sheet);
        examSetup = findViewById(R.id.exam_setup);

        students.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TeacherDashboard.this, StudentList.class);

                Bundle stdListBundle = new Bundle();

                stdListBundle.putString("course_id", courseID);
                stdListBundle.putString("teacher_id", teacherID);
                stdListBundle.putString("course_code", courseCode);

                intent.putExtras(stdListBundle);

                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        sendMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(TeacherDashboard.this);
                builder.setMessage("Developer is building own mailbox for TnS soon!! Till then, use default mailbox.")
                .setPositiveButton("Go To Mail", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

                AlertDialog alert = builder.create();
                alert.setTitle("Mail Box is default!!");
                alert.show();
            }
        });

        shareFiles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TeacherDashboard.this, courseID, Toast.LENGTH_LONG).show();
            }
        });

        markSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TeacherDashboard.this, MarkSheetActivity.class);

                Bundle stdListBundle = new Bundle();

                stdListBundle.putString("course_id", courseID);
                stdListBundle.putString("teacher_id", teacherID);
                stdListBundle.putString("course_code", courseCode);
                intent.putExtras(stdListBundle);

                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        examSetup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TeacherDashboard.this, ExamSetupActivity.class);

                Bundle examSetupBundle = new Bundle();

                examSetupBundle.putString("course_id", courseID);
                examSetupBundle.putString("teacher_id", teacherID);
                examSetupBundle.putString("course_code", courseCode);
                intent.putExtras(examSetupBundle);

                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
