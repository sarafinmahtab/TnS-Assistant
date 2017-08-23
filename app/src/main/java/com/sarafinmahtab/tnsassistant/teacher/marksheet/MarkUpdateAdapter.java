package com.sarafinmahtab.tnsassistant.teacher.marksheet;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.sarafinmahtab.tnsassistant.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arafin on 8/23/2017.
 */

public class MarkUpdateAdapter extends RecyclerView.Adapter<MarkUpdateAdapter.MarkUpdateViewHolder> {

    private List<MarkListItem> markListItem;
    private Context context;

    private ArrayList<MarkListItem> newMarkListItem;

    private ExamType examType;

    public MarkUpdateAdapter(List<MarkListItem> markListItem, Context context, ExamType examType) {
        this.markListItem = markListItem;
        this.context = context;

        newMarkListItem = new ArrayList<>();
        newMarkListItem.addAll(this.markListItem);
        this.examType = examType;
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
        holder.termTest1.setText(examType.getTest1() + ": " + markListItem.getTermTest1_Mark());
        holder.termTest2.setText(examType.getTest2() + ": " + markListItem.getTermTest2_Mark());
        holder.attendance.setText(examType.getAttendance() + ": " + markListItem.getAttendanceMark());
        holder.viva.setText(examType.getViva() + ": " + markListItem.getVivaMark());
        holder.finalExam.setText(examType.getFinal_exam() + ": " + markListItem.getFinalExamMark());
        holder.marksOutOf100.setText(String.format("Marks in Total: %s", markListItem.getMarksOutOf100()));

        holder.markUpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, markListItem.getMarkSheetID(), Toast.LENGTH_LONG).show();
            }
        });

        holder.markListConstraintLayout.setOnClickListener(new View.OnClickListener() {
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

        TextView candidateReg, termTest1, termTest2, attendance, viva, finalExam, marksOutOf100;
        ImageButton markUpdateBtn;

        ConstraintLayout markListConstraintLayout;

        public MarkUpdateViewHolder(View itemView) {
            super(itemView);

            candidateReg = (TextView) itemView.findViewById(R.id.candidate_reg);
            termTest1 = (TextView) itemView.findViewById(R.id.term_test1);
            termTest2 = (TextView) itemView.findViewById(R.id.term_test2);
            attendance = (TextView) itemView.findViewById(R.id.attendance);
            viva = (TextView) itemView.findViewById(R.id.viva);
            finalExam = (TextView) itemView.findViewById(R.id.final_exam);
            marksOutOf100 = (TextView) itemView.findViewById(R.id.marks_out_100);

            markUpdateBtn = (ImageButton) itemView.findViewById(R.id.mark_update_btn);

            markListConstraintLayout = (ConstraintLayout) itemView.findViewById(R.id.mark_update_linear);
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
