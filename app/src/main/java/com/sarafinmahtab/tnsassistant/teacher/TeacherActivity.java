package com.sarafinmahtab.tnsassistant.teacher;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
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

//    String imageUploadURL = "http://192.168.43.65/TnSAssistant/teacher_pic_upload.php";
//    String courseListURL = "http://192.168.43.65/TnSAssistant/generate_courses.php";

    String fullName, teacherID, userID, imageURL;

    TextView name, codeName, designation, deptName, email;
    Button courseLoader, changeTeacherImage, chooseTeacherImage, uploadTeacherImage;

    ImageView teacherDisplayPic, teacherImageLoad;

    ProgressDialog loadingDialog;

    private int IMG_REQUEST = 1;
    private Bitmap bitmap;
    boolean imageUploaded = false;

    List<Course> listItem;
    CourseListAdapter adapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.activity_teacher_toolbar);
        setSupportActionBar(myToolbar);

        myToolbar.setTitleTextColor(0xFFFFFFFF);

        name = (TextView) findViewById(R.id.teacher_name);
        codeName = (TextView) findViewById(R.id.teacher_code);
        designation = (TextView) findViewById(R.id.designation);
        deptName = (TextView) findViewById(R.id.dept_name);
        email = (TextView) findViewById(R.id.email_of_teacher);

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

        if(!imageURL.equals("")) {
            loadingDialog = ProgressDialog.show(this, "Please wait", "Loading " + bundle.getString("t_first_name") + " data", false, false);

            loadDisplayImage();
        }

        displayImageUpload();

        courseLoader = (Button) findViewById(R.id.course_loader);
        courseLoader.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        recyclerView = (RecyclerView) findViewById(R.id.cousres_list);
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(TeacherActivity.this));

                        listItem = new ArrayList<>();

                        loadCourseData();
                    }
                }
        );
    }

    private void loadDisplayImage() {
        teacherDisplayPic = (ImageView) findViewById(R.id.teacher_display_pic);

        ImageRequest imageLoadRequest = new ImageRequest(imageURL, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                loadingDialog.dismiss();
                teacherDisplayPic.setImageBitmap(response);
            }
        }, 0, 0, ImageView.ScaleType.CENTER_INSIDE, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loadingDialog.dismiss();
                Toast.makeText(TeacherActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        });

        MySingleton.getMyInstance(TeacherActivity.this).addToRequestQueue(imageLoadRequest);
    }

    private void displayImageUpload() {
        changeTeacherImage = (Button) findViewById(R.id.change_teacher_image);

        changeTeacherImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(TeacherActivity.this);

                LayoutInflater inflater = LayoutInflater.from(TeacherActivity.this);
                final View customView = inflater.inflate(R.layout.upload_image_dialog_layout, null);

                chooseTeacherImage = (Button) customView.findViewById(R.id.teacher_choose_btn);
                chooseTeacherImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectImage();
                    }
                });

                teacherImageLoad = (ImageView) customView.findViewById(R.id.teacher_image_load);

                uploadTeacherImage = (Button) customView.findViewById(R.id.teacher_upload_btn);
                uploadTeacherImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        uploadImage();
                    }
                });

                builder.setView(customView).setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if(imageUploaded) {
                            loadDisplayImage();
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
        final ProgressDialog uploadProgressDialog = ProgressDialog.show(this,"Uploading...", "Please wait...", false, false);

        StringRequest imageUploadStringRequest = new StringRequest(Request.Method.POST, imageUploadURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        uploadProgressDialog.dismiss();
                        imageUploaded = true;
                        Toast.makeText(TeacherActivity.this, response, Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        uploadProgressDialog.dismiss();
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

    //By Clicking On Load Button
    private void loadCourseData() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Courses!!");
        progressDialog.show();

        StringRequest stringRequestJSONArray = new StringRequest(Request.Method.POST, courseListURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();

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

                    adapter = new CourseListAdapter(listItem, TeacherActivity.this);
                    recyclerView.setAdapter(adapter);
                } catch (JSONException e) {
                    Toast.makeText(TeacherActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(TeacherActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
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
