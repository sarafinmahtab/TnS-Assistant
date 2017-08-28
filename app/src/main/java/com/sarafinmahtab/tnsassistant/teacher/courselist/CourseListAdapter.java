package com.sarafinmahtab.tnsassistant.teacher.courselist;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sarafinmahtab.tnsassistant.R;
import com.sarafinmahtab.tnsassistant.teacher.TeacherDashboard;

import java.util.List;

/**
 * Created by Arafin on 6/11/2017.
 */

public class CourseListAdapter extends RecyclerView.Adapter<CourseListAdapter.MyViewHolder> {

    private List<Course> listItem;
    private Context context;

    private String course_code, course_title, credit, session;

    private int selectedPosition = -1;

    public CourseListAdapter(List<Course> listItem, Context context) {
        this.listItem = listItem;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.course_row_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Course course = listItem.get(position);
        final int updatedPosition = position;

        course_code = "Code: " + course.getCourseCode();
        course_title = course.getCourseTitle();
        credit = "Credit: " + course.getCredit();
        session = "Session: " + course.getSession();

        holder.textViewCourse_code.setText(course_code);
        holder.textViewCourse_title.setText(course_title);
        holder.textViewCredit.setText(credit);
        holder.textViewSession.setText(session);

//        if(selectedPosition == updatedPosition) {
//            holder.itemView.setBackground(ContextCompat.getDrawable(context, R.drawable.cardview_click_effect));
//        } else {
//            holder.itemView.setBackground(ContextCompat.getDrawable(context, R.drawable.cardview_border));
//        }

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPosition = updatedPosition;
                notifyDataSetChanged();

                Intent intent = new Intent(context, TeacherDashboard.class);

                Bundle bundle = new Bundle();
                bundle.putString("teacher_id", course.getTeacherID());
                bundle.putString("course_id", course.getCourseID());
                bundle.putString("course_code", course.getCourseCode());
                bundle.putString("course_title", course.getCourseTitle());
                bundle.putString("credit", course.getCredit());
                bundle.putString("session", course.getSession());

                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listItem.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewCourse_code, textViewCourse_title, textViewCredit, textViewSession;
        LinearLayout linearLayout;
        CardView cardView;

        public MyViewHolder(View itemView) {
            super(itemView);

            textViewCourse_code = (TextView) itemView.findViewById(R.id.course_code_item);
            textViewCourse_title = (TextView) itemView.findViewById(R.id.course_title_item);
            textViewCredit = (TextView) itemView.findViewById(R.id.credit_item);
            textViewSession = (TextView) itemView.findViewById(R.id.session_item);

            cardView = (CardView) itemView.findViewById(R.id.cardview);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayout);
        }
    }
}
