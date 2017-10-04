package com.sarafinmahtab.tnsassistant.teacher.studentlist;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.SimpleDraweeView;
import com.sarafinmahtab.tnsassistant.MySingleton;
import com.sarafinmahtab.tnsassistant.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arafin on 8/11/2017.
 */

public class StudentListAdapter extends RecyclerView.Adapter<StudentListAdapter.StdViewHolder> {

    private List<StudentItem> stdListItem;
    private Context context;

    private ArrayList<StudentItem> newStdListItem;

    public StudentListAdapter(List<StudentItem> stdListItem, Context context) {
        this.stdListItem = stdListItem;
        this.context = context;

        newStdListItem = new ArrayList<>();
        newStdListItem.addAll(this.stdListItem);
    }

    @Override
    public StdViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.student_item, parent, false);
        return new StdViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final StdViewHolder holder, int position) {
        final StudentItem stdItem = newStdListItem.get(position);

        if(!stdItem.getStdListDisplay().equals("")) {
            Uri uri = Uri.parse(stdItem.getStdListDisplay());
            holder.stdDisplayPic.setImageURI(uri);

            int color = Color.rgb(88, 88, 88);
            RoundingParams roundingParams = RoundingParams.fromCornersRadius(5f);
            roundingParams.setBorder(color, 1.0f);
            roundingParams.setRoundAsCircle(true);
            holder.stdDisplayPic.getHierarchy().setRoundingParams(roundingParams);
        }

        holder.textViewStdName.setText(stdItem.getStdListName());
        holder.textViewStdReg.setText(String.format("Reg ID: %s", stdItem.getStdListReg()));

        holder.stdMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_SEND);
                String[] recipients= {stdItem.getStdListMail()};
                intent.putExtra(Intent.EXTRA_EMAIL, recipients);
                intent.putExtra(Intent.EXTRA_SUBJECT,"");
                intent.putExtra(Intent.EXTRA_TEXT,"");
                intent.putExtra(Intent.EXTRA_CC,"");
                intent.setType("text/html");
                context.startActivity(Intent.createChooser(intent, "Send mail"));
            }
        });

        holder.stdCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(String.format("tel:%s", stdItem.getStdListPhone())));
                context.startActivity(intent);
            }
        });

        holder.stdLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Clicked " + stdItem.getStdListID(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != newStdListItem ? newStdListItem.size() : 0);
    }

    public static class StdViewHolder extends RecyclerView.ViewHolder {

        TextView textViewStdName, textViewStdReg;
        SimpleDraweeView stdDisplayPic;
        ImageButton stdCall, stdMail;
        LinearLayout stdLinearLayout;
        CardView std_cardView;

        public StdViewHolder(View itemView) {
            super(itemView);

            textViewStdName = itemView.findViewById(R.id.stdlist_name);
            textViewStdReg = itemView.findViewById(R.id.stdlist_reg);

            stdDisplayPic = itemView.findViewById(R.id.stdlist_display);
            stdCall = itemView.findViewById(R.id.std_call);
            stdMail = itemView.findViewById(R.id.std_mail);

            stdLinearLayout = itemView.findViewById(R.id.std_linearLayout);
            std_cardView = itemView.findViewById(R.id.std_cardview);
        }
    }

    public void checkQueryFromList(final String query) {

        int type = 1, i, j, m;
        newStdListItem.clear();

        for(i = 0; i < query.length(); i++) {
            if(query.charAt(i) >= '0' && query.charAt(i) <= '9') {
                type = 2;
            }
        }

        if(type == 1) {
            for(i = 0; i < stdListItem.size(); i++)
            {
//				int k = 0;

                for(j = 0; j < stdListItem.get(i).getStdListName().length(); j++)
                {
                    if(j+query.length() > stdListItem.get(i).getStdListName().length()) {
                        break;
                    }

                    boolean ck = true;

                    for(m = 0; m < query.length(); m++)
                    {
                        if(query.charAt(m) != stdListItem.get(i).getStdListName().toLowerCase().charAt(j+m)) {
                            ck = false;
                        }
                    }

                    if(ck) {
                        newStdListItem.add(stdListItem.get(i));
                        break;
                    }
                }
            }
        } else {
            for(i = 0; i < stdListItem.size(); i++)
            {
//				int k = 0;

                for(j = 0; j < stdListItem.get(i).getStdListReg().length(); j++)
                {
                    if(j+query.length() > stdListItem.get(i).getStdListReg().length()) {
                        break;
                    }

                    boolean ck = true;

                    for(m = 0; m < query.length(); m++)
                    {
                        if(query.charAt(m) != stdListItem.get(i).getStdListReg().toLowerCase().charAt(j+m)) {
                            ck = false;
                        }
                    }

                    if(ck) {
                        newStdListItem.add(stdListItem.get(i));
                        break;
                    }
                }
            }
        }

        notifyDataSetChanged();
    }
}
