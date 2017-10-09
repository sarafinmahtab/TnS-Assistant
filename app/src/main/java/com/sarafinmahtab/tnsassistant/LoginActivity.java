package com.sarafinmahtab.tnsassistant;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.sarafinmahtab.tnsassistant.student.StudentActivity;
import com.sarafinmahtab.tnsassistant.teacher.TeacherActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    String insLoginURL = ServerAddress.getMyServerAddress().concat("teacher_login.php");
    String stdLoginURL = ServerAddress.getMyServerAddress().concat("student_login.php");

    EditText Username, Password;
    RadioGroup radioGroup;
    RadioButton radioButton;
    Button signIn;

    TextView loginStatus;

    TextView leftRightTvOne, leftRightTvTwo, leftRightTvThree, leftRightTvFour, leftRightTvFive;
    RelativeLayout leftRightAnimation;
    private int layoutWidth;

    private int radio_key = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);

        setContentView(R.layout.activity_login);

        loginStatus = findViewById(R.id.loginStatus);

        //Left to Right Animation
        leftRightAnimation = findViewById(R.id.leftRightAnimation);
        leftRightTvOne = findViewById(R.id.leftRightTvOne);
        leftRightTvTwo = findViewById(R.id.leftRightTvTwo);
        leftRightTvThree = findViewById(R.id.leftRightTvThree);
        leftRightTvFour = findViewById(R.id.leftRightTvFour);
        leftRightTvFive = findViewById(R.id.leftRightTvFive);

        leftRightAnimation.setVisibility(View.GONE);
        leftRightTvOne.setVisibility(View.GONE);
        leftRightTvTwo.setVisibility(View.GONE);
        leftRightTvThree.setVisibility(View.GONE);
        leftRightTvFour.setVisibility(View.GONE);
        leftRightTvFive.setVisibility(View.GONE);

        final LinearLayout layout = findViewById(R.id.linearLayout3);

        ViewTreeObserver viewTreeObserver = layout.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                layout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                layoutWidth  = layout.getMeasuredWidth()-30;
            }
        });

        Toolbar myToolbar = findViewById(R.id.home_toolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setTitleTextColor(0xFFFFFFFF);

        onRadioClickAction();
        onSigninButtonClick();
    }

    private void onRadioClickAction() {
        radioGroup = findViewById(R.id.radio_area);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                int selected_id = radioGroup.getCheckedRadioButtonId();
                radioButton = findViewById(selected_id);

                if(radioButton.getId() == R.id.teacher_radio) {
                    radio_key = 1;
                } else if(radioButton.getId() == R.id.student_radio) {
                    radio_key = 2;
                }
            }
        });
    }

    public void onSigninButtonClick() {
        signIn = findViewById(R.id.signin_btn);
        signIn.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Username = findViewById(R.id.username_entry);
                        Password = findViewById(R.id.password_entry);

                        final String username = Username.getText().toString();
                        final String password = Password.getText().toString();

                        if(TextUtils.isEmpty(username) && TextUtils.isEmpty(password)) {
                            Username.setError(getString(R.string.error_field_required));
                            Password.setError(getString(R.string.error_field_required));
                        } else if(TextUtils.isEmpty(username)) {
                            Username.setError(getString(R.string.error_field_required));
                        } else if(TextUtils.isEmpty(password)) {
                            Password.setError(getString(R.string.error_field_required));
                        } else if(password.length() < 4) {
                            Password.setError(getString(R.string.error_invalid_password));
                        }else {
                            loginStatus.setVisibility(View.GONE);
                            leftRightAnimation.setVisibility(View.VISIBLE);
                            leftRightTvOne.setVisibility(View.VISIBLE);
                            leftRightTvTwo.setVisibility(View.VISIBLE);
                            leftRightTvThree.setVisibility(View.VISIBLE);
                            leftRightTvFour.setVisibility(View.VISIBLE);
                            leftRightTvFive.setVisibility(View.VISIBLE);

                            leftToRightToLeftMove();

                            if(radio_key == 1) {

                                StringRequest stringRequestforTeacLogin = new StringRequest(Request.Method.POST, insLoginURL, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONArray jsonArray = new JSONArray(response);
                                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                                            String code = jsonObject.getString("code");

                                            loginStatus.setVisibility(View.VISIBLE);
                                            leftRightAnimation.setVisibility(View.GONE);
                                            leftRightTvOne.setVisibility(View.GONE);
                                            leftRightTvTwo.setVisibility(View.GONE);
                                            leftRightTvThree.setVisibility(View.GONE);
                                            leftRightTvFour.setVisibility(View.GONE);
                                            leftRightTvFive.setVisibility(View.GONE);

                                            switch (code) {
                                                case "login_failed":
                                                    loginStatus.setText(jsonObject.getString("message"));
                                                    break;
                                                case "login_success":
                                                    loginStatus.setTextColor(Color.BLACK);
                                                    loginStatus.setText(R.string.login_success);
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
                                            loginStatus.setVisibility(View.VISIBLE);
                                            leftRightAnimation.setVisibility(View.GONE);
                                            leftRightTvOne.setVisibility(View.GONE);
                                            leftRightTvTwo.setVisibility(View.GONE);
                                            leftRightTvThree.setVisibility(View.GONE);
                                            leftRightTvFour.setVisibility(View.GONE);
                                            leftRightTvFive.setVisibility(View.GONE);

                                            loginStatus.setText(e.getMessage());
                                        }
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        loginStatus.setVisibility(View.VISIBLE);
                                        leftRightAnimation.setVisibility(View.GONE);
                                        leftRightTvOne.setVisibility(View.GONE);
                                        leftRightTvTwo.setVisibility(View.GONE);
                                        leftRightTvThree.setVisibility(View.GONE);
                                        leftRightTvFour.setVisibility(View.GONE);
                                        leftRightTvFive.setVisibility(View.GONE);

                                        loginStatus.setText(R.string.network_unreachable);
                                        error.printStackTrace();
                                    }
                                }) {
                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError {
                                        Map<String, String> params = new HashMap<>();
                                        params.put("username", username);
                                        params.put("password", password);
                                        return params;
                                    }
                                };

                                MySingleton.getMyInstance(LoginActivity.this).addToRequestQueue(stringRequestforTeacLogin);

                            } else if(radio_key == 2) {

                                StringRequest stringRequestforStdLogin = new StringRequest(Request.Method.POST, stdLoginURL, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONArray jsonArray = new JSONArray(response);
                                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                                            String code = jsonObject.getString("code");

                                            loginStatus.setVisibility(View.VISIBLE);
                                            leftRightAnimation.setVisibility(View.GONE);
                                            leftRightTvOne.setVisibility(View.GONE);
                                            leftRightTvTwo.setVisibility(View.GONE);
                                            leftRightTvThree.setVisibility(View.GONE);
                                            leftRightTvFour.setVisibility(View.GONE);
                                            leftRightTvFive.setVisibility(View.GONE);

                                            switch (code) {
                                                case "login_failed":
                                                    loginStatus.setText(jsonObject.getString("message"));
                                                    break;
                                                case "login_success":
                                                    loginStatus.setTextColor(Color.BLACK);
                                                    loginStatus.setText(R.string.login_success);
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
                                            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                            e.printStackTrace();
                                        }
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        Toast.makeText(LoginActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                                        error.printStackTrace();
                                    }
                                }) {
                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError {
                                        Map<String, String> params = new HashMap<>();
                                        params.put("username", username);
                                        params.put("password", password);
                                        return params;
                                    }
                                };

                                MySingleton.getMyInstance(LoginActivity.this).addToRequestQueue(stringRequestforStdLogin);

                            } else if (radio_key == 0) {
                                loginStatus.setVisibility(View.VISIBLE);
                                leftRightAnimation.setVisibility(View.GONE);
                                leftRightTvOne.setVisibility(View.GONE);
                                leftRightTvTwo.setVisibility(View.GONE);
                                leftRightTvThree.setVisibility(View.GONE);
                                leftRightTvFour.setVisibility(View.GONE);
                                leftRightTvFive.setVisibility(View.GONE);

                                loginStatus.setText(R.string.have_not_checked);
                            }
                        }
                    }
                }
        );
    }

    public void leftToRightToLeftMove() {
        final ValueAnimator valueTvOne_x = ObjectAnimator.ofFloat(leftRightTvOne, "x", leftRightTvOne.getX(), layoutWidth * .84f);

        valueTvOne_x.setDuration(1000);
        valueTvOne_x.setRepeatCount(0);
        valueTvOne_x.setRepeatMode(ValueAnimator.REVERSE);
        valueTvOne_x.setInterpolator(new DecelerateInterpolator());

        final ValueAnimator valueTvTwo_x = ObjectAnimator.ofFloat(leftRightTvTwo, "x", leftRightTvTwo.getX(), layoutWidth * .87f);

        valueTvTwo_x.setDuration(1000);
        valueTvTwo_x.setRepeatCount(0);
        valueTvTwo_x.setRepeatMode(ValueAnimator.REVERSE);
        valueTvTwo_x.setInterpolator(new DecelerateInterpolator());

        final ValueAnimator valueTvThree_x = ObjectAnimator.ofFloat(leftRightTvThree, "x", leftRightTvThree.getX(), layoutWidth * .9f);

        valueTvThree_x.setDuration(1000);
        valueTvThree_x.setRepeatCount(0);
        valueTvThree_x.setRepeatMode(ValueAnimator.REVERSE);
        valueTvThree_x.setInterpolator(new DecelerateInterpolator());

        final ValueAnimator valueTvFour_x = ObjectAnimator.ofFloat(leftRightTvFour, "x", leftRightTvFour.getX(), layoutWidth * .93f);

        valueTvFour_x.setDuration(1000);
        valueTvFour_x.setRepeatCount(0);
        valueTvFour_x.setRepeatMode(ValueAnimator.REVERSE);
        valueTvFour_x.setInterpolator(new DecelerateInterpolator());

        final ValueAnimator valueTvFive_x = ObjectAnimator.ofFloat(leftRightTvFive, "x", leftRightTvFive.getX(), layoutWidth * .96f);

        valueTvFive_x.setDuration(1000);
        valueTvFive_x.setRepeatCount(0);
        valueTvFive_x.setRepeatMode(ValueAnimator.REVERSE);
        valueTvFive_x.setInterpolator(new DecelerateInterpolator());

        final AnimatorSet animatorSet1 = new AnimatorSet();
        animatorSet1.playSequentially(valueTvOne_x, valueTvTwo_x, valueTvThree_x, valueTvFour_x, valueTvFive_x);

        animatorSet1.start();
        animatorSet1.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                final ValueAnimator valueTvOne_x = ObjectAnimator.ofFloat(leftRightTvOne, "x", leftRightTvOne.getX(), leftRightTvOne.getX() * .01f);

                valueTvOne_x.setDuration(1000);
                valueTvOne_x.setRepeatCount(0);
                valueTvOne_x.setRepeatMode(ValueAnimator.REVERSE);
                valueTvOne_x.setInterpolator(new DecelerateInterpolator());

                final ValueAnimator valueTvTwo_x = ObjectAnimator.ofFloat(leftRightTvTwo, "x", leftRightTvTwo.getX(), leftRightTvTwo.getX() * .04f);

                valueTvTwo_x.setDuration(1000);
                valueTvTwo_x.setRepeatCount(0);
                valueTvTwo_x.setRepeatMode(ValueAnimator.REVERSE);
                valueTvTwo_x.setInterpolator(new DecelerateInterpolator());

                final ValueAnimator valueTvThree_x = ObjectAnimator.ofFloat(leftRightTvThree, "x", leftRightTvThree.getX(), leftRightTvThree.getX() * .07f);

                valueTvThree_x.setDuration(1000);
                valueTvThree_x.setRepeatCount(0);
                valueTvThree_x.setRepeatMode(ValueAnimator.REVERSE);
                valueTvThree_x.setInterpolator(new DecelerateInterpolator());

                final ValueAnimator valueTvFour_x = ObjectAnimator.ofFloat(leftRightTvFour, "x", leftRightTvFour.getX(), leftRightTvFour.getX() * .1f);

                valueTvFour_x.setDuration(1000);
                valueTvFour_x.setRepeatCount(0);
                valueTvFour_x.setRepeatMode(ValueAnimator.REVERSE);
                valueTvFour_x.setInterpolator(new DecelerateInterpolator());

                final ValueAnimator valueTvFive_x = ObjectAnimator.ofFloat(leftRightTvFive, "x", leftRightTvFive.getX(), leftRightTvFive.getX() * .13f);

                valueTvFive_x.setDuration(1000);
                valueTvFive_x.setRepeatCount(0);
                valueTvFive_x.setRepeatMode(ValueAnimator.REVERSE);
                valueTvFive_x.setInterpolator(new DecelerateInterpolator());

                final AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.playSequentially(valueTvFive_x, valueTvFour_x, valueTvThree_x, valueTvTwo_x, valueTvOne_x);

                animatorSet.start();
                animatorSet.addListener(new Animator.AnimatorListener() {

                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        animatorSet1.start();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        leftRightAnimation.setVisibility(View.GONE);
                        leftRightTvOne.setVisibility(View.GONE);
                        leftRightTvTwo.setVisibility(View.GONE);
                        leftRightTvThree.setVisibility(View.GONE);
                        leftRightTvFour.setVisibility(View.GONE);
                        leftRightTvFive.setVisibility(View.GONE);
                    }
                });
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                leftRightAnimation.setVisibility(View.GONE);
                leftRightTvOne.setVisibility(View.GONE);
                leftRightTvTwo.setVisibility(View.GONE);
                leftRightTvThree.setVisibility(View.GONE);
                leftRightTvFour.setVisibility(View.GONE);
                leftRightTvFive.setVisibility(View.GONE);
            }
        });
    }

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
