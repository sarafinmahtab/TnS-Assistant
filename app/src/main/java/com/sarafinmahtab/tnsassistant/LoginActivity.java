package com.sarafinmahtab.tnsassistant;

import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.sarafinmahtab.tnsassistant.BackgroundWork.BackgroundWorker;

public class LoginActivity extends AppCompatActivity {

    private static RadioGroup radioGroup;
    private static RadioButton radioButton;
    public static int radio_key;

    private EditText email, password;
    private static Button signIn, register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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
                            BackgroundWorker backgroundWorker = new BackgroundWorker(LoginActivity.this);
                            backgroundWorker.execute(type, mail, pass);
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
