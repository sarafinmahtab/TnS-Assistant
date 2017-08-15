package com.sarafinmahtab.tnsassistant.student.stdcourses;

import android.content.Context;
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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arafin on 8/12/2017.
 */

public class CurrentCoursesAdapter extends RecyclerView.Adapter<CurrentCoursesAdapter.AllCoursesViewHolder> {

    private List<CourseItem> allCoursesListItem;
    private Context context;

    private ArrayList<CourseItem> newCoursesListItem;

    public CurrentCoursesAdapter(List<CourseItem> allCoursesListItem, Context context) {
        this.allCoursesListItem = allCoursesListItem;
        this.context = context;

        newCoursesListItem = new ArrayList<>();
        newCoursesListItem.addAll(this.allCoursesListItem);
    }

    @Override
    public AllCoursesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.allcourses_item, parent, false);

        return new AllCoursesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AllCoursesViewHolder holder, int position) {

        final CourseItem courseItem = newCoursesListItem.get(position);

        holder.textViewListCourseCode.setText(courseItem.getStdListCourseCode());

        //Selects any random color
//        Random rnd = new Random();
//        int randomSelectedColor = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));

        int[] randomColors = context.getResources().getIntArray(R.array.random_colors);

        //Selects random colors from color Array
//        int randomSelectedColor = randomColors[new Random().nextInt(randomColors.length)];

        //Maintains the sorted colors from color Array
        int randomSelectedColor = randomColors[position];

        GradientDrawable bgShape = (GradientDrawable)holder.allCoursesRelativeLayout.getBackground();
        bgShape.mutate();
        bgShape.setColor(randomSelectedColor);

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
        return (null != newCoursesListItem ? newCoursesListItem.size() : 0);
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

    public void checkQueryFromList(final String query) {

        int type = 1, i, j, m;
        newCoursesListItem.clear();

        for(i = 0; i < query.length(); i++) {
            if(query.charAt(i) >= '0' && query.charAt(i) <= '9') {
                type = 2;
            }
        }

        if(type == 1) {
            for(i = 0; i < allCoursesListItem.size(); i++)
            {
//				int k = 0;

                for(j = 0; j < allCoursesListItem.get(i).getStdListCourseTitle().length(); j++)
                {
                    if(j+query.length() > allCoursesListItem.get(i).getStdListCourseTitle().length()) {
                        break;
                    }

                    boolean ck = true;

                    for(m = 0; m < query.length(); m++)
                    {
                        if(query.charAt(m) != allCoursesListItem.get(i).getStdListCourseTitle().toLowerCase().charAt(j+m)) {
                            ck = false;
                        }
                    }

                    if(ck) {
                        newCoursesListItem.add(allCoursesListItem.get(i));
                        break;
                    }
                }
            }
        } else {
            for(i = 0; i < allCoursesListItem.size(); i++)
            {
//				int k = 0;

                for(j = 0; j < allCoursesListItem.get(i).getStdListCourseCode().length(); j++)
                {
                    if(j+query.length() > allCoursesListItem.get(i).getStdListCourseCode().length()) {
                        break;
                    }

                    boolean ck = true;

                    for(m = 0; m < query.length(); m++)
                    {
                        if(query.charAt(m) != allCoursesListItem.get(i).getStdListCourseCode().toLowerCase().charAt(j+m)) {
                            ck = false;
                        }
                    }

                    if(ck) {
                        newCoursesListItem.add(allCoursesListItem.get(i));
                        break;
                    }
                }
            }
        }

        notifyDataSetChanged();
    }
}
