package com.sarafinmahtab.tnsassistant.teacher;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.sarafinmahtab.tnsassistant.R;

public class TeacherActivity extends AppCompatActivity {
    TextView code_name, designation, dept_code, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);

        code_name = (TextView) findViewById(R.id.teacher_code);
        designation = (TextView) findViewById(R.id.designation);
        dept_code = (TextView) findViewById(R.id.dept_code);
        email = (TextView) findViewById(R.id.email_of_teacher);

        Bundle bundle = getIntent().getExtras();
        code_name.setText(bundle.getString("employee_code"));
        designation.setText(bundle.getString("desig_name"));
        dept_code.setText(bundle.getString("dept_code"));
        email.setText(bundle.getString("email"));
    }
}
