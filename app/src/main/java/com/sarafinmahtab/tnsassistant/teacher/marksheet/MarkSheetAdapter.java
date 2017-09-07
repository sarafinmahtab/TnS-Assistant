package com.sarafinmahtab.tnsassistant.teacher.marksheet;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.ActivityChooserView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.sarafinmahtab.tnsassistant.teacher.examsetup.CourseCustomize;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Arafin on 8/23/2017.
 */

public class MarkSheetAdapter extends RecyclerView.Adapter<MarkSheetAdapter.MarkUpdateViewHolder> {

    private String resultUpdateURL = ServerAddress.getMyServerAddress().concat("marksheet_update.php");

    private List<MarkListItem> markListItem;
    private Context context;

    private ArrayList<MarkListItem> newMarkListItem;

    private ArrayList<String> nonAvgArray, avgArray;

    private final NumberFormat formatter = new DecimalFormat("#0");

    public MarkSheetAdapter(List<MarkListItem> markListItem, Context context) {
        this.markListItem = markListItem;
        this.context = context;

        newMarkListItem = new ArrayList<>();
        newMarkListItem.addAll(this.markListItem);

        nonAvgArray = new ArrayList<>();
        avgArray = new ArrayList<>();

        boolean[] checkedAvgArray = CourseCustomize.getCheckedAvgArray();

        if(checkedAvgArray[0]) {
            avgArray.add("tt1");
        } else {
            nonAvgArray.add("tt1");
        }

        if(checkedAvgArray[1]) {
            avgArray.add("tt2");
        } else {
            nonAvgArray.add("tt2");
        }

        if(checkedAvgArray[2]) {
            avgArray.add("presence");
        } else {
            nonAvgArray.add("presence");
        }

        if(checkedAvgArray[3]) {
            avgArray.add("viva");
        } else {
            nonAvgArray.add("viva");
        }

        if(checkedAvgArray[4]) {
            avgArray.add("final");
        } else {
            nonAvgArray.add("final");
        }
    }

