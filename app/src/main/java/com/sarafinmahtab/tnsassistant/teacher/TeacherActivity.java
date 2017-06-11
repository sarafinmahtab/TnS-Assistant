package com.sarafinmahtab.tnsassistant.teacher;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sarafinmahtab.tnsassistant.MySingleton;
import com.sarafinmahtab.tnsassistant.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TeacherActivity extends AppCompatActivity {
    TextView name, code_name, designation, dept_code, email;
    Button course_loader;
    String full_name, teacher_id;

    List<Course> listItem;
    RecyclerView.Adapter adapter;
    RecyclerView recyclerView;

    String course_list_url = "http://192.168.0.150/TnSAssistant/generate_courses.php";

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
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(TeacherActivity.this));

                        listItem = new ArrayList<>();

                        loadCourseData();
                    }
                }
        );
    }

    private void loadCourseData() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Data!!");
        progressDialog.show();

        StringRequest stringRequestforJSONArray = new StringRequest(Request.Method.POST, course_list_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("course_list");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        Course courses = new Course(
                                obj.getString("course_id"),
                                "Code: " + obj.getString("course_code"),
                                obj.getString("course_title"),
                                "Credit: " + obj.getString("credit"),
                                "Session: " + obj.getString("session"));
                        listItem.add(courses);
                    }

                    adapter = new CourseListAdapter(listItem, TeacherActivity.this);
                    recyclerView.setAdapter(adapter);
                } catch (JSONException e) {
                    Toast.makeText(TeacherActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(TeacherActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("instructor_id", teacher_id);
                return params;
            }
        };

        MySingleton.getMyInstance(TeacherActivity.this).addToRequestQueue(stringRequestforJSONArray);
    }
}
