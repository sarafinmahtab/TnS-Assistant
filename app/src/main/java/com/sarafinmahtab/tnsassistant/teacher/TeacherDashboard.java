package com.sarafinmahtab.tnsassistant.teacher;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.sarafinmahtab.tnsassistant.R;
import com.sarafinmahtab.tnsassistant.teacher.marksheet.MarkSheetActivity;
import com.sarafinmahtab.tnsassistant.teacher.studentlist.StudentList;

public class TeacherDashboard extends AppCompatActivity {

    String courseCode, courseID, teacherID;

    ImageButton students, sendMail, shareFiles, markSheet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_dashboard);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.activity_teacher_dashboard__toolbar);
        setSupportActionBar(myToolbar);

        myToolbar.setTitleTextColor(0xFFFFFFFF);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        Bundle bundle = getIntent().getExtras();

        courseCode = bundle.getString("course_code");
        courseID = bundle.getString("course_id");
        teacherID = bundle.getString("teacher_id");
        getSupportActionBar().setTitle("Dashboard: " + courseCode);

        students = (ImageButton) findViewById(R.id.student_list);
        sendMail = (ImageButton) findViewById(R.id.teacher_inbox);
        shareFiles = (ImageButton) findViewById(R.id.share_files);
        markSheet = (ImageButton) findViewById(R.id.result_sheet);

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
                Toast.makeText(TeacherDashboard.this, teacherID, Toast.LENGTH_LONG).show();
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
//                stdListBundle.putInt("tab_id", 0);
                intent.putExtras(stdListBundle);

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
