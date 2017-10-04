package com.sarafinmahtab.tnsassistant.student;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.facebook.drawee.view.SimpleDraweeView;
import com.sarafinmahtab.tnsassistant.LoginActivity;
import com.sarafinmahtab.tnsassistant.MySingleton;
import com.sarafinmahtab.tnsassistant.R;
import com.sarafinmahtab.tnsassistant.ServerAddress;
import com.sarafinmahtab.tnsassistant.student.stdcourses.AllCoursesActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

public class StudentActivity extends AppCompatActivity {

    String stdImageUploadURL = ServerAddress.getMyServerAddress().concat("student_pic_upload.php");

    String name, stdID, userID, stdImageURL;

    TextView stdName, stdEmail, regID, deptName;
    Bundle bundle;

    private int IMG_REQUEST = 1;
    private Bitmap bitmap;
    boolean imageUploaded = false;

    Button changeStudentImage, chooseStudentImage, uploadStudentImage;
    SimpleDraweeView stdDisplayPic;
    ImageView stdImageLoad;

    //Grid ImageButtons
    ImageButton allCoursesBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        bundle = getIntent().getExtras();

        Toolbar myToolbar = findViewById(R.id.activity_student_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle(bundle.getString("s_first_name") + " Activity");

        myToolbar.setTitleTextColor(0xFFFFFFFF);

        stdName = findViewById(R.id.student_name);
        stdEmail = findViewById(R.id.email_of_student);
        regID = findViewById(R.id.reg_std);
        deptName = findViewById(R.id.dept_std);

        allCoursesBtn = findViewById(R.id.all_courses);

        name = bundle.getString("s_first_name") + " " + bundle.getString("s_last_name");
        stdID = bundle.getString("student_id");
        stdImageURL = bundle.getString("std_display_picture");
        stdImageURL = ServerAddress.getMyUploadServerAddress().concat(stdImageURL);
        userID = bundle.getString("user_id");

        stdName.setText(name);
        stdEmail.setText(bundle.getString("email_no"));
        regID.setText(bundle.getString("registration_no"));
        deptName.setText(bundle.getString("dept_name"));

        if(!stdImageURL.equals("")) {
            loadDisplayImage();
        }

        displayImageUpload();

        allCoursesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentActivity.this, AllCoursesActivity.class);

                Bundle allCoursesBundle = new Bundle();
                allCoursesBundle.putString("student_id", stdID);
                intent.putExtras(allCoursesBundle);

                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
    }

    private void loadDisplayImage() {
        stdDisplayPic = findViewById(R.id.student_display_pic);

        Uri uri = Uri.parse(stdImageURL);
        stdDisplayPic.setImageURI(uri);
    }

    private void displayImageUpload() {
        changeStudentImage = findViewById(R.id.change_student_image);

        changeStudentImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(StudentActivity.this);

                LayoutInflater inflater = LayoutInflater.from(StudentActivity.this);
                final View customView2 = inflater.inflate(R.layout.upload_img_dialog_layout, null);

                chooseStudentImage = customView2.findViewById(R.id.student_choose_btn);
                chooseStudentImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectImage();
                    }
                });

                stdImageLoad = customView2.findViewById(R.id.student_image_load);

                uploadStudentImage = customView2.findViewById(R.id.student_upload_btn);
                uploadStudentImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        uploadImage();
                    }
                });

                builder.setView(customView2).setNegativeButton("Close", new DialogInterface.OnClickListener() {
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

        if(requestCode == IMG_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri path = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), path);
                stdImageLoad.setImageBitmap(bitmap);
            } catch (IOException e) {
                Toast.makeText(StudentActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        } else {
            Toast.makeText(StudentActivity.this, "No Image is selected!!", Toast.LENGTH_LONG).show();
        }
    }

    //By Clicking On Upload Button
    private void uploadImage() {
        final ProgressDialog uploadProgressDialog2 = ProgressDialog.show(this,"Uploading...", "Please wait...", false, false);

        StringRequest imageUploadStringRequest = new StringRequest(Request.Method.POST, stdImageUploadURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        uploadProgressDialog2.dismiss();
                        imageUploaded = true;
                        Toast.makeText(StudentActivity.this, response, Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                uploadProgressDialog2.dismiss();
                Toast.makeText(StudentActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new Hashtable<>();

                params.put("student_display_image", imageToString(bitmap));

                String imageNameGenerator = "";
                char ch;
                int id = Integer.parseInt(userID);

                while(id > 0)
                {
                    ch = (char) ('A'+id%26);
                    imageNameGenerator += ch;
                    id/=26;
                }

                params.put("student_display_image_name", imageNameGenerator);
                params.put("student_id", stdID);

                return params;
            }
        };

        MySingleton.getMyInstance(StudentActivity.this).addToRequestQueue(imageUploadStringRequest);
    }

    private String imageToString(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imgBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgBytes, Base64.DEFAULT);
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
                startActivity(new Intent(StudentActivity.this, LoginActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
