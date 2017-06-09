package com.sarafinmahtab.tnsassistant.teacher;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.sarafinmahtab.tnsassistant.R;
import com.sarafinmahtab.tnsassistant.RecyclerAdapter;

import java.util.ArrayList;

public class TeacherActivity extends AppCompatActivity {
    TextView name, code_name, designation, dept_code, email;
    Button course_loader;
    String full_name, teacher_id;

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;

    ArrayList<Course> arrayList = new ArrayList<>();

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

        teacher_id = bundle.getString("teacher_id");
        full_name = bundle.getString("t_first_name") + " " + bundle.getString("t_last_name");

        name.setText(full_name);
        code_name.setText("Code Name: " + bundle.getString("employee_code"));
        designation.setText(bundle.getString("desig_name"));
        dept_code.setText(bundle.getString("dept_code"));
        email.setText(bundle.getString("email"));

        course_loader = (Button) findViewById(R.id.course_loader);
        course_loader.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        recyclerView = (RecyclerView) findViewById(R.id.cousres_list);
                        layoutManager = new LinearLayoutManager(TeacherActivity.this);
                        recyclerView.setLayoutManager(layoutManager);

                        CourseListBackgroundTask courseListBackgroundTask = new CourseListBackgroundTask(TeacherActivity.this, teacher_id);
                        arrayList = courseListBackgroundTask.getCourseList();
                        adapter = new RecyclerAdapter(arrayList);
                        recyclerView.setAdapter(adapter);
                    }
                }
        );
    }
}
