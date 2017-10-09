package com.sarafinmahtab.tnsassistant.teacher;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.sarafinmahtab.tnsassistant.LoginActivity;
import com.sarafinmahtab.tnsassistant.MySingleton;
import com.sarafinmahtab.tnsassistant.R;
import com.sarafinmahtab.tnsassistant.ServerAddress;
import com.sarafinmahtab.tnsassistant.teacher.courselist.Course;
import com.sarafinmahtab.tnsassistant.teacher.courselist.CourseListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class TeacherActivity extends AppCompatActivity {

    String imageUploadURL = ServerAddress.getMyServerAddress().concat("teacher_pic_upload.php");
    String courseListURL = ServerAddress.getMyServerAddress().concat("generate_courses.php");

    String fullName, teacherID, userID, imageURL;

    TextView loadCourseData;
    TextView name, codeName, designation, deptName, email;
    Button courseLoader, changeTeacherImage, chooseTeacherImage, uploadTeacherImage;

    SimpleDraweeView teacherDisplayPic;
    ImageView teacherImageLoad;

    private int IMG_REQUEST = 1;
    private Bitmap bitmap;
    boolean imageUploaded = false;

    List<Course> listItem;
    CourseListAdapter adapter;
    RecyclerView recyclerView;

    RelativeLayout dotAnimation;
    ObjectAnimator waveOneAnimator, waveTwoAnimator, waveThreeAnimator;
    TextView hangoutTvOne, hangoutTvTwo, hangoutTvThree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_teacher);

        Toolbar myToolbar = findViewById(R.id.activity_teacher_toolbar);
        setSupportActionBar(myToolbar);

        myToolbar.setTitleTextColor(0xFFFFFFFF);

        name = findViewById(R.id.teacher_name);
        codeName = findViewById(R.id.teacher_code);
        designation = findViewById(R.id.designation);
        deptName = findViewById(R.id.dept_name);
        email = findViewById(R.id.email_of_teacher);
        loadCourseData = findViewById(R.id.load_course_data);

        Bundle bundle = getIntent().getExtras();

        teacherID = bundle.getString("teacher_id");
        userID = bundle.getString("user_id");
        fullName = bundle.getString("t_first_name") + " " + bundle.getString("t_last_name");
        imageURL = bundle.getString("display_picture");
        imageURL = ServerAddress.getMyUploadServerAddress().concat(imageURL);

        getSupportActionBar().setTitle(String.format("%s Activity", bundle.getString("t_first_name")));

        name.setText(fullName);
        codeName.setText(String.format("Code Name: %s", bundle.getString("employee_code")));
        designation.setText(bundle.getString("desig_name"));
        deptName.setText(bundle.getString("dept_name"));
        email.setText(bundle.getString("email"));

        //HangOut Animation
        dotAnimation = findViewById(R.id.course_data_dot_animation);
        hangoutTvOne = findViewById(R.id.hangoutTvOne);
        hangoutTvTwo = findViewById(R.id.hangoutTvTwo);
        hangoutTvThree = findViewById(R.id.hangoutTvThree);

        loadCourseData.setVisibility(View.VISIBLE);
        dotAnimation.setVisibility(View.GONE);
        hangoutTvOne.setVisibility(View.GONE);
        hangoutTvTwo.setVisibility(View.GONE);
        hangoutTvThree.setVisibility(View.GONE);

        if(!imageURL.equals("")) {
            loadDisplayImage();
        }

        displayImageUpload();

        courseLoader = findViewById(R.id.course_loader);
        courseLoader.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        loadCourseData.setText(R.string.requesting_running_courses);
                        dotAnimation.setVisibility(View.VISIBLE);
                        hangoutTvOne.setVisibility(View.VISIBLE);
                        hangoutTvTwo.setVisibility(View.VISIBLE);
                        hangoutTvThree.setVisibility(View.VISIBLE);

                        waveAnimation();

                        recyclerView = findViewById(R.id.courses_list);
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(TeacherActivity.this));

                        listItem = new ArrayList<>();

                        loadCourseData();
                    }
                }
        );
    }

    private void loadDisplayImage() {
        teacherDisplayPic = findViewById(R.id.teacher_display_pic);

        Uri uri = Uri.parse(imageURL);
        teacherDisplayPic.setImageURI(uri);
    }

    private void displayImageUpload() {
        changeTeacherImage = findViewById(R.id.change_teacher_image);

        changeTeacherImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(TeacherActivity.this);

                LayoutInflater inflater = LayoutInflater.from(TeacherActivity.this);
                final View customView = inflater.inflate(R.layout.upload_image_dialog_layout, null);

                chooseTeacherImage = customView.findViewById(R.id.teacher_choose_btn);
                chooseTeacherImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectImage();
                    }
                });

                teacherImageLoad = customView.findViewById(R.id.teacher_image_load);

                uploadTeacherImage = customView.findViewById(R.id.teacher_upload_btn);
                uploadTeacherImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        uploadImage();
                    }
                });

                builder.setView(customView).setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if(imageUploaded) {
                            Uri uri = Uri.parse(imageURL);
                            teacherDisplayPic.setImageURI(uri);
                        }
                        dialog.cancel();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    //By Clicking On Choose Button
    private void selectImage() {
        Intent selectImageIntent = new Intent();
        selectImageIntent.setType("image/*");
        selectImageIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(selectImageIntent, "Select Picture"), IMG_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == IMG_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri path = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), path);
                teacherImageLoad.setImageBitmap(bitmap);
            } catch (IOException e) {
                Toast.makeText(TeacherActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }

        } else {
            Toast.makeText(TeacherActivity.this, "No Image is selected!!", Toast.LENGTH_LONG).show();
        }
    }

    //By Clicking On Upload Button
    private void uploadImage() {
        StringRequest imageUploadStringRequest = new StringRequest(Request.Method.POST, imageUploadURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        imageUploaded = true;
                        Toast.makeText(TeacherActivity.this, response, Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(TeacherActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new Hashtable<>();

                params.put("teacher_display_image", imageToString(bitmap));

                String imageNameGenerator = "";
                char ch;
                int id = Integer.parseInt(userID);

                while(id > 0)
                {
                    ch = (char) ('A'+id%26);
                    imageNameGenerator += ch;
                    id/=26;
                }

                params.put("teacher_display_image_name", imageNameGenerator);
                params.put("teacher_id", teacherID);

                return params;
            }
        };

        MySingleton.getMyInstance(TeacherActivity.this).addToRequestQueue(imageUploadStringRequest);
    }

    private String imageToString(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imgBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgBytes, Base64.DEFAULT);
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

    //By Clicking On Load Button
    private void loadCourseData() {
        StringRequest stringRequestJSONArray = new StringRequest(Request.Method.POST, courseListURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("course_list");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        Course courses = new Course(teacherID,
                                obj.getString("course_id"),
                                obj.getString("course_code"),
                                obj.getString("course_title"),
                                obj.getString("credit"),
                                obj.getString("session"));
                        listItem.add(courses);
                    }

                    loadCourseData.setVisibility(View.GONE);
                    dotAnimation.setVisibility(View.GONE);
                    hangoutTvOne.setVisibility(View.GONE);
                    hangoutTvTwo.setVisibility(View.GONE);
                    hangoutTvThree.setVisibility(View.GONE);

                    adapter = new CourseListAdapter(listItem, TeacherActivity.this);
                    recyclerView.setAdapter(adapter);
                } catch (JSONException e) {
                    loadCourseData.setVisibility(View.VISIBLE);
                    dotAnimation.setVisibility(View.GONE);
                    hangoutTvOne.setVisibility(View.GONE);
                    hangoutTvTwo.setVisibility(View.GONE);
                    hangoutTvThree.setVisibility(View.GONE);

                    loadCourseData.setText(e.getMessage());
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadCourseData.setVisibility(View.VISIBLE);
                dotAnimation.setVisibility(View.GONE);
                hangoutTvOne.setVisibility(View.GONE);
                hangoutTvTwo.setVisibility(View.GONE);
                hangoutTvThree.setVisibility(View.GONE);

                loadCourseData.setText(R.string.no_network_status);

                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("instructor_id", teacherID);
                return params;
            }
        };

        MySingleton.getMyInstance(TeacherActivity.this).addToRequestQueue(stringRequestJSONArray);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.logout_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                startActivity(new Intent(TeacherActivity.this, LoginActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
