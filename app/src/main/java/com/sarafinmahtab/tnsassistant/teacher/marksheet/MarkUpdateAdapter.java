package com.sarafinmahtab.tnsassistant.teacher.marksheet;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sarafinmahtab.tnsassistant.R;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by Arafin on 8/23/2017.
 */

public class MarkUpdateAdapter extends RecyclerView.Adapter<MarkUpdateAdapter.MarkUpdateViewHolder> {


    private List<MarkListItem> markListItem;
    private Context context;

    private ArrayList<MarkListItem> newMarkListItem;

    public MarkUpdateAdapter(List<MarkListItem> markListItem, Context context) {
        this.markListItem = markListItem;
        this.context = context;

        newMarkListItem = new ArrayList<>();
        newMarkListItem.addAll(this.markListItem);
    }

    @Override
    public MarkUpdateViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.mark_update_item, parent, false);

        return new MarkUpdateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MarkUpdateViewHolder holder, int position) {

        final MarkListItem markListItem = newMarkListItem.get(position);

        holder.candidateReg.setText(markListItem.getCandidateReg());
        holder.candidateName.setText(markListItem.getCandidateName());

        holder.markUpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, markListItem.getMarkSheetID(), Toast.LENGTH_LONG).show();
            }
        });

        holder.markListConstraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "My Linear", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != newMarkListItem ? newMarkListItem.size() : 0);
    }

    public static class MarkUpdateViewHolder extends RecyclerView.ViewHolder{

        TextView candidateReg, candidateName;
        ImageButton markUpdateBtn;

        ConstraintLayout markListConstraintLayout;

        public MarkUpdateViewHolder(View itemView) {
            super(itemView);

            candidateReg = (TextView) itemView.findViewById(R.id.candidate_reg);
            candidateName = (TextView) itemView.findViewById(R.id.candidate_name);

            markUpdateBtn = (ImageButton) itemView.findViewById(R.id.mark_update_btn);

            markListConstraintLayout = (ConstraintLayout) itemView.findViewById(R.id.mark_update_linear);
        }
    }


    public void checkQueryFromList(final String query) {

        int type = 1, i, j, m;
        newMarkListItem.clear();

        for(i = 0; i < query.length(); i++) {
            if(query.charAt(i) >= '0' && query.charAt(i) <= '9') {
                type = 2;
            }
        }

        if(type == 1) {
            for(i = 0; i < markListItem.size(); i++)
            {
//				int k = 0;

                for(j = 0; j < markListItem.get(i).getCandidateName().length(); j++)
                {
                    if(j+query.length() > markListItem.get(i).getCandidateName().length()) {
                        break;
                    }

                    boolean ck = true;

                    for(m = 0; m < query.length(); m++)
                    {
                        if(query.charAt(m) != markListItem.get(i).getCandidateName().toLowerCase().charAt(j+m)) {
                            ck = false;
                        }
                    }

                    if(ck) {
                        newMarkListItem.add(markListItem.get(i));
                        break;
                    }
                }
            }
        } else {
            for(i = 0; i < markListItem.size(); i++)
            {
//				int k = 0;

                for(j = 0; j < markListItem.get(i).getCandidateReg().length(); j++)
                {
                    if(j+query.length() > markListItem.get(i).getCandidateReg().length()) {
                        break;
                    }

                    boolean ck = true;

                    for(m = 0; m < query.length(); m++)
                    {
                        if(query.charAt(m) != markListItem.get(i).getCandidateReg().toLowerCase().charAt(j+m)) {
                            ck = false;
                        }
                    }

                    if(ck) {
                        newMarkListItem.add(markListItem.get(i));
                        break;
                    }
                }
            }
        }

        notifyDataSetChanged();
    }
}
