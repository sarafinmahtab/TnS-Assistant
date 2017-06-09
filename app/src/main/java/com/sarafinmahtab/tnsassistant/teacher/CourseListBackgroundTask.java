package com.sarafinmahtab.tnsassistant.teacher;

import android.app.DownloadManager;
import android.content.Context;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.RequiresApi;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.sarafinmahtab.tnsassistant.MySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Arafin on 6/9/2017.
 */

public class CourseListBackgroundTask {
    private Context context;
    private ArrayList<Course> arrayList;
    private String teacher_id;

    public CourseListBackgroundTask(Context context, String teacher_id) {
        this.context = context;
        this.teacher_id = teacher_id;
        arrayList = new ArrayList<>();
    }

    public ArrayList<Course> getCourseList() {

        String course_array_url = "http://192.168.0.150/TnSAssistant/generate_courses.php";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, course_array_url, (String)null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        int count = 0;
                        while (count < response.length()) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(count);
                                Course course = new Course(jsonObject.getString("course_id"), jsonObject.getString("course_code"),
                                        jsonObject.getString("course_title"), jsonObject.getString("credit"), jsonObject.getString("session"));
                                arrayList.add(course);
                                count++;
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Error Occured!!", Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("teacher_id", teacher_id);
                return params;
            }
        };

        MySingleton.getMyInstance(context).addToRequestQueue(jsonArrayRequest);
        return arrayList;
    }
}
