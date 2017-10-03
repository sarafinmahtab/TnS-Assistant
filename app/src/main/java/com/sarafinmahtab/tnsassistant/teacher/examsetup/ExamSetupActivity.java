package com.sarafinmahtab.tnsassistant.teacher.examsetup;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.sarafinmahtab.tnsassistant.teacher.courselist.Course;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ExamSetupActivity extends AppCompatActivity {

    private String examDataLoadUrl = ServerAddress.getMyServerAddress().concat("custom_full_exam_data_loader.php");
    private String avgFuncUpdateUrl = ServerAddress.getMyServerAddress().concat("avg_func_update.php");

    String courseID, teacherID, courseCode;

    boolean[] checkedAvgArray = new boolean[5];

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
    TextView rulesResult;

    private ArrayList<String> nonAvgArray, avgArray;

    RelativeLayout dotAnimation;
    ObjectAnimator waveOneAnimator, waveTwoAnimator, waveThreeAnimator;
    TextView hangoutTvOne, hangoutTvTwo, hangoutTvThree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_setup);

        Toolbar toolbar = findViewById(R.id.exam_setup_toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        Bundle courseBundle = getIntent().getExtras();

        courseID = courseBundle.getString("course_id");
        teacherID = courseBundle.getString("teacher_id");
        courseCode = courseBundle.getString("course_code");

        toolbar.setTitleTextColor(0xFFFFFFFF);

        getSupportActionBar().setTitle(String.format("Set Exam Rules of %s", courseCode));

        callCustomCourseData();

        customTT1Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = LayoutInflater.from(ExamSetupActivity.this).inflate(R.layout.update_custom_exam_dialog, null);

                final int width = ViewGroup.LayoutParams.MATCH_PARENT, height = ViewGroup.LayoutParams.WRAP_CONTENT;
                final Dialog bottomSheetDialog = new Dialog(ExamSetupActivity.this, R.style.MaterialDialogSheet);
                bottomSheetDialog.setContentView(view);
                bottomSheetDialog.setCancelable(false);

                if(bottomSheetDialog.getWindow() != null) {
                    bottomSheetDialog.getWindow().setLayout(width, height);
                    bottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);
                    bottomSheetDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
                    bottomSheetDialog.show();
                }

                final TextView dialogHeader = view.findViewById(R.id.update_custom_exam_dialog);
                final Button updateBtn = view.findViewById(R.id.update_btn);
                final ImageButton cancelBtn = view.findViewById(R.id.cancel_btn);

                final EditText ExamName = view.findViewById(R.id.edit_custom_name);
                final EditText ExamPercentage = view.findViewById(R.id.edit_custom_percent);

                ExamName.setText(CourseCustomize.getCustomTT1Name());
                ExamPercentage.setText(CourseCustomize.getCustomTT1Percent());

                updateBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final String newName = ExamName.getText().toString();
                        final String newPercent = ExamPercentage.getText().toString();

                        StringRequest stringRequest = new StringRequest(Request.Method.POST, tt1LoadUrl, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                switch (response) {
                                    case "success":
                                        Toast.makeText(ExamSetupActivity.this, "Updated SuccessFully", Toast.LENGTH_LONG).show();

                                        customTT1Name.setText(newName);
                                        customTT1Name.setTextColor(Color.rgb(0, 113, 14));
                                        CourseCustomize.setCustomTT1Name(newName);

                                        customTT1Percent.setText(newPercent);
                                        customTT1Percent.setTextColor(Color.rgb(0, 113, 14));
                                        CourseCustomize.setCustomTT1Percent(newPercent);

                                        bottomSheetDialog.cancel();
                                        break;
                                    case "failed":
                                        Toast.makeText(ExamSetupActivity.this, "Update Failed", Toast.LENGTH_LONG).show();
                                        break;
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(ExamSetupActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
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

                        MySingleton.getMyInstance(ExamSetupActivity.this).addToRequestQueue(stringRequest);
                    }
                });

                cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialog.cancel();
                    }
                });
            }
        });

        customTT2Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = LayoutInflater.from(ExamSetupActivity.this).inflate(R.layout.update_custom_exam_dialog, null);

                final int width = ViewGroup.LayoutParams.MATCH_PARENT, height = ViewGroup.LayoutParams.WRAP_CONTENT;
                final Dialog bottomSheetDialog = new Dialog(ExamSetupActivity.this, R.style.MaterialDialogSheet);
                bottomSheetDialog.setContentView(view);
                bottomSheetDialog.setCancelable(false);

                if(bottomSheetDialog.getWindow() != null) {
                    bottomSheetDialog.getWindow().setLayout(width, height);
                    bottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);
                    bottomSheetDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
                    bottomSheetDialog.show();
                }

                final TextView dialogHeader = view.findViewById(R.id.update_custom_exam_dialog);
                final Button updateBtn = view.findViewById(R.id.update_btn);
                final ImageButton cancelBtn = view.findViewById(R.id.cancel_btn);

                final EditText ExamName = view.findViewById(R.id.edit_custom_name);
                final EditText ExamPercentage = view.findViewById(R.id.edit_custom_percent);

                ExamName.setText(CourseCustomize.getCustomTT2Name());
                ExamPercentage.setText(CourseCustomize.getCustomTT2Percent());

                updateBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final String newName = ExamName.getText().toString();
                        final String newPercent = ExamPercentage.getText().toString();

                        StringRequest stringRequest = new StringRequest(Request.Method.POST, tt2LoadUrl, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                switch (response) {
                                    case "success":
                                        Toast.makeText(ExamSetupActivity.this, "Updated SuccessFully", Toast.LENGTH_LONG).show();

                                        customTT2Name.setText(newName);
                                        customTT2Name.setTextColor(Color.rgb(0, 113, 14));
                                        CourseCustomize.setCustomTT2Name(newName);

                                        customTT2Percent.setText(newPercent);
                                        customTT2Percent.setTextColor(Color.rgb(0, 113, 14));
                                        CourseCustomize.setCustomTT2Percent(newPercent);

                                        bottomSheetDialog.cancel();
                                        break;
                                    case "failed":
                                        Toast.makeText(ExamSetupActivity.this, "Update Failed", Toast.LENGTH_LONG).show();
                                        break;
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(ExamSetupActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
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

                        MySingleton.getMyInstance(ExamSetupActivity.this).addToRequestQueue(stringRequest);
                    }
                });

                cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialog.cancel();
                    }
                });
            }
        });

        customAttendanceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = LayoutInflater.from(ExamSetupActivity.this).inflate(R.layout.update_custom_exam_dialog, null);

                final int width = ViewGroup.LayoutParams.MATCH_PARENT, height = ViewGroup.LayoutParams.WRAP_CONTENT;
                final Dialog bottomSheetDialog = new Dialog(ExamSetupActivity.this, R.style.MaterialDialogSheet);
                bottomSheetDialog.setContentView(view);
                bottomSheetDialog.setCancelable(false);

                if(bottomSheetDialog.getWindow() != null) {
                    bottomSheetDialog.getWindow().setLayout(width, height);
                    bottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);
                    bottomSheetDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
                    bottomSheetDialog.show();
                }

                final TextView dialogHeader = view.findViewById(R.id.update_custom_exam_dialog);
                final Button updateBtn = view.findViewById(R.id.update_btn);
                final ImageButton cancelBtn = view.findViewById(R.id.cancel_btn);

                final EditText ExamName = view.findViewById(R.id.edit_custom_name);
                final EditText ExamPercentage = view.findViewById(R.id.edit_custom_percent);

                ExamName.setText(CourseCustomize.getCustomAttendanceName());
                ExamPercentage.setText(CourseCustomize.getCustomAttendancePercent());

                updateBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final String newName = ExamName.getText().toString();
                        final String newPercent = ExamPercentage.getText().toString();

                        StringRequest stringRequest = new StringRequest(Request.Method.POST, attendanceLoadUrl, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                switch (response) {
                                    case "success":
                                        Toast.makeText(ExamSetupActivity.this, "Updated SuccessFully", Toast.LENGTH_LONG).show();

                                        customAttendanceName.setText(newName);
                                        customAttendanceName.setTextColor(Color.rgb(0, 113, 14));
                                        CourseCustomize.setCustomAttendanceName(newName);

                                        customAttendancePercent.setText(newPercent);
                                        customAttendancePercent.setTextColor(Color.rgb(0, 113, 14));
                                        CourseCustomize.setCustomAttendancePercent(newPercent);

                                        bottomSheetDialog.cancel();
                                        break;
                                    case "failed":
                                        Toast.makeText(ExamSetupActivity.this, "Update Failed", Toast.LENGTH_LONG).show();
                                        break;
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(ExamSetupActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
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

                        MySingleton.getMyInstance(ExamSetupActivity.this).addToRequestQueue(stringRequest);
                    }
                });

                cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialog.cancel();
                    }
                });
            }
        });

        customVivaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = LayoutInflater.from(ExamSetupActivity.this).inflate(R.layout.update_custom_exam_dialog, null);

                final int width = ViewGroup.LayoutParams.MATCH_PARENT, height = ViewGroup.LayoutParams.WRAP_CONTENT;
                final Dialog bottomSheetDialog = new Dialog(ExamSetupActivity.this, R.style.MaterialDialogSheet);
                bottomSheetDialog.setContentView(view);
                bottomSheetDialog.setCancelable(false);

                if(bottomSheetDialog.getWindow() != null) {
                    bottomSheetDialog.getWindow().setLayout(width, height);
                    bottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);
                    bottomSheetDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
                    bottomSheetDialog.show();
                }

                final TextView dialogHeader = view.findViewById(R.id.update_custom_exam_dialog);
                final Button updateBtn = view.findViewById(R.id.update_btn);
                final ImageButton cancelBtn = view.findViewById(R.id.cancel_btn);

                final EditText ExamName = view.findViewById(R.id.edit_custom_name);
                final EditText ExamPercentage = view.findViewById(R.id.edit_custom_percent);

                ExamName.setText(CourseCustomize.getCustomVivaName());
                ExamPercentage.setText(CourseCustomize.getCustomVivaPercent());

                updateBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final String newName = ExamName.getText().toString();
                        final String newPercent = ExamPercentage.getText().toString();

                        StringRequest stringRequest = new StringRequest(Request.Method.POST, vivaLoadUrl, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                switch (response) {
                                    case "success":
                                        Toast.makeText(ExamSetupActivity.this, "Updated SuccessFully", Toast.LENGTH_LONG).show();

                                        customVivaName.setText(newName);
                                        customVivaName.setTextColor(Color.rgb(0, 113, 14));
                                        CourseCustomize.setCustomVivaName(newName);

                                        customVivaPercent.setText(newPercent);
                                        customVivaPercent.setTextColor(Color.rgb(0, 113, 14));
                                        CourseCustomize.setCustomVivaPercent(newPercent);

                                        bottomSheetDialog.cancel();
                                        break;
                                    case "failed":
                                        Toast.makeText(ExamSetupActivity.this, "Update Failed", Toast.LENGTH_LONG).show();
                                        break;
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(ExamSetupActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
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

                        MySingleton.getMyInstance(ExamSetupActivity.this).addToRequestQueue(stringRequest);
                    }
                });

                cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialog.cancel();
                    }
                });
            }
        });

        customFinalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = LayoutInflater.from(ExamSetupActivity.this).inflate(R.layout.update_custom_exam_dialog, null);

                final int width = ViewGroup.LayoutParams.MATCH_PARENT, height = ViewGroup.LayoutParams.WRAP_CONTENT;
                final Dialog bottomSheetDialog = new Dialog(ExamSetupActivity.this, R.style.MaterialDialogSheet);
                bottomSheetDialog.setContentView(view);
                bottomSheetDialog.setCancelable(false);

                if(bottomSheetDialog.getWindow() != null) {
                    bottomSheetDialog.getWindow().setLayout(width, height);
                    bottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);
                    bottomSheetDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
                    bottomSheetDialog.show();
                }

                final TextView dialogHeader = view.findViewById(R.id.update_custom_exam_dialog);
                final Button updateBtn = view.findViewById(R.id.update_btn);
                final ImageButton cancelBtn = view.findViewById(R.id.cancel_btn);

                final EditText ExamName = view.findViewById(R.id.edit_custom_name);
                final EditText ExamPercentage = view.findViewById(R.id.edit_custom_percent);

                ExamName.setText(CourseCustomize.getCustomFinalName());
                ExamPercentage.setText(CourseCustomize.getCustomFinalPercent());

                updateBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final String newName = ExamName.getText().toString();
                        final String newPercent = ExamPercentage.getText().toString();

                        StringRequest stringRequest = new StringRequest(Request.Method.POST, finalLoadUrl, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                switch (response) {
                                    case "success":
                                        Toast.makeText(ExamSetupActivity.this, "Updated SuccessFully", Toast.LENGTH_LONG).show();

                                        customFinalName.setText(newName);
                                        customFinalName.setTextColor(Color.rgb(0, 113, 14));
                                        CourseCustomize.setCustomFinalName(newName);

                                        customFinalPercent.setText(newPercent);
                                        customFinalPercent.setTextColor(Color.rgb(0, 113, 14));
                                        CourseCustomize.setCustomFinalPercent(newPercent);

                                        bottomSheetDialog.cancel();
                                        break;
                                    case "failed":
                                        Toast.makeText(ExamSetupActivity.this, "Update Failed", Toast.LENGTH_LONG).show();
                                        break;
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(ExamSetupActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
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

                        MySingleton.getMyInstance(ExamSetupActivity.this).addToRequestQueue(stringRequest);
                    }
                });

                cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialog.cancel();
                    }
                });
            }
        });

        avgFunction = findViewById(R.id.avg_check_btn);

        avgFunction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = LayoutInflater.from(ExamSetupActivity.this).inflate(R.layout.avg_func_check_dialog, null);

                final int width = ViewGroup.LayoutParams.MATCH_PARENT, height = ViewGroup.LayoutParams.WRAP_CONTENT;
                final Dialog bottomSheetDialog = new Dialog(ExamSetupActivity.this, R.style.MaterialDialogSheet);
                bottomSheetDialog.setContentView(view);
                bottomSheetDialog.setCancelable(false);

                if(bottomSheetDialog.getWindow() != null) {
                    bottomSheetDialog.getWindow().setLayout(width, height);
                    bottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);
                    bottomSheetDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
                    bottomSheetDialog.show();
                }

                final Button updateBtn = view.findViewById(R.id.update_btn2);
                final ImageButton cancelBtn = view.findViewById(R.id.cancel_btn2);

                final CheckBox tt1CheckBox = view.findViewById(R.id.tt1_check_avg);
                final CheckBox tt2CheckBox = view.findViewById(R.id.tt2_check_avg);
                final CheckBox attendanceCheckBox = view.findViewById(R.id.attendance_check_avg);
                final CheckBox vivaCheckBox = view.findViewById(R.id.viva_check_avg);
                final CheckBox finalCheckBox = view.findViewById(R.id.final_check_avg);

                checkedAvgArray = CourseCustomize.getCheckedAvgArray();

                tt1CheckBox.setChecked(checkedAvgArray[0]);
                tt1CheckBox.setText(CourseCustomize.getCustomTT1Name());

                tt2CheckBox.setChecked(checkedAvgArray[1]);
                tt2CheckBox.setText(CourseCustomize.getCustomTT2Name());

                attendanceCheckBox.setChecked(checkedAvgArray[2]);
                attendanceCheckBox.setText(CourseCustomize.getCustomAttendanceName());

                vivaCheckBox.setChecked(checkedAvgArray[3]);
                vivaCheckBox.setText(CourseCustomize.getCustomVivaName());

                finalCheckBox.setChecked(checkedAvgArray[4]);
                finalCheckBox.setText(CourseCustomize.getCustomFinalName());

                updateBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        StringRequest stringRequestForAvgUpdate = new StringRequest(Request.Method.POST, avgFuncUpdateUrl, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    String code = jsonObject.getString("code");

                                    switch (code) {
                                        case "success":

                                            JSONArray jsonArray = jsonObject.getJSONArray("avg_func_loader");
                                            JSONObject obj = jsonArray.getJSONObject(0);

                                            checkedAvgArray[0] = obj.getString("custom_test1_avg_check").equals("1");
                                            checkedAvgArray[1] = obj.getString("custom_test2_avg_check").equals("1");
                                            checkedAvgArray[2] = obj.getString("custom_attendance_avg_check").equals("1");
                                            checkedAvgArray[3] = obj.getString("custom_viva_avg_check").equals("1");
                                            checkedAvgArray[4] = obj.getString("custom_final_avg_check").equals("1");

                                            CourseCustomize.setCheckedAvgArray(checkedAvgArray);

                                            Toast.makeText(ExamSetupActivity.this, "Updated Successfully", Toast.LENGTH_LONG).show();

                                            bottomSheetDialog.cancel();
                                            break;
                                        case "failed":
                                            Toast.makeText(ExamSetupActivity.this, "Update Failed!! Please Try Again", Toast.LENGTH_LONG).show();
                                            break;
                                    }
                                } catch (JSONException e) {
                                    Toast.makeText(ExamSetupActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                    e.printStackTrace();
                                }

                                callCustomCourseData();

                                bottomSheetDialog.cancel();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(ExamSetupActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                                error.printStackTrace();
                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<>();

                                params.put("tt1_check_box", tt1CheckBox.isChecked() ? "1" : "0");
                                params.put("tt2_check_box", tt2CheckBox.isChecked() ? "1" : "0");
                                params.put("attendance_check_box", attendanceCheckBox.isChecked() ? "1" : "0");
                                params.put("viva_check_box", vivaCheckBox.isChecked() ? "1" : "0");
                                params.put("final_check_box", finalCheckBox.isChecked() ? "1" : "0");
                                params.put("course_id", courseID);

                                return params;
                            }
                        };

                        MySingleton.getMyInstance(ExamSetupActivity.this).addToRequestQueue(stringRequestForAvgUpdate);
                    }
                });

                cancelBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialog.cancel();
                    }
                });
            }
        });
    }

    private void callCustomCourseData() {

        //HangOut Animation
        dotAnimation = findViewById(R.id.exam_setup_dot_animation);
        hangoutTvOne = findViewById(R.id.hangoutTvOne);
        hangoutTvTwo = findViewById(R.id.hangoutTvTwo);
        hangoutTvThree = findViewById(R.id.hangoutTvThree);

        dotAnimation.setVisibility(View.VISIBLE);
        hangoutTvOne.setVisibility(View.VISIBLE);
        hangoutTvTwo.setVisibility(View.VISIBLE);
        hangoutTvThree.setVisibility(View.VISIBLE);

        waveAnimation();

        nonAvgArray = new ArrayList<>();
        avgArray = new ArrayList<>();

        rulesResult = findViewById(R.id.rules_result);

        customTT1Name = findViewById(R.id.custom_tt1_name);
        customTT1Percent = findViewById(R.id.custom_tt1_percent);
        customTT1Btn = findViewById(R.id.customize_tt1);

        customTT2Name = findViewById(R.id.custom_tt2_name);
        customTT2Percent = findViewById(R.id.custom_tt2_percent);
        customTT2Btn = findViewById(R.id.customize_tt2);

        customAttendanceName =  findViewById(R.id.custom_attendance_name);
        customAttendancePercent = findViewById(R.id.custom_attendance_percent);
        customAttendanceBtn = findViewById(R.id.customize_attendance);

        customVivaName = findViewById(R.id.custom_viva_name);
        customVivaPercent = findViewById(R.id.custom_viva_percent);
        customVivaBtn = findViewById(R.id.customize_viva);

        customFinalName = findViewById(R.id.custom_final_name);
        customFinalPercent = findViewById(R.id.custom_final_percent);
        customFinalBtn = findViewById(R.id.customize_final);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, examDataLoadUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("custom_full_exam_data_loader");
                    JSONObject obj = jsonArray.getJSONObject(0);

                    CourseCustomize.setCustomTT1Name(obj.getString("custom_test1_name"));
                    CourseCustomize.setCustomTT1Percent(obj.getString("custom_test1_percent"));
                    checkedAvgArray[0] = obj.getString("custom_test1_avg_check").equals("1");

                    if(checkedAvgArray[0]) {
                        avgArray.add("tt1");
                    } else {
                        nonAvgArray.add("tt1");
                    }

                    CourseCustomize.setCustomTT2Name(obj.getString("custom_test2_name"));
                    CourseCustomize.setCustomTT2Percent(obj.getString("custom_test2_percent"));
                    checkedAvgArray[1] = obj.getString("custom_test2_avg_check").equals("1");


                    if(checkedAvgArray[1]) {
                        avgArray.add("tt2");
                    } else {
                        nonAvgArray.add("tt2");
                    }

                    CourseCustomize.setCustomAttendanceName(obj.getString("custom_attendance_name"));
                    CourseCustomize.setCustomAttendancePercent(obj.getString("custom_attendance_percent"));
                    checkedAvgArray[2] = obj.getString("custom_attendance_avg_check").equals("1");


                    if(checkedAvgArray[2]) {
                        avgArray.add("presence");
                    } else {
                        nonAvgArray.add("presence");
                    }

                    CourseCustomize.setCustomVivaName(obj.getString("custom_viva_name"));
                    CourseCustomize.setCustomVivaPercent(obj.getString("custom_viva_percent"));
                    checkedAvgArray[3] = obj.getString("custom_viva_avg_check").equals("1");

                    if(checkedAvgArray[3]) {
                        avgArray.add("viva");
                    } else {
                        nonAvgArray.add("viva");
                    }

                    CourseCustomize.setCustomFinalName(obj.getString("custom_final_name"));
                    CourseCustomize.setCustomFinalPercent(obj.getString("custom_final_percent"));
                    checkedAvgArray[4] = obj.getString("custom_final_avg_check").equals("1");

                    if(checkedAvgArray[4]) {
                        avgArray.add("final");
                    } else {
                        nonAvgArray.add("final");
                    }

                    CourseCustomize.setCheckedAvgArray(checkedAvgArray);

                    customTT1Name.setText(CourseCustomize.getCustomTT1Name());
                    customTT1Percent.setText(CourseCustomize.getCustomTT1Percent());

                    customTT2Name.setText(CourseCustomize.getCustomTT2Name());
                    customTT2Percent.setText(CourseCustomize.getCustomTT2Percent());

                    customAttendanceName.setText(CourseCustomize.getCustomAttendanceName());
                    customAttendancePercent.setText(CourseCustomize.getCustomAttendancePercent());

                    customVivaName.setText(CourseCustomize.getCustomVivaName());
                    customVivaPercent.setText(CourseCustomize.getCustomVivaPercent());

                    customFinalName.setText(CourseCustomize.getCustomFinalName());
                    customFinalPercent.setText(CourseCustomize.getCustomFinalPercent());

                    //Setting Rules
                    String rules = "";

                    for(int i = 0; i < nonAvgArray.size(); i++) {
                        if(nonAvgArray.get(i).equals("tt1")) {
                            if(Integer.valueOf(CourseCustomize.getCustomTT1Percent()) != 0) {
                                rules = rules.concat("(" + CourseCustomize.getCustomTT1Name() + ")*" + CourseCustomize.getCustomTT1Percent() + "%");
                                if(i != nonAvgArray.size()-1) {
                                    rules = rules.concat(" + ");
                                } else if(avgArray.size() != 0) {
                                    rules = rules.concat(" + AVG(");
                                }
                            }
                        } else if(nonAvgArray.get(i).equals("tt2")) {
                            if(Integer.valueOf(CourseCustomize.getCustomTT2Percent()) != 0) {
                                rules = rules.concat("(" + CourseCustomize.getCustomTT2Name() + ")*" + CourseCustomize.getCustomTT2Percent() + "%");
                                if(i != nonAvgArray.size()-1) {
                                    rules = rules.concat(" + ");
                                } else if(avgArray.size() != 0) {
                                    rules = rules.concat(" + AVG(");
                                }
                            }
                        } else if(nonAvgArray.get(i).equals("presence")) {
                            if(Integer.valueOf(CourseCustomize.getCustomAttendancePercent()) != 0) {
                                rules = rules.concat("(" + CourseCustomize.getCustomAttendanceName() + ")*" + CourseCustomize.getCustomAttendancePercent() + "%");
                                if(i != nonAvgArray.size()-1) {
                                    rules = rules.concat(" + ");
                                } else if(avgArray.size() != 0) {
                                    rules = rules.concat(" + AVG(");
                                }
                            }
                        } else if(nonAvgArray.get(i).equals("viva")) {
                            if(Integer.valueOf(CourseCustomize.getCustomVivaPercent()) != 0) {
                                rules = rules.concat("(" + CourseCustomize.getCustomVivaName() + ")*" + CourseCustomize.getCustomVivaPercent() + "%");
                                if(i != nonAvgArray.size()-1) {
                                    rules = rules.concat(" + ");
                                } else if(avgArray.size() != 0) {
                                    rules = rules.concat(" + AVG(");
                                }
                            }
                        } else if(nonAvgArray.get(i).equals("final")) {
                            if(Integer.valueOf(CourseCustomize.getCustomFinalPercent()) != 0) {
                                rules = rules.concat("(" + CourseCustomize.getCustomFinalName() + ")*" + CourseCustomize.getCustomFinalPercent() + "%");
                                if(i != nonAvgArray.size()-1) {
                                    rules = rules.concat(" + ");
                                } else if(avgArray.size() != 0) {
                                    rules = rules.concat(" + AVG(");
                                }
                            }
                        }
                    }

                    for(int i = 0; i < avgArray.size(); i++) {
                        if(avgArray.get(i).equals("tt1")) {
                            if(Integer.valueOf(CourseCustomize.getCustomTT1Percent()) != 0) {
                                rules = rules.concat("(" + CourseCustomize.getCustomTT1Name() + ")*" + CourseCustomize.getCustomTT1Percent() + "%");
                                if(i != avgArray.size()-1) {
                                    rules = rules.concat(" + ");
                                } else {
                                    rules = rules.concat(")");
                                }
                            }
                        } else if(avgArray.get(i).equals("tt2")) {
                            if(Integer.valueOf(CourseCustomize.getCustomTT2Percent()) != 0) {
                                rules = rules.concat("(" + CourseCustomize.getCustomTT2Name() + ")*" + CourseCustomize.getCustomTT2Percent() + "%");
                                if(i != avgArray.size()-1) {
                                    rules = rules.concat(" + ");
                                } else {
                                    rules = rules.concat(")");
                                }
                            }
                        } else if(avgArray.get(i).equals("presence")) {
                            if(Integer.valueOf(CourseCustomize.getCustomAttendancePercent()) != 0) {
                                rules = rules.concat("(" + CourseCustomize.getCustomAttendanceName() + ")*" + CourseCustomize.getCustomAttendancePercent() + "%");
                                if(i != avgArray.size()-1) {
                                    rules = rules.concat(" + ");
                                } else {
                                    rules = rules.concat(")");
                                }
                            }
                        } else if(avgArray.get(i).equals("viva")) {
                            if(Integer.valueOf(CourseCustomize.getCustomVivaPercent()) != 0) {
                                rules = rules.concat("(" + CourseCustomize.getCustomVivaName() + ")*" + CourseCustomize.getCustomVivaPercent() + "%");
                                if(i != avgArray.size()-1) {
                                    rules = rules.concat(" + ");
                                } else {
                                    rules = rules.concat(")");
                                }
                            }
                        } else if(avgArray.get(i).equals("final")) {
                            if(Integer.valueOf(CourseCustomize.getCustomFinalPercent()) != 0) {
                                rules = rules.concat("(" + CourseCustomize.getCustomFinalName() + ")*" + CourseCustomize.getCustomFinalPercent() + "%");
                                if(i != avgArray.size()-1) {
                                    rules = rules.concat(" + ");
                                } else {
                                    rules = rules.concat(")");
                                }
                            }
                        }
                    }

                    if(avgArray.size() != 0) {
                        rules = rules.concat(")");
                    }

                    if(nonAvgArray.size() == 0 && avgArray.size() == 0) {
                        rulesResult.setText(com.sarafinmahtab.tnsassistant.R.string.no_rules_set_up);
                    }

                    rulesResult.setText(rules);

                    dotAnimation.setVisibility(View.GONE);
                    hangoutTvOne.setVisibility(View.GONE);
                    hangoutTvTwo.setVisibility(View.GONE);
                    hangoutTvThree.setVisibility(View.GONE);

                } catch (JSONException e) {
                    Toast.makeText(ExamSetupActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    dotAnimation.setVisibility(View.GONE);
                    hangoutTvOne.setVisibility(View.GONE);
                    hangoutTvTwo.setVisibility(View.GONE);
                    hangoutTvThree.setVisibility(View.GONE);

                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ExamSetupActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                dotAnimation.setVisibility(View.GONE);
                hangoutTvOne.setVisibility(View.GONE);
                hangoutTvTwo.setVisibility(View.GONE);
                hangoutTvThree.setVisibility(View.GONE);

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

        MySingleton.getMyInstance(ExamSetupActivity.this).addToRequestQueue(stringRequest);
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
    public void onResume() {
        super.onResume();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
