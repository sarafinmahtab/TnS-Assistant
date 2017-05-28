package com.sarafinmahtab.tnsassistant;

import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class LoginActivity extends AppCompatActivity {

    private EditText email, password;

    private static RadioGroup radioGroup;
    private static RadioButton radioButton;
    private static Button signIn, register;

    public String server_url = "http://192.168.0.63/tnsAssistant/reg_response.php";

    public static int radio_key;

//    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//        Cache cache = new DiskBasedCache(getCacheDir(), 1024*1024);
//        Network network = new BasicNetwork(new HurlStack());
//        requestQueue = new RequestQueue(cache, network);
//        requestQueue.start();

        radioClickAction();
        onLoginButtonClick();
        onRegisterButtonClick();
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

    public void onLoginButtonClick() {
        signIn = (Button) findViewById(R.id.signIn);
        signIn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        email = (EditText) findViewById(R.id.email);
                        password = (EditText) findViewById(R.id.passWord);

                        String radio_str;
                        String mail = email.getText().toString();
                        String pass = password.getText().toString();
                        if(radio_key == 1) {
                            radio_str = "Teacher";
                            String type = "Login";
                            Toast.makeText(LoginActivity.this, radio_str+'\n'+mail+'\n'+pass, Toast.LENGTH_SHORT).show();
                        } else if(radio_key == 2) {
                            radio_str = "Student";
                            Toast.makeText(LoginActivity.this, radio_str+'\n'+mail+'\n'+pass, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LoginActivity.this, "You haven't checked any profile yet!", Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );
    }

    public void onRegisterButtonClick() {
        register = (Button) findViewById(R.id.register);
        register.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent;
                        if(radio_key == 1) {
                            intent = new Intent("com.sarafinmahtab.tnsassistant.teacher.RegisterTeacher");
                            StringRequest stringRequestforReg = new StringRequest(Request.Method.POST, server_url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Toast.makeText(LoginActivity.this, response, Toast.LENGTH_LONG).show();
                                }
                            }, new Response.ErrorListener(){
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(LoginActivity.this, "An Error Occurred", Toast.LENGTH_LONG).show();
                                    error.printStackTrace();
                                }
                            });

                            MySingleton.getMyInstance(getApplicationContext()).addToRequestQueue(stringRequestforReg);
                            startActivity(intent);
                        } else if(radio_key == 2) {
                            intent = new Intent("com.sarafinmahtab.tnsassistant.student.RegisterStudent");
                            StringRequest stringRequestforReg = new StringRequest(Request.Method.POST, server_url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    Toast.makeText(LoginActivity.this, response, Toast.LENGTH_LONG).show();
                                }
                            }, new Response.ErrorListener(){
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(LoginActivity.this, "An Error Occurred", Toast.LENGTH_LONG).show();
                                    error.printStackTrace();
                                }
                            });

                            MySingleton.getMyInstance(getApplicationContext()).addToRequestQueue(stringRequestforReg);
                            startActivity(intent);
                        } else {
                            Toast.makeText(LoginActivity.this, "You haven't checked any profile yet!", Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );
    }
}