    @Override
    public MarkUpdateViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.mark_update_item, parent, false);

        return new MarkUpdateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MarkUpdateViewHolder holder, int position) {

        final MarkListItem markListItem = newMarkListItem.get(position);

        holder.updateStatus.setVisibility(View.GONE);
        holder.candidateReg.setText(String.format("Reg ID - %s", markListItem.getRegNo()));

        holder.termTest1.setText(CourseCustomize.getCustomTT1Name());
        holder.termTest1Mark.setText(markListItem.getTermTest1_Mark());

        holder.termTest2.setText(CourseCustomize.getCustomTT2Name());
        holder.termTest2Mark.setText(markListItem.getTermTest2_Mark());

        holder.attendance.setText(CourseCustomize.getCustomAttendanceName());
        holder.attendanceMark.setText(markListItem.getAttendanceMark());

        holder.viva.setText(CourseCustomize.getCustomVivaName());
        holder.vivaMark.setText(markListItem.getVivaMark());

        holder.finalExam.setText(CourseCustomize.getCustomFinalName());
        holder.finalExamMark.setText(markListItem.getFinalExamMark());

        holder.marksOutOf100Mark.setText(markListItem.getMarksOutOf100());

        final double finalMark = Double.parseDouble(markListItem.getMarksOutOf100());
        int pos;

        if(finalMark >= 80) {
            pos = 0;
        } else if(finalMark >= 70 && finalMark <= 79) {
            pos = 1;
        } else if(finalMark >= 60 && finalMark <= 69) {
            pos = 2;
        } else if(finalMark >= 50 && finalMark <= 59) {
            pos = 3;
        } else if(finalMark >= 40 && finalMark <= 49) {
            pos = 4;
        } else if(finalMark < 40){
            pos = 5;
        } else {
            pos = 6;
        }

        int[] randomColors = context.getResources().getIntArray(R.array.gpa_colors);

        //Selects random colors from color Array
        int randomSelectedColor = randomColors[pos];

        //Maintains the sorted colors from color Array
//        int randomSelectedColor = randomColors[position];

        GradientDrawable bgShape = (GradientDrawable) holder.circleFinalMarks.getBackground();
        bgShape.mutate();
        bgShape.setColor(randomSelectedColor);

        holder.markListRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, markListItem.getCourseRegID(), Toast.LENGTH_LONG).show();
            }
        });

        holder.markUpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = LayoutInflater.from(context).inflate(R.layout.result_update_dialog, null);

                final int width = ViewGroup.LayoutParams.MATCH_PARENT, height = ViewGroup.LayoutParams.WRAP_CONTENT;
                final Dialog bottomSheetDialog = new Dialog(context, R.style.MaterialDialogSheet);
                bottomSheetDialog.setContentView(view);
                bottomSheetDialog.setCancelable(false);

                if(bottomSheetDialog.getWindow() != null) {
                    bottomSheetDialog.getWindow().setLayout(width, height);
                    bottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);
                    bottomSheetDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
                    bottomSheetDialog.show ();
                }

                final Button updateBtn = (Button) view.findViewById(R.id.update_btn_dialog);
                final ImageButton closeBtn = (ImageButton) view.findViewById(R.id.result_dialog_close);

                final TextView regText = (TextView) view.findViewById(R.id.reg_id_dialog);
                regText.setText(String.format("Reg ID: %s", markListItem.getRegNo()));

                final TextView textViewTT1 = (TextView) view.findViewById(R.id.tt1_result_update);
                textViewTT1.setText(CourseCustomize.getCustomTT1Name());
                final EditText textTT1 = (EditText) view.findViewById(R.id.tt1_result_edit);
                textTT1.setText(markListItem.getTermTest1_Mark());

                final TextView textViewTT2 = (TextView) view.findViewById(R.id.tt2_result_update);
                textViewTT2.setText(CourseCustomize.getCustomTT2Name());
                final EditText textTT2 = (EditText) view.findViewById(R.id.tt2_result_edit);
                textTT2.setText(markListItem.getTermTest2_Mark());

                final TextView textViewAttendance = (TextView) view.findViewById(R.id.attendance_result_update);
                textViewAttendance.setText(CourseCustomize.getCustomAttendanceName());
                final EditText textAttendance = (EditText) view.findViewById(R.id.attendance_result_edit);
                textAttendance.setText(markListItem.getAttendanceMark());

                final TextView textViewViva = (TextView) view.findViewById(R.id.viva_result_update);
                textViewViva.setText(CourseCustomize.getCustomVivaName());
                final EditText textViva = (EditText) view.findViewById(R.id.viva_result_edit);
                textViva.setText(markListItem.getVivaMark());

                final TextView textViewFinal = (TextView) view.findViewById(R.id.final_result_update);
                textViewFinal.setText(CourseCustomize.getCustomFinalName());
                final EditText textFinal = (EditText) view.findViewById(R.id.final_result_edit);
                textFinal.setText(markListItem.getFinalExamMark());

                updateBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        StringRequest updateResultRequest = new StringRequest(Request.Method.POST, resultUpdateURL, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                switch (response) {
                                    case "success":
                                        bottomSheetDialog.cancel();
                                        holder.updateStatus.setVisibility(View.VISIBLE);
                                        holder.updateStatus.setText(R.string.updated);

                                        holder.termTest1Mark.setText(markListItem.getTermTest1_Mark());
                                        holder.termTest2Mark.setText(markListItem.getTermTest2_Mark());
                                        holder.attendanceMark.setText(markListItem.getAttendanceMark());
                                        holder.vivaMark.setText(markListItem.getVivaMark());
                                        holder.finalExamMark.setText(markListItem.getFinalExamMark());
                                        holder.marksOutOf100Mark.setText(markListItem.getMarksOutOf100());

                                        final double finalMark = Double.parseDouble(markListItem.getMarksOutOf100());
                                        int pos;

                                        if (finalMark >= 80) {
                                            pos = 0;
                                        } else if (finalMark >= 70 && finalMark <= 79) {
                                            pos = 1;
                                        } else if (finalMark >= 60 && finalMark <= 69) {
                                            pos = 2;
                                        } else if (finalMark >= 50 && finalMark <= 59) {
                                            pos = 3;
                                        } else if (finalMark >= 40 && finalMark <= 49) {
                                            pos = 4;
                                        } else if (finalMark < 40) {
                                            pos = 5;
                                        } else {
                                            pos = 6;
                                        }

                                        int[] randomColors = context.getResources().getIntArray(R.array.gpa_colors);
                                        int randomSelectedColor = randomColors[pos];

                                        GradientDrawable bgShape = (GradientDrawable) holder.circleFinalMarks.getBackground();
                                        bgShape.mutate();
                                        bgShape.setColor(randomSelectedColor);

                                        Toast.makeText(context, "Result Updated Successfully", Toast.LENGTH_LONG).show();
                                        break;
                                    case "failed":
                                        Toast.makeText(context, "Result Update Failed", Toast.LENGTH_LONG).show();
                                        break;
                                    default:
                                        Toast.makeText(context, "An Error Occurred", Toast.LENGTH_LONG).show();
                                        break;
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
                                error.printStackTrace();
                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<>();

                                params.put("course_reg_id", markListItem.getCourseRegID());

                                markListItem.setTermTest1_Mark(textTT1.getText().toString());
                                params.put("term_test_1", markListItem.getTermTest1_Mark());

                                markListItem.setTermTest2_Mark(textTT2.getText().toString());
                                params.put("term_test_2", markListItem.getTermTest2_Mark());

                                markListItem.setAttendanceMark(textAttendance.getText().toString());
                                params.put("attendance", markListItem.getAttendanceMark());

                                markListItem.setVivaMark(textViva.getText().toString());
                                params.put("viva", markListItem.getVivaMark());

                                markListItem.setFinalExamMark(textFinal.getText().toString());
                                params.put("final_exam", markListItem.getFinalExamMark());

                                String markOutOf100Final = doTheCalculation(markListItem.getTermTest1_Mark(),
                                        markListItem.getTermTest2_Mark(), markListItem.getAttendanceMark(), markListItem.getVivaMark(),
                                        markListItem.getFinalExamMark());

                                markListItem.setMarksOutOf100(markOutOf100Final);
                                params.put("marks_out_of_100", markListItem.getMarksOutOf100());

                                return params;
                            }
                        };

                        MySingleton.getMyInstance(context).addToRequestQueue(updateResultRequest);
                    }
                });

                closeBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        bottomSheetDialog.cancel();
                    }
                });
            }
        });
    }

    private String doTheCalculation(String newTT1, String newTT2, String newAttendance, String newViva, String newFinal) {
        double addMark = 0, avgMark = 0;
        double mark, percent;

        Log.d("Query",  newTT1 + " " + newTT2 + " " + newAttendance + " " + newViva + " " + newFinal);

        for (int i = 0; i < nonAvgArray.size(); i++) {
            if (nonAvgArray.get(i).equals("tt1")) {
                mark = Double.valueOf(newTT1);
                percent = Double.valueOf(CourseCustomize.getCustomTT1Percent());

                addMark = addMark + mark * percent / 100;
            } else if (nonAvgArray.get(i).equals("tt2")) {
                mark = Double.valueOf(newTT2);
                percent = Double.valueOf(CourseCustomize.getCustomTT2Percent());

                addMark = addMark + mark * percent / 100;
            } else if (nonAvgArray.get(i).equals("presence")) {
                mark = Double.valueOf(newAttendance);
                percent = Double.valueOf(CourseCustomize.getCustomAttendancePercent());

                addMark = addMark + mark * percent / 100;
            } else if (nonAvgArray.get(i).equals("viva")) {
                mark = Double.valueOf(newViva);
                percent = Double.valueOf(CourseCustomize.getCustomVivaPercent());

                addMark = addMark + mark * percent / 100;
            } else if (nonAvgArray.get(i).equals("final")) {
                mark = Double.valueOf(newFinal);
                percent = Double.valueOf(CourseCustomize.getCustomFinalPercent());

                addMark = addMark + mark * percent / 100;
            }
        }

        for (int i = 0; i < avgArray.size(); i++) {
            if (avgArray.get(i).equals("tt1")) {
                mark = Double.valueOf(newTT1);
                percent = Double.valueOf(CourseCustomize.getCustomTT1Percent());

                avgMark = avgMark + mark * percent / 100;
            } else if (avgArray.get(i).equals("tt2")) {
                mark = Double.valueOf(newTT2);
                percent = Double.valueOf(CourseCustomize.getCustomTT2Percent());

                avgMark = avgMark + mark * percent / 100;
            } else if (avgArray.get(i).equals("presence")) {
                mark = Double.valueOf(newAttendance);
                percent = Double.valueOf(CourseCustomize.getCustomAttendancePercent());

                avgMark = avgMark + mark * percent / 100;
            } else if (avgArray.get(i).equals("viva")) {
                mark = Double.valueOf(newViva);
                percent = Double.valueOf(CourseCustomize.getCustomVivaPercent());

                avgMark = avgMark + mark * percent / 100;
            } else if (avgArray.get(i).equals("final")) {
                mark = Double.valueOf(newFinal);
                percent = Double.valueOf(CourseCustomize.getCustomFinalPercent());

                avgMark = avgMark + mark * percent / 100;
            }
        }

        if (avgArray.size() != 0) {
            addMark = addMark + (avgMark / avgArray.size());
        }

        return String.valueOf(formatter.format(addMark));
    }

    @Override
    public int getItemCount() {
        return (null != newMarkListItem ? newMarkListItem.size() : 0);
    }

    public static class MarkUpdateViewHolder extends RecyclerView.ViewHolder{

        TextView updateStatus, candidateReg;
        TextView termTest1, termTest2, attendance, viva, finalExam;
        TextView termTest1Mark, termTest2Mark, attendanceMark, vivaMark, finalExamMark, marksOutOf100Mark;

        ImageButton markUpdateBtn;

        RelativeLayout markListRelativeLayout, circleFinalMarks;

        public MarkUpdateViewHolder(View itemView) {
            super(itemView);

            updateStatus = (TextView) itemView.findViewById(R.id.update_status);
            candidateReg = (TextView) itemView.findViewById(R.id.candidate_reg);

            termTest1 = (TextView) itemView.findViewById(R.id.term_test1);
            termTest2 = (TextView) itemView.findViewById(R.id.term_test2);
            attendance = (TextView) itemView.findViewById(R.id.attendance);
            viva = (TextView) itemView.findViewById(R.id.viva);
            finalExam = (TextView) itemView.findViewById(R.id.final_exam);

            termTest1Mark = (TextView) itemView.findViewById(R.id.term_test1_marks);
            termTest2Mark = (TextView) itemView.findViewById(R.id.term_test2_marks);
            attendanceMark = (TextView) itemView.findViewById(R.id.attendance_marks);
            vivaMark = (TextView) itemView.findViewById(R.id.viva_marks);
            finalExamMark = (TextView) itemView.findViewById(R.id.final_exam_marks);

            circleFinalMarks = (RelativeLayout) itemView.findViewById(R.id.circle_final_marks);
            marksOutOf100Mark = (TextView) itemView.findViewById(R.id.final_marks_oval_text);

            markListRelativeLayout = (RelativeLayout) itemView.findViewById(R.id.mark_update_linear);
            markUpdateBtn = (ImageButton) itemView.findViewById(R.id.mark_update_btn);
        }
    }

    public void checkQueryFromList(final String query) {

        int i, j, m;
        newMarkListItem.clear();

        for(i = 0; i < markListItem.size(); i++)
        {
//				int k = 0;

            for(j = 0; j < markListItem.get(i).getRegNo().length(); j++)
            {
                if(j+query.length() > markListItem.get(i).getRegNo().length()) {
                    break;
                }

                boolean ck = true;

                for(m = 0; m < query.length(); m++)
                {
                    if(query.charAt(m) != markListItem.get(i).getRegNo().toLowerCase().charAt(j+m)) {
                        ck = false;
                    }
                }

                if(ck) {
                    newMarkListItem.add(markListItem.get(i));
                    break;
                }
            }
        }

        notifyDataSetChanged();
    }
}
