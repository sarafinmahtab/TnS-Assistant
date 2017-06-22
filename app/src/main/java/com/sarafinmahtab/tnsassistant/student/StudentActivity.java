package com.sarafinmahtab.tnsassistant.student;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.sarafinmahtab.tnsassistant.R;

public class StudentActivity extends AppCompatActivity {

    TextView std_name, std_email, reg_id, dept_name;
    Bundle bundle;

    String name, std_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        bundle = getIntent().getExtras();

        Toolbar myToolbar = (Toolbar) findViewById(R.id.activity_student_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle(bundle.getString("s_first_name") + " Activity");

        myToolbar.setTitleTextColor(0xFFFFFFFF);

        std_name = (TextView) findViewById(R.id.student_name);
        std_email = (TextView) findViewById(R.id.email_of_student);
        reg_id = (TextView) findViewById(R.id.reg_std);
        dept_name = (TextView) findViewById(R.id.dept_std);

        std_id = bundle.getString("student_id");
        name = bundle.getString("s_first_name") + " " + bundle.getString("s_last_name");

        std_name.setText(name);
        std_email.setText(bundle.getString("email_no"));
        reg_id.setText(bundle.getString("registration_no"));
        dept_name.setText(bundle.getString("dept_name"));
    }
}
