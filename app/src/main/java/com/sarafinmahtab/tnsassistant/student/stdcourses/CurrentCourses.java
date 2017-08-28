package com.sarafinmahtab.tnsassistant.student.stdcourses;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.sarafinmahtab.tnsassistant.MySingleton;
import com.sarafinmahtab.tnsassistant.R;
import com.sarafinmahtab.tnsassistant.ServerAddress;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CurrentCourses extends Fragment {

    String currentCoursesListURL = ServerAddress.getMyServerAddress().concat("all_courses_list.php");
//    String currentCoursesListURL = "http://192.168.43.65/TnSAssistant/all_courses_list.php";

    String studentID;
    int isCompleted = 0;

    RecyclerView currentCourseRecyclerView;
    CurrentCoursesAdapter currentCoursesAdapter;
    List<CourseItem> currentCoursesList;

    SearchView currentCourseSearchView;
    EditText searchEditText;
    ImageView closeButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_current_courses, container, false);

        AllCoursesActivity allCoursesActivity = (AllCoursesActivity) getActivity();
        studentID = allCoursesActivity.getStudentID();

        // Inflate the layout for this fragment
        currentCourseSearchView = (SearchView) rootView.findViewById(R.id.frag_searchView_current);
        searchEditText = (EditText) rootView.findViewById(R.id.search_src_text);
        closeButton = (ImageView) rootView.findViewById(R.id.search_close_btn);

        currentCourseRecyclerView = (RecyclerView) rootView.findViewById(R.id.frag_recyclerView_current);

        currentCourseRecyclerView.setHasFixedSize(true);
        currentCourseRecyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
        currentCoursesList = new ArrayList<>();

        StringRequest stringRequestForCurrentCoursesList = new StringRequest(Request.Method.POST, currentCoursesListURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("all_courses_list");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);

                        CourseItem courseItem = new CourseItem(
                                obj.getString("course_id"),
                                obj.getString("course_code"),
                                obj.getString("course_title"),
                                obj.getString("credit"),
                                obj.getString("semester")
                        );

                        currentCoursesList.add(courseItem);
                    }

                    currentCoursesAdapter = new CurrentCoursesAdapter(currentCoursesList, rootView.getContext());
                    currentCourseRecyclerView.setAdapter(currentCoursesAdapter);

                } catch (JSONException e) {
                    Toast.makeText(rootView.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(rootView.getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("student_id", studentID);
                params.put("is_completed", String.valueOf(isCompleted));

                return params;
            }
        };

        MySingleton.getMyInstance(rootView.getContext()).addToRequestQueue(stringRequestForCurrentCoursesList);

        currentCourseSearchView.onActionViewExpanded();
        currentCourseSearchView.setIconified(false);
        currentCourseSearchView.setQueryHint("Search by Course Code or Title");

        if(!currentCourseSearchView.isFocused()) {
            currentCourseSearchView.clearFocus();
        }

//        currentCourseSearchView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                searchView.setIconified(false);
//            }
//        });

        currentCourseSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                currentCoursesAdapter.checkQueryFromList(newText.toLowerCase());

//                Toast.makeText(rootView.getContext(), newText, Toast.LENGTH_LONG).show();

                return true;
            }
        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Clear the text from EditText view
                searchEditText.setText("");

                //Clear query
                currentCourseSearchView.setQuery("", false);
                currentCoursesAdapter.notifyDataSetChanged();
                currentCourseSearchView.clearFocus();
            }
        });

        return rootView;
    }
}
