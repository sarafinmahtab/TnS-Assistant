package com.sarafinmahtab.tnsassistant;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sarafinmahtab.tnsassistant.teacher.Course;

import java.util.ArrayList;

/**
 * Created by Arafin on 6/8/2017.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    ArrayList<Course> arrayList = new ArrayList<>();

    public RecyclerAdapter(ArrayList<Course> arrayList) {
        this.arrayList = arrayList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_row_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.course_code.setText(arrayList.get(position).getCourse_code());
        holder.course_title.setText(arrayList.get(position).getCourse_title());
        holder.credit.setText(arrayList.get(position).getCredit());
        holder.session.setText(arrayList.get(position).getSession());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView course_code, course_title, credit, session;

        public MyViewHolder(View itemView) {
            super(itemView);

            course_code = (TextView) itemView.findViewById(R.id.course_code_item);
            course_title = (TextView) itemView.findViewById(R.id.course_title_item);
            credit = (TextView) itemView.findViewById(R.id.credit_item);
            session = (TextView) itemView.findViewById(R.id.session_item);
        }
    }
}
