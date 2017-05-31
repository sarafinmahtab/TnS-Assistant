package com.sarafinmahtab.tnsassistant.teacher;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.sarafinmahtab.tnsassistant.R;

public class TeacherActivity extends AppCompatActivity {
    TextView greetings, email_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);

        greetings = (TextView) findViewById(R.id.greetings);
        email_view = (TextView) findViewById(R.id.email_view);

        Bundle bundle = getIntent().getExtras();
        greetings.setText("Welcome " + bundle.getString("name"));
        email_view.setText(bundle.getString("username"));
    }
}
