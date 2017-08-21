package com.sarafinmahtab.tnsassistant;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sarafinmahtab.tnsassistant.student.StudentActivity;
import com.sarafinmahtab.tnsassistant.teacher.TeacherActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    EditText Username, Password;
    RadioGroup radioGroup;
    RadioButton radioButton;
    Button signIn;

    String teac_login_url = "http://192.168.0.63/TnSAssistant/teacher_login.php";
    String std_login_url = "http://192.168.0.63/TnSAssistant/student_login.php";

//    String teac_login_url = "http://192.168.43.65/TnSAssistant/teacher_login.php";
//    String std_login_url = "http://192.168.43.65/TnSAssistant/student_login.php";

    private AlertDialog.Builder builder;
    private int radio_key = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.home_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("TnS Assistant");
        myToolbar.setTitleTextColor(0xFFFFFFFF);

        onRadioClickAction();
        onSigninButtonClick();
//        onRegisterButtonClick();
    }

    private void onRadioClickAction() {
        radioGroup = (RadioGroup) findViewById(R.id.radio_area);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                int selected_id = radioGroup.getCheckedRadioButtonId();
                radioButton = (RadioButton) findViewById(selected_id);

                if(radioButton.getId() == R.id.teacher_radio) {
                    radio_key = 1;
                } else if(radioButton.getId() == R.id.student_radio) {
                    radio_key = 2;
                }
            }
        });
    }

    public void onSigninButtonClick() {
        signIn = (Button) findViewById(R.id.signin_btn);
        signIn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
                        progressDialog.setMessage("Please Wait!!");
                        progressDialog.show();

                        Username = (EditText) findViewById(R.id.username_entry);
                        Password = (EditText) findViewById(R.id.password_entry);
                        builder = new AlertDialog.Builder(LoginActivity.this);

                        final String username = Username.getText().toString();
                        final String password = Password.getText().toString();

                        if(username.equals("") || password.equals("")) {
                            progressDialog.dismiss();
                            builder.setTitle("Blank Username or Password!!");
                            display_alert("Please fill all the fields.");
                        } else {

                            if(radio_key == 1) {

                                StringRequest stringRequestforTeacLogin = new StringRequest(Request.Method.POST, teac_login_url, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        progressDialog.dismiss();

                                        try {
                                            JSONArray jsonArray = new JSONArray(response);
                                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                                            String code = jsonObject.getString("code");

                                            switch (code) {
                                                case "login_failed":
                                                    builder.setTitle("Login failed!!");
                                                    display_alert(jsonObject.getString("message"));
                                                    break;
                                                case "login_success":
                                                    Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_LONG).show();
                                                    Intent intent = new Intent(LoginActivity.this, TeacherActivity.class);

                                                    Bundle bundle = new Bundle();
                                                    bundle.putString("teacher_id", jsonObject.getString("teacher_id"));
                                                    bundle.putString("t_first_name", jsonObject.getString("t_first_name"));
                                                    bundle.putString("t_last_name", jsonObject.getString("t_last_name"));
                                                    bundle.putString("display_picture", jsonObject.getString("display_picture"));
                                                    bundle.putString("employee_code", jsonObject.getString("employee_code"));
                                                    bundle.putString("desig_name", jsonObject.getString("desig_name"));
                                                    bundle.putString("dept_name", jsonObject.getString("dept_name"));
                                                    bundle.putString("email", jsonObject.getString("email"));
                                                    bundle.putString("user_id", jsonObject.getString("user_id"));
                                                    intent.putExtras(bundle);

                                                    startActivity(intent);
                                                    break;
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        progressDialog.dismiss();
                                        Toast.makeText(LoginActivity.this, "Connection Failed!!", Toast.LENGTH_LONG).show();
                                        error.printStackTrace();
                                    }
                                }) {
                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError {
                                        Map<String, String> params = new HashMap<String, String>();
                                        params.put("username", username);
                                        params.put("password", password);
                                        return params;
                                    }
                                };

                                MySingleton.getMyInstance(LoginActivity.this).addToRequestQueue(stringRequestforTeacLogin);

                            } else if(radio_key == 2) {

                                StringRequest stringRequestforStdLogin = new StringRequest(Request.Method.POST, std_login_url, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                        progressDialog.dismiss();

                                        try {
                                            JSONArray jsonArray = new JSONArray(response);
                                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                                            String code = jsonObject.getString("code");

                                            switch (code) {
                                                case "login_failed":
                                                    builder.setTitle("Login failed!!");
                                                    display_alert(jsonObject.getString("message"));
                                                    break;
                                                case "login_success":
                                                    Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_LONG).show();
                                                    Intent intent = new Intent(LoginActivity.this, StudentActivity.class);

                                                    Bundle bundle = new Bundle();
                                                    bundle.putString("student_id", jsonObject.getString("student_id"));
                                                    bundle.putString("registration_no", jsonObject.getString("registration_no"));
                                                    bundle.putString("email_no", jsonObject.getString("email_no"));
                                                    bundle.putString("s_first_name", jsonObject.getString("s_first_name"));
                                                    bundle.putString("s_last_name", jsonObject.getString("s_last_name"));
                                                    bundle.putString("std_display_picture", jsonObject.getString("std_display_picture"));
                                                    bundle.putString("dept_name", jsonObject.getString("dept_name"));
                                                    bundle.putString("user_id", jsonObject.getString("user_id"));

                                                    intent.putExtras(bundle);

                                                    startActivity(intent);
                                                    break;
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        progressDialog.dismiss();
                                        Toast.makeText(LoginActivity.this, "Connection Failed!!", Toast.LENGTH_LONG).show();
                                        error.printStackTrace();
                                    }
                                }) {
                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError {
                                        Map<String, String> params = new HashMap<String, String>();
                                        params.put("username", username);
                                        params.put("password", password);
                                        return params;
                                    }
                                };

                                MySingleton.getMyInstance(LoginActivity.this).addToRequestQueue(stringRequestforStdLogin);

                            } else if (radio_key == 0) {

                                progressDialog.dismiss();
                                builder.setTitle("Login not possible!!");
                                display_alert("You haven't checked any profile yet!");

                            }
                        }
                    }
                }
        );
    }

    public void display_alert(String message) {
        builder.setMessage(message);
        builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Username.setText("");
                        Password.setText("");
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

//    public void onRegisterButtonClick() {
//        register = (Button) findViewById(R.id.onstart);
//        register.setOnClickListener(
//                new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent;
//                        if(radio_key == 1) {
//                            intent = new Intent("com.sarafinmahtab.tnsassistant.teacher.RegisterTeacher");
//                            startActivity(intent);
//                        } else if(radio_key == 2) {
//                            intent = new Intent("com.sarafinmahtab.tnsassistant.student.RegisterStudent");
//                            startActivity(intent);
//                        } else {
//                            Toast.makeText(LoginActivity.this, "You haven't checked any profile yet!", Toast.LENGTH_LONG).show();
//                        }
//                    }
//                }
//        );
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.rate_us:
                Toast.makeText(LoginActivity.this, "Ratings", Toast.LENGTH_LONG).show();
            case R.id.about:
                Toast.makeText(LoginActivity.this, "About Us", Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
