package com.sarafinmahtab.tnsassistant.teacher.studentlist;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sarafinmahtab.tnsassistant.MySingleton;
import com.sarafinmahtab.tnsassistant.R;
import com.sarafinmahtab.tnsassistant.ServerAddress;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentList extends AppCompatActivity {

    String stdListURL = ServerAddress.getMyServerAddress().concat("std_list.php");

    String courseID, teacherID, stdName;

    List<StudentItem> stdListItem;
    StudentListAdapter stdAdapter;
    RecyclerView stdRecyclerView;

    SearchView stdSearchView;
    EditText searchEditText;
    ImageView closeButton;

    RelativeLayout dotAnimation;
    ObjectAnimator waveOneAnimator, waveTwoAnimator, waveThreeAnimator;
    TextView hangoutTvOne, hangoutTvTwo, hangoutTvThree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);

        Toolbar stdListToolbar = findViewById(R.id.stdlist_toolbar);
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

        //HangOut Animation
        dotAnimation = findViewById(R.id.std_list_dot_animation);
        hangoutTvOne = findViewById(R.id.hangoutTvOne);
        hangoutTvTwo = findViewById(R.id.hangoutTvTwo);
        hangoutTvThree = findViewById(R.id.hangoutTvThree);

        dotAnimation.setVisibility(View.VISIBLE);
        hangoutTvOne.setVisibility(View.VISIBLE);
        hangoutTvTwo.setVisibility(View.VISIBLE);
        hangoutTvThree.setVisibility(View.VISIBLE);

        waveAnimation();

        stdSearchView = findViewById(R.id.std_searchView);
        searchEditText = findViewById(R.id.search_src_text);
        closeButton = findViewById(R.id.search_close_btn);
        stdRecyclerView = findViewById(R.id.std_list_view);

        stdRecyclerView.setHasFixedSize(true);
        stdRecyclerView.setLayoutManager(new LinearLayoutManager(StudentList.this));

        stdListItem = new ArrayList<>();

        StringRequest stringRequestForStdList = new StringRequest(Request.Method.POST, stdListURL, new Response.Listener<String>() {
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
                                obj.getString("email_no"),
                                obj.getString("contact_no"));
                        stdListItem.add(studentItem);
                    }

                    dotAnimation.setVisibility(View.GONE);
                    hangoutTvOne.setVisibility(View.GONE);
                    hangoutTvTwo.setVisibility(View.GONE);
                    hangoutTvThree.setVisibility(View.GONE);

                    stdAdapter = new StudentListAdapter(stdListItem, StudentList.this);
                    stdRecyclerView.setAdapter(stdAdapter);
                } catch (JSONException e) {
                    dotAnimation.setVisibility(View.GONE);
                    hangoutTvOne.setVisibility(View.GONE);
                    hangoutTvTwo.setVisibility(View.GONE);
                    hangoutTvThree.setVisibility(View.GONE);

                    Toast.makeText(StudentList.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dotAnimation.setVisibility(View.GONE);
                hangoutTvOne.setVisibility(View.GONE);
                hangoutTvTwo.setVisibility(View.GONE);
                hangoutTvThree.setVisibility(View.GONE);

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

        stdSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                stdAdapter.checkQueryFromList(newText.toLowerCase());

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

    //HangOut Animation
    public void waveAnimation() {
        PropertyValuesHolder tvOne_Y = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, -40.0f);
        PropertyValuesHolder tvOne_X = PropertyValuesHolder.ofFloat(View.TRANSLATION_X, 0);
        waveOneAnimator = ObjectAnimator.ofPropertyValuesHolder(hangoutTvOne, tvOne_X, tvOne_Y);
        waveOneAnimator.setRepeatCount(-1);
        waveOneAnimator.setRepeatMode(ValueAnimator.REVERSE);
        waveOneAnimator.setDuration(300);
        waveOneAnimator.start();

        PropertyValuesHolder tvTwo_Y = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, -40.0f);
        PropertyValuesHolder tvTwo_X = PropertyValuesHolder.ofFloat(View.TRANSLATION_X, 0);
        waveTwoAnimator = ObjectAnimator.ofPropertyValuesHolder(hangoutTvTwo, tvTwo_X, tvTwo_Y);
        waveTwoAnimator.setRepeatCount(-1);
        waveTwoAnimator.setRepeatMode(ValueAnimator.REVERSE);
        waveTwoAnimator.setDuration(300);
        waveTwoAnimator.setStartDelay(100);
        waveTwoAnimator.start();

        PropertyValuesHolder tvThree_Y = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, -40.0f);
        PropertyValuesHolder tvThree_X = PropertyValuesHolder.ofFloat(View.TRANSLATION_X, 0);
        waveThreeAnimator = ObjectAnimator.ofPropertyValuesHolder(hangoutTvThree, tvThree_X, tvThree_Y);
        waveThreeAnimator.setRepeatCount(-1);
        waveThreeAnimator.setRepeatMode(ValueAnimator.REVERSE);
        waveThreeAnimator.setDuration(300);
        waveThreeAnimator.setStartDelay(200);
        waveThreeAnimator.start();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
