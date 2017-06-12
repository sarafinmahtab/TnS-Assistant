package com.sarafinmahtab.tnsassistant.teacher;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sarafinmahtab.tnsassistant.R;

import java.util.List;

/**
 * Created by Arafin on 6/11/2017.
 */

public class CourseListAdapter extends RecyclerView.Adapter<CourseListAdapter.MyViewHolder> {

    private List<Course> listItem;
    private Context context;

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

        holder.textViewCourse_code.setText(course.getCourse_code());
        holder.textViewCourse_title.setText(course.getCourse_title());
        holder.textViewCredit.setText(course.getCredit());
        holder.textViewSession.setText(course.getSession());

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(context, "You've clicked " + course.getCourse_id(), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(context, TeacherPanel.class);

                Bundle bundle = new Bundle();
                bundle.putString("teacher_id", course.getCourse_id());
                bundle.putString("course_code", course.getCourse_code());
                bundle.putString("course_title", course.getCourse_title());
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

        public MyViewHolder(View itemView) {
            super(itemView);

            textViewCourse_code = (TextView) itemView.findViewById(R.id.course_code_item);
            textViewCourse_title = (TextView) itemView.findViewById(R.id.course_title_item);
            textViewCredit = (TextView) itemView.findViewById(R.id.credit_item);
            textViewSession = (TextView) itemView.findViewById(R.id.session_item);

            linearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayout);
        }
    }
}
