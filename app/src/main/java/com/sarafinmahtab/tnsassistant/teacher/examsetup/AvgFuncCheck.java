package com.sarafinmahtab.tnsassistant.teacher.examsetup;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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

public class AvgFuncCheck extends AppCompatActivity {

    String avgFuncUpdateUrl = ServerAddress.getMyServerAddress().concat("avg_func_update.php");
    String avgFunctionsUrl = ServerAddress.getMyServerAddress().concat("avg_func_loader.php");

    String courseID, courseCode, url;

    CheckBox tt1CheckBox, tt2CheckBox, attendanceCheckBox, vivaCheckBox, finalCheckBox;
    Button updateBtn, cancelBtn;

    CourseCustomize courseCustomize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avg_func_check);

        Bundle bundle = getIntent().getExtras();
        url = bundle.getString("url");

        courseID = bundle.getString("course_id");
        courseCode = bundle.getString("course_code");
        courseCustomize = (CourseCustomize) getIntent().getSerializableExtra("course_customize");

        tt1CheckBox = (CheckBox) findViewById(R.id.tt1_check_avg);
        tt2CheckBox = (CheckBox) findViewById(R.id.tt2_check_avg);
        attendanceCheckBox = (CheckBox) findViewById(R.id.attendance_check_avg);
        vivaCheckBox = (CheckBox) findViewById(R.id.viva_check_avg);
        finalCheckBox = (CheckBox) findViewById(R.id.final_check_avg);

        tt1CheckBox.setText(courseCustomize.getCustomTT1Name());
        tt2CheckBox.setText(courseCustomize.getCustomTT2Name());
        attendanceCheckBox.setText(courseCustomize.getCustomAttendanceName());
        vivaCheckBox.setText(courseCustomize.getCustomVivaName());
        finalCheckBox.setText(courseCustomize.getCustomFinalName());

        updateBtn = (Button) findViewById(R.id.update_btn2);
        cancelBtn = (Button) findViewById(R.id.cancel_btn2);

        callAvgFunc();

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringRequest stringRequestForAvgUpdate = new StringRequest(Request.Method.POST, avgFuncUpdateUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        switch (response) {
                            case "success":
                                Toast.makeText(AvgFuncCheck.this, "Updated Successfully.", Toast.LENGTH_LONG).show();

                                AvgFuncCheck.super.onBackPressed();

                                break;
                            case "failed":
                                Toast.makeText(AvgFuncCheck.this, "Update Failed!! Please Try Again.", Toast.LENGTH_LONG).show();
                                break;
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AvgFuncCheck.this, error.getMessage(), Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();

                        if(tt1CheckBox.isChecked()) {
                            params.put("tt1_check_box", "1");
                        } else {
                            params.put("tt1_check_box", "0");
                        }

                        if(tt2CheckBox.isChecked()) {
                            params.put("tt2_check_box", "1");
                        } else {
                            params.put("tt2_check_box", "0");
                        }

                        if(attendanceCheckBox.isChecked()) {
                            params.put("attendance_check_box", "1");
                        } else {
                            params.put("attendance_check_box", "0");
                        }

                        if(vivaCheckBox.isChecked()) {
                            params.put("viva_check_box", "1");
                        } else {
                            params.put("viva_check_box", "0");
                        }

                        if(finalCheckBox.isChecked()) {
                            params.put("final_check_box", "1");
                        } else {
                            params.put("final_check_box", "0");
                        }

                        params.put("course_id", courseID);

                        return params;
                    }
                };

                MySingleton.getMyInstance(AvgFuncCheck.this).addToRequestQueue(stringRequestForAvgUpdate);
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AvgFuncCheck.super.onBackPressed();
            }
        });
    }

    private void callAvgFunc() {

        StringRequest stringRequestForAvgFunc = new StringRequest(Request.Method.POST, avgFunctionsUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("avg_func_loader");
                    JSONObject obj = jsonArray.getJSONObject(0);

                    boolean[] checkedAvgArray = new boolean[5];

                    checkedAvgArray[0] = obj.getString("custom_test1_avg_check").equals("1");
                    checkedAvgArray[1] = obj.getString("custom_test2_avg_check").equals("1");
                    checkedAvgArray[2] = obj.getString("custom_attendance_avg_check").equals("1");
                    checkedAvgArray[3] = obj.getString("custom_viva_avg_check").equals("1");
                    checkedAvgArray[4] = obj.getString("custom_final_avg_check").equals("1");

                    courseCustomize.setCheckedAvgArray(checkedAvgArray);

                    tt1CheckBox.setChecked(checkedAvgArray[0]);
                    tt1CheckBox.setText(courseCustomize.getCustomTT1Name());

                    tt2CheckBox.setChecked(checkedAvgArray[1]);
                    tt2CheckBox.setText(courseCustomize.getCustomTT2Name());

                    attendanceCheckBox.setChecked(checkedAvgArray[2]);
                    attendanceCheckBox.setText(courseCustomize.getCustomAttendanceName());

                    vivaCheckBox.setChecked(checkedAvgArray[3]);
                    vivaCheckBox.setText(courseCustomize.getCustomVivaName());

                    finalCheckBox.setChecked(checkedAvgArray[4]);
                    finalCheckBox.setText(courseCustomize.getCustomFinalName());

                } catch (JSONException e) {
                    Toast.makeText(AvgFuncCheck.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AvgFuncCheck.this, error.getMessage(), Toast.LENGTH_LONG).show();
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

        MySingleton.getMyInstance(AvgFuncCheck.this).addToRequestQueue(stringRequestForAvgFunc);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
