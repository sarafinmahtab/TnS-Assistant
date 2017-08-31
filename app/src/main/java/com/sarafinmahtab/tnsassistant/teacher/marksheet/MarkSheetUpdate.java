package com.sarafinmahtab.tnsassistant.teacher.marksheet;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
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

public class MarkSheetUpdate extends Fragment {

    MarkSheetActivity markSheetActivity;
    private CourseCustomize courseCustomize;

    String markSheetLoader = ServerAddress.getMyServerAddress().concat("mark_sheet_loader.php");

//    String markSheetLoader = "http://192.168.43.65/TnSAssistant/mark_sheet_loader.php";

    String courseID, teacherID;

    RecyclerView markSheetRecyclerView;
    MarkUpdateAdapter markUpdateAdapter;
    List<MarkListItem> stdMarkList;

    SearchView markSheetSearchView;
    EditText markSheetSearchEditText;
    ImageView markSheetCloseButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_mark_sheet_update, container, false);

        markSheetActivity = (MarkSheetActivity) getActivity();

        courseID = markSheetActivity.getCourseID();
        teacherID = markSheetActivity.getTeacherID();
        courseCustomize = markSheetActivity.getCourseCustomize();

        markSheetSearchView = (SearchView) rootView.findViewById(R.id.frag_searchView_mark_update);
        markSheetSearchEditText = (EditText) rootView.findViewById(R.id.search_src_text);
        markSheetCloseButton = (ImageView) rootView.findViewById(R.id.search_close_btn);

        markSheetRecyclerView = (RecyclerView) rootView.findViewById(R.id.frag_recyclerView_mark_update);
        markSheetRecyclerView.setHasFixedSize(true);
        markSheetRecyclerView.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
        stdMarkList = new ArrayList<>();

        StringRequest stringRequestForStdMarkList = new StringRequest(Request.Method.POST, markSheetLoader, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("mark_sheet_loader");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);

                        MarkListItem markListItem = new MarkListItem();

                        markListItem.setCourseRegID(obj.getString("course_reg_id"));
                        markListItem.setRegNo(obj.getString("registration_no"));
                        markListItem.setMarkSheetID(obj.getString("marksheet_id"));
                        markListItem.setTermTest1_Mark(obj.getString("term_test_1"));
                        markListItem.setTermTest2_Mark(obj.getString("term_test_2"));
                        markListItem.setAttendanceMark(obj.getString("attendance"));
                        markListItem.setVivaMark(obj.getString("viva"));
                        markListItem.setFinalExamMark(obj.getString("final_exam"));
                        markListItem.setMarksOutOf100(obj.getString("marks_out_of_100"));

                        stdMarkList.add(markListItem);
                    }

                    markUpdateAdapter = new MarkUpdateAdapter(stdMarkList, rootView.getContext(), markSheetActivity);
                    markSheetRecyclerView.setAdapter(markUpdateAdapter);

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

                params.put("course_id", courseID);

                return params;
            }
        };

        MySingleton.getMyInstance(rootView.getContext()).addToRequestQueue(stringRequestForStdMarkList);

        markSheetSearchView.onActionViewExpanded();
        markSheetSearchView.setIconified(false);
        markSheetSearchView.setQueryHint("Search by Reg ID or Name");

        if(!markSheetSearchView.isFocused()) {
            markSheetSearchView.clearFocus();
        }

//        currentCourseSearchView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                searchView.setIconified(false);
//            }
//        });

        markSheetSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                markUpdateAdapter.checkQueryFromList(newText.toLowerCase());

//                Toast.makeText(rootView.getContext(), newText, Toast.LENGTH_LONG).show();

                return true;
            }
        });

        markSheetCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Clear the text from EditText view
                markSheetSearchEditText.setText("");

                //Clear query
                markSheetSearchView.setQuery("", false);
                markUpdateAdapter.notifyDataSetChanged();
                markSheetSearchView.clearFocus();
            }
        });

        return rootView;
    }
}
