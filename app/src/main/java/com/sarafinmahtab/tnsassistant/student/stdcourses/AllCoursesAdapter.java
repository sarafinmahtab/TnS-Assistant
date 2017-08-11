package com.sarafinmahtab.tnsassistant.student.stdcourses;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sarafinmahtab.tnsassistant.R;

import java.util.List;
import java.util.Random;

/**
 * Created by Arafin on 8/12/2017.
 */

public class AllCoursesAdapter extends RecyclerView.Adapter<AllCoursesAdapter.AllCoursesViewHolder> {

    private List<CourseItem> allCoursesListItem;
    private Context context;

    public AllCoursesAdapter(List<CourseItem> allCoursesListItem, Context context) {
        this.allCoursesListItem = allCoursesListItem;
        this.context = context;
    }

    @Override
    public AllCoursesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.allcourses_item, parent, false);

        return new AllCoursesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AllCoursesViewHolder holder, int position) {

        final CourseItem courseItem = allCoursesListItem.get(position);

        holder.textViewListCourseCode.setText(courseItem.getStdListCourseCode());

        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));

        GradientDrawable bgShape = (GradientDrawable)holder.allCoursesRelativeLayout.getBackground();
        bgShape.mutate();
        bgShape.setColor(color);

        holder.textViewListCourseTitle.setText(courseItem.getStdListCourseTitle());
        holder.textViewListCredit.setText(String.format("Credit: %s", courseItem.getStdListCredit()));
        holder.textViewListSemester.setText(String.format("Semester: %s", courseItem.getStdListSemester()));

        holder.allCoursesLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, courseItem.getStdListCourseID(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return allCoursesListItem.size();
    }

    public static class AllCoursesViewHolder extends RecyclerView.ViewHolder {

        TextView textViewListCourseCode, textViewListCourseTitle, textViewListCredit, textViewListSemester;

        RelativeLayout allCoursesRelativeLayout;
        LinearLayout allCoursesLinearLayout;
        CardView allCourses_cardView;

        public AllCoursesViewHolder(View itemView) {
            super(itemView);

            textViewListCourseCode = (TextView) itemView.findViewById(R.id.course_code_oval_text);
            textViewListCourseTitle = (TextView) itemView.findViewById(R.id.all_courses_title_item);
            textViewListCredit = (TextView) itemView.findViewById(R.id.all_courses_credit_item);
            textViewListSemester = (TextView) itemView.findViewById(R.id.all_courses_semester_item);

            allCoursesRelativeLayout = (RelativeLayout) itemView.findViewById(R.id.circle_course_code);
            allCoursesLinearLayout = (LinearLayout) itemView.findViewById(R.id.all_courses_linearLayout2);
            allCourses_cardView = (CardView) itemView.findViewById(R.id.all_courses_cardview);
        }
    }
}
