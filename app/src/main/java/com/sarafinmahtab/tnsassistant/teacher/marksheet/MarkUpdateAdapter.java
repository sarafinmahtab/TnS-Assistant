package com.sarafinmahtab.tnsassistant.teacher.marksheet;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sarafinmahtab.tnsassistant.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Arafin on 8/23/2017.
 */

public class MarkUpdateAdapter extends RecyclerView.Adapter<MarkUpdateAdapter.MarkUpdateViewHolder> {

    private List<MarkListItem> markListItem;
    private Context context;

    private ArrayList<MarkListItem> newMarkListItem;

    private CourseCustomize courseCustomize;

    public MarkUpdateAdapter(List<MarkListItem> markListItem, Context context, CourseCustomize courseCustomize) {
        this.markListItem = markListItem;
        this.context = context;

        newMarkListItem = new ArrayList<>();
        newMarkListItem.addAll(this.markListItem);
        this.courseCustomize = courseCustomize;
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

        holder.termTest1.setText(courseCustomize.getCustomTT1Name());
        holder.termTest1Mark.setText(markListItem.getTermTest1_Mark());

        holder.termTest2.setText(courseCustomize.getCustomTT2Name());
        holder.termTest1Mark.setText(markListItem.getTermTest2_Mark());

        holder.attendance.setText(courseCustomize.getCustomAttendanceName());
        holder.attendanceMark.setText(markListItem.getAttendanceMark());

        holder.viva.setText(courseCustomize.getCustomVivaName());
        holder.vivaMark.setText(markListItem.getVivaMark());

        holder.finalExam.setText(courseCustomize.getCustomFinalName());
        holder.finalExamMark.setText(markListItem.getFinalExamMark());

        //Can Do the Calculation
        holder.marksOutOf100Mark.setText(markListItem.getMarksOutOf100());
        int[] randomColors = context.getResources().getIntArray(R.array.gpa_colors);

        //Selects random colors from color Array
        int randomSelectedColor = randomColors[new Random().nextInt(randomColors.length)];

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
