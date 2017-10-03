package com.sarafinmahtab.tnsassistant.teacher.marksheet;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;

import android.os.Bundle;

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
import com.sarafinmahtab.tnsassistant.teacher.examsetup.CourseCustomize;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MarkSheetActivity extends AppCompatActivity {

    private String markSheetLoader = ServerAddress.getMyServerAddress().concat("mark_sheet_loader.php");

    String courseID, teacherID, courseCode;

    RecyclerView markSheetRecyclerView;
    MarkSheetAdapter markSheetAdapter;
    List<MarkListItem> stdMarkList;

    SearchView markSheetSearchView;
    EditText markSheetSearchEditText;
    ImageView markSheetCloseButton;

    RelativeLayout dotAnimation;
    ObjectAnimator waveOneAnimator, waveTwoAnimator, waveThreeAnimator;
    TextView hangoutTvOne, hangoutTvTwo, hangoutTvThree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_sheet);

        Toolbar toolbar = findViewById(R.id.mark_sheet_toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        Bundle courseBundle = getIntent().getExtras();

        courseID = courseBundle.getString("course_id");
        teacherID = courseBundle.getString("teacher_id");
        courseCode = courseBundle.getString("course_code");

        toolbar.setTitleTextColor(0xFFFFFFFF);

        getSupportActionBar().setTitle(courseCode + " Result Sheet");

        //HangOut Animation
        dotAnimation = findViewById(R.id.mark_sheet_dot_animation);
        hangoutTvOne = findViewById(R.id.hangoutTvOne);
        hangoutTvTwo = findViewById(R.id.hangoutTvTwo);
        hangoutTvThree = findViewById(R.id.hangoutTvThree);

        dotAnimation.setVisibility(View.VISIBLE);
        hangoutTvOne.setVisibility(View.VISIBLE);
        hangoutTvTwo.setVisibility(View.VISIBLE);
        hangoutTvThree.setVisibility(View.VISIBLE);

        waveAnimation();

        markSheetSearchView = findViewById(R.id.frag_searchView_mark_update);
        markSheetSearchEditText = findViewById(R.id.search_src_text);
        markSheetCloseButton = findViewById(R.id.search_close_btn);

        markSheetRecyclerView = findViewById(R.id.frag_recyclerView_mark_update);
        markSheetRecyclerView.setHasFixedSize(true);
        markSheetRecyclerView.setLayoutManager(new LinearLayoutManager(MarkSheetActivity.this));
        stdMarkList = new ArrayList<>();

        StringRequest stringRequestForStdMarkList = new StringRequest(Request.Method.POST, markSheetLoader, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    //Loads all Custom Course Exam data
                    JSONArray jsonArray1 = jsonObject.getJSONArray("custom_full_exam_data_loader");
                    JSONObject obj1 = jsonArray1.getJSONObject(0);

                    boolean[] checkedAvgArray = new boolean[5];

                    CourseCustomize.setCustomTT1Name(obj1.getString("custom_test1_name"));
                    CourseCustomize.setCustomTT1Percent(obj1.getString("custom_test1_percent"));
                    checkedAvgArray[0] = obj1.getString("custom_test1_avg_check").equals("1");

                    CourseCustomize.setCustomTT2Name(obj1.getString("custom_test2_name"));
                    CourseCustomize.setCustomTT2Percent(obj1.getString("custom_test2_percent"));
                    checkedAvgArray[1] = obj1.getString("custom_test2_avg_check").equals("1");

                    CourseCustomize.setCustomAttendanceName(obj1.getString("custom_attendance_name"));
                    CourseCustomize.setCustomAttendancePercent(obj1.getString("custom_attendance_percent"));
                    checkedAvgArray[2] = obj1.getString("custom_attendance_avg_check").equals("1");

                    CourseCustomize.setCustomVivaName(obj1.getString("custom_viva_name"));
                    CourseCustomize.setCustomVivaPercent(obj1.getString("custom_viva_percent"));
                    checkedAvgArray[3] = obj1.getString("custom_viva_avg_check").equals("1");

                    CourseCustomize.setCustomFinalName(obj1.getString("custom_final_name"));
                    CourseCustomize.setCustomFinalPercent(obj1.getString("custom_final_percent"));
                    checkedAvgArray[4] = obj1.getString("custom_final_avg_check").equals("1");

                    CourseCustomize.setCheckedAvgArray(checkedAvgArray);

                    //Loads all the Marks of each Student
                    JSONArray jsonArray2 = jsonObject.getJSONArray("mark_sheet_loader");

                    for (int i = 0; i < jsonArray2.length(); i++) {
                        JSONObject obj2 = jsonArray2.getJSONObject(i);

                        MarkListItem markListItem = new MarkListItem();

                        markListItem.setCourseRegID(obj2.getString("course_reg_id"));
                        markListItem.setRegNo(obj2.getString("registration_no"));
                        markListItem.setMarkSheetID(obj2.getString("marksheet_id"));
                        markListItem.setTermTest1_Mark(obj2.getString("term_test_1"));
                        markListItem.setTermTest2_Mark(obj2.getString("term_test_2"));
                        markListItem.setAttendanceMark(obj2.getString("attendance"));
                        markListItem.setVivaMark(obj2.getString("viva"));
                        markListItem.setFinalExamMark(obj2.getString("final_exam"));
                        markListItem.setMarksOutOf100(obj2.getString("marks_out_of_100"));

                        stdMarkList.add(markListItem);
                    }

                    dotAnimation.setVisibility(View.GONE);
                    hangoutTvOne.setVisibility(View.GONE);
                    hangoutTvTwo.setVisibility(View.GONE);
                    hangoutTvThree.setVisibility(View.GONE);

                    markSheetAdapter = new MarkSheetAdapter(stdMarkList, MarkSheetActivity.this);
                    markSheetRecyclerView.setAdapter(markSheetAdapter);

                } catch (JSONException e) {
                    dotAnimation.setVisibility(View.GONE);
                    hangoutTvOne.setVisibility(View.GONE);
                    hangoutTvTwo.setVisibility(View.GONE);
                    hangoutTvThree.setVisibility(View.GONE);

                    Toast.makeText(MarkSheetActivity.this, response, Toast.LENGTH_LONG).show();
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

                Toast.makeText(MarkSheetActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
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

        MySingleton.getMyInstance(MarkSheetActivity.this).addToRequestQueue(stringRequestForStdMarkList);

        markSheetSearchView.onActionViewExpanded();
        markSheetSearchView.setIconified(false);
        markSheetSearchView.setQueryHint("Search by Reg ID or Name");

        if(!markSheetSearchView.isFocused()) {
            markSheetSearchView.clearFocus();
        }

        markSheetSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                markSheetAdapter.checkQueryFromList(newText.toLowerCase());

                return true;
            }
        });

        markSheetCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Clear the text from EditText view
                markSheetSearchEditText.setText("");

                //Clear query
                markSheetSearchView.setQuery("", false);
                markSheetAdapter.notifyDataSetChanged();
                markSheetSearchView.clearFocus();
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
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
