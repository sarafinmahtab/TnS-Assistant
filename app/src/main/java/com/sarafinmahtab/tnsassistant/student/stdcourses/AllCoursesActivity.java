package com.sarafinmahtab.tnsassistant.student.stdcourses;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sarafinmahtab.tnsassistant.MySingleton;
import com.sarafinmahtab.tnsassistant.R;
import com.sarafinmahtab.tnsassistant.teacher.courselist.Course;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AllCoursesActivity extends AppCompatActivity {

    String all_courses_list_url = "http://192.168.0.63/TnSAssistant/all_courses_list.php";
    String studentID;

    RecyclerView allCourseRecyclerView;
    RecyclerView.Adapter allCourseAdapter;
    List<CourseItem> allCoursesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_courses);

        Toolbar stdListToolbar = (Toolbar) findViewById(R.id.all_courses_toolbar);
        setSupportActionBar(stdListToolbar);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        stdListToolbar.setTitleTextColor(0xFFFFFFFF);

        allCourseRecyclerView = (RecyclerView) findViewById(R.id.all_courses_list_view);
        allCourseRecyclerView.setHasFixedSize(true);
        allCourseRecyclerView.setLayoutManager(new LinearLayoutManager(AllCoursesActivity.this));

        allCoursesList = new ArrayList<>();

        Bundle bundle = getIntent().getExtras();
        studentID = bundle.getString("student_id");

        getSupportActionBar().setTitle("Your Running Courses");

        StringRequest stringRequestForAllCoursesList = new StringRequest(Request.Method.POST, all_courses_list_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("all_courses_list");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);

                        CourseItem courseItem = new CourseItem(
                                obj.getString("course_id"),
                                obj.getString("course_code"),
                                obj.getString("course_title"),
                                obj.getString("credit"),
                                obj.getString("semester")
                        );

                        allCoursesList.add(courseItem);
                    }

                    allCourseAdapter = new AllCoursesAdapter(allCoursesList, AllCoursesActivity.this);
                    allCourseRecyclerView.setAdapter(allCourseAdapter);

                } catch (JSONException e) {
                    Toast.makeText(AllCoursesActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AllCoursesActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("student_id", studentID);

                return params;
            }
        };

        MySingleton.getMyInstance(AllCoursesActivity.this).addToRequestQueue(stringRequestForAllCoursesList);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
