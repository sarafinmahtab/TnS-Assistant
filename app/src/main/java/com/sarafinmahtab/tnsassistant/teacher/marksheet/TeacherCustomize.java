package com.sarafinmahtab.tnsassistant.teacher.marksheet;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
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

public class TeacherCustomize extends Fragment {

    private MarkSheetActivity markSheetActivity;
    private CourseCustomize courseCustomize;

    String courseID, teacherID, courseCode;

    String examDataLoadUrl = ServerAddress.getMyServerAddress().concat("custom_exam_data_loader.php");

    String tt1LoadUrl = ServerAddress.getMyServerAddress().concat("custom_tt1_data_update.php");
    String tt2LoadUrl = ServerAddress.getMyServerAddress().concat("custom_tt2_data_update.php");
    String attendanceLoadUrl = ServerAddress.getMyServerAddress().concat("custom_attendance_data_update.php");
    String vivaLoadUrl = ServerAddress.getMyServerAddress().concat("custom_viva_data_update.php");
    String finalLoadUrl = ServerAddress.getMyServerAddress().concat("custom_final_data_update.php");

    TextView customTT1Name, customTT1Percent;
    ImageButton customTT1Btn;
    TextView customTT2Name, customTT2Percent;
    ImageButton customTT2Btn;
    TextView customAttendanceName, customAttendancePercent;
    ImageButton customAttendanceBtn;
    TextView customVivaName, customVivaPercent;
    ImageButton customVivaBtn;
    TextView customFinalName, customFinalPercent;
    ImageButton customFinalBtn;

