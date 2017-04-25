package com.sarafinmahtab.tnsassistant;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private static RadioGroup radioGroup;
    private static RadioButton radioButton;
    private String radio_str;

    private EditText email, password;
    private static Button signIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        onButtonClick();
    }

    public void onButtonClick() {
        signIn = (Button) findViewById(R.id.signIn);
        signIn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
                        int selected_id = radioGroup.getCheckedRadioButtonId();
                        radioButton = (RadioButton) findViewById(selected_id);
                        radio_str = radioButton.getText().toString();

                        email = (EditText) findViewById(R.id.email);
                        password = (EditText) findViewById(R.id.passWord);

                        String mail = email.getText().toString();
                        String pass = password.getText().toString();

                        Toast.makeText(LoginActivity.this, radio_str+'\n'+mail+'\n'+pass, Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }
}
