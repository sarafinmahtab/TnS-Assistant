package com.sarafinmahtab.tnsassistant.teacher.studentlist;

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
import com.sarafinmahtab.tnsassistant.teacher.TeacherActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentList extends AppCompatActivity {

    String std_list_url = "http://192.168.0.63/TnSAssistant/std_list.php";
    String courseID, teacherID, stdName;

    List<StudentItem> stdListItem;
    StudentListAdapter stdAdapter;
    RecyclerView stdRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);

        Toolbar stdListToolbar = (Toolbar) findViewById(R.id.stdlist_toolbar);
        setSupportActionBar(stdListToolbar);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        stdListToolbar.setTitleTextColor(0xFFFFFFFF);

        stdRecyclerView = (RecyclerView) findViewById(R.id.std_list_view);
        stdRecyclerView.setHasFixedSize(true);
        stdRecyclerView.setLayoutManager(new LinearLayoutManager(StudentList.this));

        stdListItem = new ArrayList<>();

        Bundle bundle = getIntent().getExtras();

        courseID = bundle.getString("course_id");
        teacherID = bundle.getString("teacher_id");

        getSupportActionBar().setTitle(bundle.getString("course_code"));

        StringRequest stringRequestForStdList = new StringRequest(Request.Method.POST, std_list_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("std_list");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);

                        stdName = obj.getString("s_first_name") + " " + obj.getString("s_last_name");

                        StudentItem studentItem = new StudentItem(
                                obj.getString("student_id"),
                                obj.getString("std_display_picture"), stdName,
                                obj.getString("registration_no"),
                                obj.getString("contact_no"));
                        stdListItem.add(studentItem);
                    }

                    stdAdapter = new StudentListAdapter(stdListItem, StudentList.this);
                    stdRecyclerView.setAdapter(stdAdapter);
                } catch (JSONException e) {
                    Toast.makeText(StudentList.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(StudentList.this, error.getMessage(), Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("course_id", courseID);

                return params;
            }
        };

        MySingleton.getMyInstance(StudentList.this).addToRequestQueue(stringRequestForStdList);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