    Button avgFunction;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_teacher_customize, container, false);

        markSheetActivity = (MarkSheetActivity) getActivity();
        courseCustomize = markSheetActivity.getCourseCustomize();

        courseID = markSheetActivity.getCourseID();
        teacherID = markSheetActivity.getTeacherID();
        courseCode = markSheetActivity.getCourseCode();

        customTT1Name = (TextView) rootView.findViewById(R.id.custom_tt1_name);
        customTT1Percent = (TextView) rootView.findViewById(R.id.custom_tt1_percent);
        customTT1Btn = (ImageButton) rootView.findViewById(R.id.customize_tt1);

        customTT2Name = (TextView) rootView.findViewById(R.id.custom_tt2_name);
        customTT2Percent = (TextView) rootView.findViewById(R.id.custom_tt2_percent);
        customTT2Btn = (ImageButton) rootView.findViewById(R.id.customize_tt2);

        customAttendanceName = (TextView) rootView.findViewById(R.id.custom_attendance_name);
        customAttendancePercent = (TextView) rootView.findViewById(R.id.custom_attendance_percent);
        customAttendanceBtn = (ImageButton) rootView.findViewById(R.id.customize_attendance);

        customVivaName = (TextView) rootView.findViewById(R.id.custom_viva_name);
        customVivaPercent = (TextView) rootView.findViewById(R.id.custom_viva_percent);
        customVivaBtn = (ImageButton) rootView.findViewById(R.id.customize_viva);

        customFinalName = (TextView) rootView.findViewById(R.id.custom_final_name);
        customFinalPercent = (TextView) rootView.findViewById(R.id.custom_final_percent);
        customFinalBtn = (ImageButton) rootView.findViewById(R.id.customize_final);

        callCustomCourseData();

        customTT1Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(markSheetActivity, UpdateCustomExam.class);

                Bundle bundle = new Bundle();
                bundle.putString("url", tt1LoadUrl);
                bundle.putString("name", customTT1Name.getText().toString());
                bundle.putString("percent", customTT1Percent.getText().toString());
                bundle.putString("course_id", courseID);
                bundle.putString("course_code", courseCode);
                intent.putExtras(bundle);

                startActivity(intent);
            }
        });

        customTT2Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(markSheetActivity, UpdateCustomExam.class);

                Bundle bundle = new Bundle();
                bundle.putString("url", tt2LoadUrl);
                bundle.putString("name", customTT2Name.getText().toString());
                bundle.putString("percent", customTT2Percent.getText().toString());
                bundle.putString("course_id", courseID);
                bundle.putString("course_code", courseCode);
                intent.putExtras(bundle);

                startActivity(intent);
            }
        });

        customAttendanceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(markSheetActivity, UpdateCustomExam.class);

                Bundle bundle = new Bundle();
                bundle.putString("url", attendanceLoadUrl);
                bundle.putString("name", customAttendanceName.getText().toString());
                bundle.putString("percent", customAttendancePercent.getText().toString());
                bundle.putString("course_id", courseID);
                bundle.putString("course_code", courseCode);
                intent.putExtras(bundle);

                startActivity(intent);
            }
        });

        customVivaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(markSheetActivity, UpdateCustomExam.class);

                Bundle bundle = new Bundle();
                bundle.putString("url", vivaLoadUrl);
                bundle.putString("name", customVivaName.getText().toString());
                bundle.putString("percent", customVivaPercent.getText().toString());
                bundle.putString("course_id", courseID);
                bundle.putString("course_code", courseCode);
                intent.putExtras(bundle);

                startActivity(intent);
            }
        });

        customFinalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(markSheetActivity, UpdateCustomExam.class);

                Bundle bundle = new Bundle();
                bundle.putString("url", finalLoadUrl);
                bundle.putString("name", customFinalName.getText().toString());
                bundle.putString("percent", customFinalPercent.getText().toString());
                bundle.putString("course_id", courseID);
                bundle.putString("course_code", courseCode);
                intent.putExtras(bundle);

                startActivity(intent);
            }
        });

        avgFunction = (Button) rootView.findViewById(R.id.avg_check_btn);

        avgFunction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(markSheetActivity, AvgFuncCheck.class);

                Bundle bundle = new Bundle();
                bundle.putString("url", vivaLoadUrl);

                bundle.putString("course_id", courseID);
                bundle.putString("course_code", courseCode);
                intent.putExtras(bundle);
                intent.putExtra("course_customize", courseCustomize);

                startActivity(intent);
            }
        });

        return rootView;
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
                    customTT1Name.setText(courseCustomize.getCustomTT1Name());
                    courseCustomize.setCustomTT1Percent(obj.getString("custom_test1_percent"));
                    customTT1Percent.setText(courseCustomize.getCustomTT1Percent());

                    courseCustomize.setCustomTT2Name(obj.getString("custom_test2_name"));
                    customTT2Name.setText(courseCustomize.getCustomTT2Name());
                    courseCustomize.setCustomTT2Percent(obj.getString("custom_test2_percent"));
                    customTT2Percent.setText(courseCustomize.getCustomTT2Percent());

                    courseCustomize.setCustomAttendanceName(obj.getString("custom_attendance_name"));
                    customAttendanceName.setText(courseCustomize.getCustomAttendanceName());
                    courseCustomize.setCustomAttendancePercent(obj.getString("custom_attendance_percent"));
                    customAttendancePercent.setText(courseCustomize.getCustomAttendancePercent());

                    courseCustomize.setCustomVivaName(obj.getString("custom_viva_name"));
                    customVivaName.setText(courseCustomize.getCustomVivaName());
                    courseCustomize.setCustomVivaPercent(obj.getString("custom_viva_percent"));
                    customVivaPercent.setText(courseCustomize.getCustomVivaPercent());

                    courseCustomize.setCustomFinalName(obj.getString("custom_final_name"));
                    customFinalName.setText(courseCustomize.getCustomFinalName());
                    courseCustomize.setCustomFinalPercent(obj.getString("custom_final_percent"));
                    customFinalPercent.setText(courseCustomize.getCustomFinalPercent());

                } catch (JSONException e) {
                    Toast.makeText(markSheetActivity, e.getMessage(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(markSheetActivity, error.getMessage(), Toast.LENGTH_LONG).show();
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

        MySingleton.getMyInstance(markSheetActivity).addToRequestQueue(stringRequest);
    }

    @Override
    public void onResume() {
        super.onResume();

        callCustomCourseData();
    }
}
