package com.sarafinmahtab.tnsassistant;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.sarafinmahtab.tnsassistant.teacher.TeacherActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText Username, Password;

    private static RadioGroup radioGroup;
    private static RadioButton radioButton;
    private static Button signIn, register;

    private String login_url = "http://192.168.0.102/TnSAssistant/teacher_login.php";
    private AlertDialog.Builder builder;

    public static int radio_key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        radioClickAction();
        onSigninButtonClick();
//        onRegisterButtonClick();
    }

    public void radioClickAction() {
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(
                new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                        int selected_id = radioGroup.getCheckedRadioButtonId();
                        radioButton = (RadioButton) findViewById(selected_id);
                        if(radioButton.getId() == R.id.teacher) {
                            radio_key = 1;
                        } else if(radioButton.getId() == R.id.student) {
                            radio_key = 2;
                        }
                    }
                }
        );
    }

    public void onSigninButtonClick() {
        signIn = (Button) findViewById(R.id.signIn);
        signIn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Username = (EditText) findViewById(R.id.username);
                        Password = (EditText) findViewById(R.id.passWord);
                        builder = new AlertDialog.Builder(LoginActivity.this);

                        String radio_str;
                        final String username = Username.getText().toString();
                        final String password = Password.getText().toString();

                        switch (radio_key) {
                            case 1:
                                if(username.equals("") || password.equals("")) {
                                    builder.setTitle("Invalid Username or Password!!");
                                    display_alert("Please fill all the fields.");
                                } else {
                                    StringRequest stringRequestforLogin = new StringRequest(Request.Method.POST, login_url, new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
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
                                                        bundle.putString("t_first_name", jsonObject.getString("t_first_name"));
                                                        bundle.putString("t_last_name", jsonObject.getString("t_last_name"));
                                                        bundle.putString("employee_code", jsonObject.getString("employee_code"));
                                                        bundle.putString("desig_name", jsonObject.getString("desig_name"));
                                                        bundle.putString("dept_code", jsonObject.getString("dept_code"));
                                                        bundle.putString("email", jsonObject.getString("email"));
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
                                            Toast.makeText(LoginActivity.this, "Error occurred!!", Toast.LENGTH_LONG).show();
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

                                    MySingleton.getMyInstance(LoginActivity.this).addToRequestQueue(stringRequestforLogin);
                                }
                                break;
                            case 2:
                                radio_str = "Student";
                                Toast.makeText(LoginActivity.this, radio_str + '\n' + username + '\n' + password, Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                Toast.makeText(LoginActivity.this, "You haven't checked any profile yet!", Toast.LENGTH_LONG).show();
                                break;
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

    public void onRegisterButtonClick() {
        register = (Button) findViewById(R.id.onstart);
        register.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent;
                        if(radio_key == 1) {
                            intent = new Intent("com.sarafinmahtab.tnsassistant.teacher.RegisterTeacher");
                            startActivity(intent);
                        } else if(radio_key == 2) {
                            intent = new Intent("com.sarafinmahtab.tnsassistant.student.RegisterStudent");
                            startActivity(intent);
                        } else {
                            Toast.makeText(LoginActivity.this, "You haven't checked any profile yet!", Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );
    }
}
