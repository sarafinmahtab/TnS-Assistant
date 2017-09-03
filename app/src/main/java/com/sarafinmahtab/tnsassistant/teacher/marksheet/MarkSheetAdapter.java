package com.sarafinmahtab.tnsassistant.teacher.marksheet;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.ActivityChooserView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sarafinmahtab.tnsassistant.R;
import com.sarafinmahtab.tnsassistant.teacher.examsetup.CourseCustomize;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arafin on 8/23/2017.
 */

public class MarkSheetAdapter extends RecyclerView.Adapter<MarkSheetAdapter.MarkUpdateViewHolder> {

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
    public void onBindViewHolder(MarkUpdateViewHolder holder, int position) {

        final MarkListItem markListItem = newMarkListItem.get(position);

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

        double addMark = 0, avgMark = 0;
        double mark, percent;

        //Do the Calculation
        for (int i = 0; i < nonAvgArray.size(); i++) {
            if (nonAvgArray.get(i).equals("tt1")) {
                mark = Double.valueOf(markListItem.getTermTest1_Mark());
                percent = Double.valueOf(CourseCustomize.getCustomTT1Percent());

                addMark = addMark + mark * percent / 100;
            } else if (nonAvgArray.get(i).equals("tt2")) {
                mark = Double.valueOf(markListItem.getTermTest2_Mark());
                percent = Double.valueOf(CourseCustomize.getCustomTT2Percent());

                addMark = addMark + mark * percent / 100;
            } else if (nonAvgArray.get(i).equals("presence")) {
                mark = Double.valueOf(markListItem.getAttendanceMark());
                percent = Double.valueOf(CourseCustomize.getCustomAttendancePercent());

                addMark = addMark + mark * percent / 100;
            } else if (nonAvgArray.get(i).equals("viva")) {
                mark = Double.valueOf(markListItem.getVivaMark());
                percent = Double.valueOf(CourseCustomize.getCustomVivaPercent());

                addMark = addMark + mark * percent / 100;
            } else if (nonAvgArray.get(i).equals("final")) {
                mark = Double.valueOf(markListItem.getFinalExamMark());
                percent = Double.valueOf(CourseCustomize.getCustomFinalPercent());

                addMark = addMark + mark * percent / 100;
            }
        }

        for (int i = 0; i < avgArray.size(); i++) {
            if (avgArray.get(i).equals("tt1")) {
                mark = Double.valueOf(markListItem.getTermTest1_Mark());
                percent = Double.valueOf(CourseCustomize.getCustomTT1Percent());

                avgMark = avgMark + mark * percent / 100;
            } else if (avgArray.get(i).equals("tt2")) {
                mark = Double.valueOf(markListItem.getTermTest2_Mark());
                percent = Double.valueOf(CourseCustomize.getCustomTT2Percent());

                avgMark = avgMark + mark * percent / 100;
            } else if (avgArray.get(i).equals("presence")) {
                mark = Double.valueOf(markListItem.getAttendanceMark());
                percent = Double.valueOf(CourseCustomize.getCustomAttendancePercent());

                avgMark = avgMark + mark * percent / 100;
            } else if (avgArray.get(i).equals("viva")) {
                mark = Double.valueOf(markListItem.getVivaMark());
                percent = Double.valueOf(CourseCustomize.getCustomVivaPercent());

                avgMark = avgMark + mark * percent / 100;
            } else if (avgArray.get(i).equals("final")) {
                mark = Double.valueOf(markListItem.getFinalExamMark());
                percent = Double.valueOf(CourseCustomize.getCustomFinalPercent());

                avgMark = avgMark + mark * percent / 100;
            }
        }

        if (avgArray.size() != 0) {
            addMark = addMark + (avgMark / avgArray.size());
        }

        markListItem.setMarksOutOf100(String.valueOf(formatter.format(addMark)));

        final double finalMark = Double.parseDouble(markListItem.getMarksOutOf100());
        int pos;

        holder.marksOutOf100Mark.setText(markListItem.getMarksOutOf100());

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

        GradientDrawable bgShape = (GradientDrawable)holder.circleFinalMarks.getBackground();
        bgShape.mutate();
        bgShape.setColor(randomSelectedColor);

        holder.markUpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, markListItem.getMarkSheetID(), Toast.LENGTH_LONG).show();
            }
        });

        holder.markListRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, markListItem.getCourseRegID(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != newMarkListItem ? newMarkListItem.size() : 0);
    }

    public static class MarkUpdateViewHolder extends RecyclerView.ViewHolder{

        TextView candidateReg;
        TextView termTest1, termTest2, attendance, viva, finalExam;
        TextView termTest1Mark, termTest2Mark, attendanceMark, vivaMark, finalExamMark, marksOutOf100Mark;

        ImageButton markUpdateBtn;

        RelativeLayout markListRelativeLayout, circleFinalMarks;

        public MarkUpdateViewHolder(View itemView) {
            super(itemView);

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
