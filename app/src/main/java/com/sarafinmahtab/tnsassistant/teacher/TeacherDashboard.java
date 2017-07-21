package com.sarafinmahtab.tnsassistant.teacher;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.sarafinmahtab.tnsassistant.R;

public class TeacherDashboard extends AppCompatActivity {

    String title;

    TextView course_title;
    ImageButton students, send_mail, share_files, mark_sheet;

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

        title = bundle.getString("course_title");
        getSupportActionBar().setTitle(title);

        students = (ImageButton) findViewById(R.id.students);
        send_mail = (ImageButton) findViewById(R.id.send_mail);
        share_files = (ImageButton) findViewById(R.id.sharing);
        mark_sheet = (ImageButton) findViewById(R.id.mark_sheet);

        students.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TeacherDashboard.this, "Polapainz", Toast.LENGTH_LONG).show();
            }
        });

        send_mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TeacherDashboard.this, "Mail Pathao", Toast.LENGTH_LONG).show();
            }
        });

        share_files.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TeacherDashboard.this, "share mar", Toast.LENGTH_LONG).show();
            }
        });

        mark_sheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TeacherDashboard.this, "Number de", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

//    Method 2:
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == android.R.id.home) {
//            finish();
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}
