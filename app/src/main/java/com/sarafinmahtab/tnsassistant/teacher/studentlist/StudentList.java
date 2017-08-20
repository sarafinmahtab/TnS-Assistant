package com.sarafinmahtab.tnsassistant.teacher.studentlist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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

public class StudentList extends AppCompatActivity {

//    String std_list_url = "http://192.168.0.63/TnSAssistant/std_list.php";
    String std_list_url = "http://192.168.43.65/TnSAssistant/std_list.php";

    String courseID, teacherID, stdName;

    List<StudentItem> stdListItem;
    StudentListAdapter stdAdapter;
    RecyclerView stdRecyclerView;

    SearchView stdSearchView;
    EditText searchEditText;
    ImageView closeButton;

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

        Bundle bundle = getIntent().getExtras();

        courseID = bundle.getString("course_id");
        teacherID = bundle.getString("teacher_id");

        getSupportActionBar().setTitle(bundle.getString("course_code"));

        stdSearchView = (SearchView) findViewById(R.id.std_searchView);
        searchEditText = (EditText) findViewById(R.id.search_src_text);
        closeButton = (ImageView) findViewById(R.id.search_close_btn);
        stdRecyclerView = (RecyclerView) findViewById(R.id.std_list_view);

        stdRecyclerView.setHasFixedSize(true);
        stdRecyclerView.setLayoutManager(new LinearLayoutManager(StudentList.this));

        stdListItem = new ArrayList<>();

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

        stdSearchView.onActionViewExpanded();
        stdSearchView.setIconified(false);
        stdSearchView.setQueryHint("Search by Name or Reg ID");

        if(!stdSearchView.isFocused()) {
            stdSearchView.clearFocus();
        }

//        stdSearchView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                searchView.setIconified(false);
//            }
//        });

        stdSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                stdAdapter.checkQueryFromList(newText.toLowerCase());
//                Toast.makeText(StudentList.this, newText, Toast.LENGTH_LONG).show();

                return true;
            }
        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Clear the text from EditText view
                searchEditText.setText("");

                //Clear query
                stdSearchView.setQuery("", false);
                stdAdapter.notifyDataSetChanged();
                stdSearchView.clearFocus();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
