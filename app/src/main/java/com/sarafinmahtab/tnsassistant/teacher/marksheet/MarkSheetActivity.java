package com.sarafinmahtab.tnsassistant.teacher.marksheet;

import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

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

import java.util.HashMap;
import java.util.Map;

public class MarkSheetActivity extends AppCompatActivity {

    String examDataLoadUrl = ServerAddress.getMyServerAddress().concat("custom_exam_data_loader.php");
    String avgFunctionsUrl = ServerAddress.getMyServerAddress().concat("avg_func_loader.php");

    String courseID, teacherID, courseCode;

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    private CourseCustomize courseCustomize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_sheet);

        Toolbar toolbar = (Toolbar) findViewById(R.id.mark_sheet_toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.mark_sheet_container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.mark_sheet_tabs);
        tabLayout.setupWithViewPager(mViewPager);

        Bundle courseBundle = getIntent().getExtras();

        courseID = courseBundle.getString("course_id");
        teacherID = courseBundle.getString("teacher_id");
        courseCode = courseBundle.getString("course_code");

        toolbar.setTitleTextColor(0xFFFFFFFF);

        getSupportActionBar().setTitle(courseCode + " Result Sheet");

        courseCustomize = new CourseCustomize();
        callCustomCourseData();
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new MarkSheetUpdate();
                case 1:
                    return new TeacherCustomize();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Update";
                case 1:
                    return "Customize";
            }
            return null;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void callCustomCourseData() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, examDataLoadUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("custom_exam_data_loader");
                    JSONObject obj = jsonArray.getJSONObject(0);

                    courseCustomize.setCustomTT1Name(obj.getString("custom_test1_name"));
                    courseCustomize.setCustomTT1Percent(obj.getString("custom_test1_percent"));

                    courseCustomize.setCustomTT2Name(obj.getString("custom_test2_name"));
                    courseCustomize.setCustomTT2Percent(obj.getString("custom_test2_percent"));

                    courseCustomize.setCustomAttendanceName(obj.getString("custom_attendance_name"));
                    courseCustomize.setCustomAttendancePercent(obj.getString("custom_attendance_percent"));

                    courseCustomize.setCustomVivaName(obj.getString("custom_viva_name"));
                    courseCustomize.setCustomVivaPercent(obj.getString("custom_viva_percent"));

                    courseCustomize.setCustomFinalName(obj.getString("custom_final_name"));
                    courseCustomize.setCustomFinalPercent(obj.getString("custom_final_percent"));

                } catch (JSONException e) {
                    Toast.makeText(MarkSheetActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
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

        MySingleton.getMyInstance(MarkSheetActivity.this).addToRequestQueue(stringRequest);

        StringRequest stringRequestForAvgFunc = new StringRequest(Request.Method.POST, avgFunctionsUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("avg_func_loader");
                    JSONObject obj = jsonArray.getJSONObject(0);

                    courseCustomize.getCheckedAvgArray().set(0, Boolean.parseBoolean(obj.getString("custom_test1_avg_check")));
                    courseCustomize.getCheckedAvgArray().set(1, Boolean.parseBoolean(obj.getString("custom_test2_avg_check")));
                    courseCustomize.getCheckedAvgArray().set(2, Boolean.parseBoolean(obj.getString("custom_attendance_avg_check")));
                    courseCustomize.getCheckedAvgArray().set(3, Boolean.parseBoolean(obj.getString("custom_viva_avg_check")));
                    courseCustomize.getCheckedAvgArray().set(4, Boolean.parseBoolean(obj.getString("custom_final_avg_check")));

                } catch (JSONException e) {
                    Toast.makeText(MarkSheetActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
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

        MySingleton.getMyInstance(MarkSheetActivity.this).addToRequestQueue(stringRequestForAvgFunc);
    }

    public CourseCustomize getCourseCustomize() {
        return courseCustomize;
    }

    public String getCourseID() {
        return courseID;
    }

    public String getTeacherID() {
        return teacherID;
    }

    public String getCourseCode() {
        return courseCode;
    }
}
