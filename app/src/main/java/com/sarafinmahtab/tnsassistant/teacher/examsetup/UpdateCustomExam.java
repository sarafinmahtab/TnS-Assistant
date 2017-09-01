package com.sarafinmahtab.tnsassistant.teacher.examsetup;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sarafinmahtab.tnsassistant.MySingleton;
import com.sarafinmahtab.tnsassistant.R;

import java.util.HashMap;
import java.util.Map;

public class UpdateCustomExam extends AppCompatActivity {

    EditText ExamName, ExamPercentage;
    Button update, cancel;

    String courseID, courseCode, url, name, percent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_custom_exam);

        Bundle bundle = getIntent().getExtras();
        url = bundle.getString("url");
        name = bundle.getString("name");
        percent = bundle.getString("percent");
        courseID = bundle.getString("course_id");
        courseCode = bundle.getString("course_code");

        ExamName = (EditText) findViewById(R.id.edit_custom_name);
        ExamPercentage = (EditText) findViewById(R.id.edit_custom_percent);

        ExamName.setText(name);
        ExamPercentage.setText(percent);

        update = (Button) findViewById(R.id.update_btn);
        cancel = (Button) findViewById(R.id.cancel_btn);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String newName = ExamName.getText().toString();
                final String newPercent = ExamPercentage.getText().toString();

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        switch (response) {
                            case "success":
                                Toast.makeText(UpdateCustomExam.this, "Updated SuccessFully", Toast.LENGTH_LONG).show();

                                UpdateCustomExam.super.onBackPressed();

                                break;
                            case "failed":
                                Toast.makeText(UpdateCustomExam.this, "Update Failed", Toast.LENGTH_LONG).show();
                                break;
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(UpdateCustomExam.this, error.getMessage(), Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();

                        params.put("course_id", courseID);
                        params.put("custom_name", newName);
                        params.put("custom_percent", newPercent);

                        return params;
                    }
                };

                MySingleton.getMyInstance(UpdateCustomExam.this).addToRequestQueue(stringRequest);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateCustomExam.super.onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
