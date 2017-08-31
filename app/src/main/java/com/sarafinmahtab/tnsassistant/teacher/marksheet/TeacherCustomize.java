package com.sarafinmahtab.tnsassistant.teacher.marksheet;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TeacherCustomize extends Fragment {

    MarkSheetActivity markSheetActivity;
    CourseCustomize courseCustomize;

    String courseID, teacherID;

    String examDataLoadUrl = ServerAddress.getMyServerAddress().concat("custom_exam_data_loader.php");
    String avgFunctionsUrl = ServerAddress.getMyServerAddress().concat("avg_func_loader.php");
    String avgFuncUpdateUrl = ServerAddress.getMyServerAddress().concat("avg_func_update.php");

    String tt1LoadUrl = ServerAddress.getMyServerAddress().concat(".php");
    String tt2LoadUrl = ServerAddress.getMyServerAddress().concat(".php");
    String attendanceLoadUrl = ServerAddress.getMyServerAddress().concat(".php");
    String vivaLoadUrl = ServerAddress.getMyServerAddress().concat(".php");
    String finalLoadUrl = ServerAddress.getMyServerAddress().concat(".php");

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

    Button avgFunction, updateBtn;

    CheckBox tt1CheckBox, tt2CheckBox, attendanceCheckBox, vivaCheckBox, finalCheckBox;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_teacher_customize, container, false);

        markSheetActivity = (MarkSheetActivity) getActivity();
        courseCustomize = new CourseCustomize();

        courseID = markSheetActivity.getCourseID();
        teacherID = markSheetActivity.getTeacherID();

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

        customTT1Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog(tt1LoadUrl, customTT1Name.getText().toString(), customTT1Percent.getText().toString());
            }
        });

        customTT2Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog(tt2LoadUrl, customTT2Name.getText().toString(), customTT2Percent.getText().toString());
            }
        });

        customAttendanceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog(attendanceLoadUrl, customAttendanceName.getText().toString(), customAttendancePercent.getText().toString());
            }
        });

        customVivaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog(vivaLoadUrl, customVivaName.getText().toString(), customVivaPercent.getText().toString());
            }
        });

        customFinalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog(finalLoadUrl, customFinalName.getText().toString(), customFinalPercent.getText().toString());
            }
        });

        avgFunction = (Button) rootView.findViewById(R.id.avg_check_btn);

        avgFunction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(markSheetActivity);

                LayoutInflater dialogInflater = LayoutInflater.from(markSheetActivity);
                final View customView = dialogInflater.inflate(R.layout.avg_func_check_dialog, null);

                tt1CheckBox = (CheckBox) customView.findViewById(R.id.tt1_check_avg);
                tt2CheckBox = (CheckBox) customView.findViewById(R.id.tt2_check_avg);
                attendanceCheckBox = (CheckBox) customView.findViewById(R.id.attendance_check_avg);
                vivaCheckBox = (CheckBox) customView.findViewById(R.id.viva_check_avg);
                finalCheckBox = (CheckBox) customView.findViewById(R.id.final_check_avg);

                StringRequest stringRequestForAvgFunc = new StringRequest(Request.Method.POST, avgFunctionsUrl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("avg_func_loader");
                            JSONObject obj = jsonArray.getJSONObject(0);

                            tt1CheckBox.setChecked(Boolean.parseBoolean(obj.getString("custom_test1_avg_check")));
                            tt1CheckBox.setText(courseCustomize.getCustomTT1Name());

                            tt2CheckBox.setChecked(Boolean.parseBoolean(obj.getString("custom_test2_avg_check")));
                            tt2CheckBox.setText(courseCustomize.getCustomTT2Name());

                            attendanceCheckBox.setChecked(Boolean.parseBoolean(obj.getString("custom_attendance_avg_check")));
                            attendanceCheckBox.setText(courseCustomize.getCustomAttendanceName());

                            vivaCheckBox.setChecked(Boolean.parseBoolean(obj.getString("custom_viva_avg_check")));
                            vivaCheckBox.setText(courseCustomize.getCustomVivaName());

                            finalCheckBox.setChecked(Boolean.parseBoolean(obj.getString("custom_final_avg_check")));
                            finalCheckBox.setText(courseCustomize.getCustomFinalName());

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

                MySingleton.getMyInstance(markSheetActivity).addToRequestQueue(stringRequestForAvgFunc);

                builder.setView(customView)
                        .setTitle("Set Your Averages")
                        .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialog, int which) {
                                StringRequest stringRequestForAvgUpdate = new StringRequest(Request.Method.POST, avgFuncUpdateUrl, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONObject jsonObject = new JSONObject(response);

                                            switch (jsonObject.getString("data_report")) {
                                                case "success":
                                                    Toast.makeText(markSheetActivity, "Updated Successfully", Toast.LENGTH_LONG).show();
                                                    break;
                                                case "failed":
                                                    Toast.makeText(markSheetActivity, "Update Failed", Toast.LENGTH_LONG).show();
                                                    break;
                                                default:
                                                    Toast.makeText(markSheetActivity, "Update Failed", Toast.LENGTH_LONG).show();
                                                    break;
                                            }
                                        } catch (JSONException e) {
                                            Toast.makeText(markSheetActivity, e.getMessage(), Toast.LENGTH_LONG).show();
                                            e.printStackTrace();
                                        }
                                        dialog.cancel();
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

                                MySingleton.getMyInstance(markSheetActivity).addToRequestQueue(stringRequestForAvgUpdate);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        updateBtn = (Button) rootView.findViewById(R.id.update_btn_custom);

        return rootView;
    }

    private void openDialog(final String url, final String name, final String percent) {
        EditText examName, examPercent;

        AlertDialog.Builder builder = new AlertDialog.Builder(markSheetActivity);

        LayoutInflater dialogInflater = LayoutInflater.from(markSheetActivity);
        final View customView = dialogInflater.inflate(R.layout.custom_exam_edit_dialog, null);

        examName = (EditText) customView.findViewById(R.id.edit_custom_name);
        examPercent = (EditText) customView.findViewById(R.id.edit_custom_percent);

        examName.setText(name);
        examPercent.setText(percent);

        builder.setView(customView)
                .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                dialog.cancel();
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
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }
}
