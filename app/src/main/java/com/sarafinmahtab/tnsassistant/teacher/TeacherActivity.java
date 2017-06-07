package com.sarafinmahtab.tnsassistant.teacher;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.sarafinmahtab.tnsassistant.R;

public class TeacherActivity extends AppCompatActivity {
    TextView name, code_name, designation, dept_code, email;
    String full_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);

        name = (TextView) findViewById(R.id.name);
        code_name = (TextView) findViewById(R.id.teacher_code);
        designation = (TextView) findViewById(R.id.designation);
        dept_code = (TextView) findViewById(R.id.dept_code);
        email = (TextView) findViewById(R.id.email_of_teacher);

        Bundle bundle = getIntent().getExtras();

        full_name = bundle.getString("t_first_name") + " " + bundle.getString("t_last_name");

        name.setText(full_name);
        code_name.setText("Code Name: " + bundle.getString("employee_code"));
        designation.setText(bundle.getString("desig_name"));
        dept_code.setText(bundle.getString("dept_code"));
        email.setText(bundle.getString("email"));
    }
}
