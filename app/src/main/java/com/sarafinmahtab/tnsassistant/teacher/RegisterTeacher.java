package com.sarafinmahtab.tnsassistant.teacher;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sarafinmahtab.tnsassistant.MySingleton;
import com.sarafinmahtab.tnsassistant.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterTeacher extends AppCompatActivity {

    private EditText Username, FirstName, LastName, Email, Phone, Password, ConfirmPass;
    private String username, first_name, last_name, email, phone, password, confirm_pass;
//    private String reg_url = "http://192.168.0.63/TnSAssistant/teacher_register.php";
    private String reg_url = "http://192.168.0.100/TnSAssistant/teacher_register.php";
    private AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_teacher);

        Username = (EditText) findViewById(R.id.t_username);
        FirstName = (EditText) findViewById(R.id.t_first_name);
        LastName = (EditText) findViewById(R.id.t_last_name);
        Email = (EditText) findViewById(R.id.t_mail);
        Phone = (EditText) findViewById(R.id.t_phone);
        Password = (EditText) findViewById(R.id.t_password);
        ConfirmPass = (EditText) findViewById(R.id.t_confirm_pass);
        Button register_btn = (Button) findViewById(R.id.t_register);

        builder = new AlertDialog.Builder(RegisterTeacher.this);

        register_btn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        username = Username.getText().toString();
                        first_name = FirstName.getText().toString();
                        last_name = LastName.getText().toString();
                        email = Email.getText().toString();
                        phone = Phone.getText().toString();
                        password = Password.getText().toString();
                        confirm_pass = ConfirmPass.getText().toString();

                        if(username.equals("") || first_name.equals("") || last_name.equals("") || email.equals("") || phone.equals("") || password.equals("") || confirm_pass.equals("")) {
                            builder.setTitle("Some information is missing!!");
                            builder.setMessage("Please fill all the fields.");
                            display_alert("input_error");
                        } else {
                            if(!password.equals(confirm_pass)) {
                                builder.setTitle("Something went wrong!!");
                                builder.setMessage("Password didn't match!!");
                                display_alert("input_error");
                            } else {
                                StringRequest stringRequestforReg = new StringRequest(Request.Method.POST, reg_url, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONArray jsonArray = new JSONArray(response);
                                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                                            String code = jsonObject.getString("code");
                                            String message = jsonObject.getString("message");
                                            builder.setTitle("Server response");
                                            builder.setMessage(message);
                                            display_alert(code);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }, new Response.ErrorListener(){
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        error.printStackTrace();
                                    }
                                }) {
                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError {
                                        Map<String, String> params = new HashMap<String, String>();

                                        params.put("username", username);
                                        params.put("first_name", first_name);
                                        params.put("last_name", last_name);
                                        params.put("email", email);
                                        params.put("phone", phone);
                                        params.put("password", password);

                                        return params;
                                    }
                                };

                                MySingleton.getMyInstance(RegisterTeacher.this).addToRequestQueue(stringRequestforReg);
                            }
                        }
                    }
                }
        );
    }

    public void display_alert(final String code) {
        builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (code) {
                            case "input_error":
                                Password.setText("");
                                ConfirmPass.setText("");
                                break;
                            case "reg_failed":
                                Username.setText("");
                                FirstName.setText("");
                                LastName.setText("");
                                Email.setText("");
                                Phone.setText("");
                                Password.setText("");
                                break;
                            case "reg_success":
                                Username.setText("");
                                FirstName.setText("");
                                LastName.setText("");
                                Email.setText("");
                                Phone.setText("");
                                Password.setText("");
                                finish();
                                break;
                        }
                    }
                }
        );

        AlertDialog alertDialog = builder.show();
        alertDialog.show();
    }
}
