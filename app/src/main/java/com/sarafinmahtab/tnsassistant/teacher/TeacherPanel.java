package com.sarafinmahtab.tnsassistant.teacher;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.sarafinmahtab.tnsassistant.R;

public class TeacherPanel extends AppCompatActivity {

    String title;
    TextView course_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_panel);

        course_title = (TextView) findViewById(R.id.panel_course_title);

        Bundle bundle = getIntent().getExtras();

        title = bundle.getString("course_title");

        course_title.setText(title);
    }
}
