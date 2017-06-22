package com.example.raj.deliveryyy.status;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.raj.deliveryyy.R;
import com.example.raj.deliveryyy.Successfull.Sucessfull;
import com.example.raj.deliveryyy.UnDeliveredActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by uma on 6/7/2017.
 */

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

    private ArrayList<RecyclerItem> items;
    private Context context;
    String statusDesc;

    public MyRecyclerViewAdapter(ArrayList<RecyclerItem> items, Context context) {
        this.items = items;
        this.context = context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final RecyclerItem recyclerItem = items.get(position);
        holder.statusdesc.setText(recyclerItem.getStatusDesc());
        holder.statusdesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if (recyclerItem.getStatusCode().equals("000")) {
                    intent = (new Intent(context, Sucessfull.class));
                    intent.putExtra("StatusCode", recyclerItem.getStatusCode());
                    context.startActivity(intent);
                } else {
                    intent = (new Intent(context, UnDeliveredActivity.class));
                    intent.putExtra("StatusCodeundelivery", recyclerItem.getStatusCode());
                    context.startActivity(intent);
                }
            }
        });
    }
    @Override
    public int getItemCount() {
        return items.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView statusdesc, statusCode;
        public ArrayList<RecyclerItem> items = new ArrayList<>();
        Context context;
        CardView statusDescription;
        public LinearLayout linearLayout;
        public ViewHolder(final View itemView) {
            super(itemView);
            statusdesc = (TextView) itemView.findViewById(R.id.textview2);
            statusDescription = (CardView) itemView.findViewById(R.id.cardView_statusDescription);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayout);
        }
        public List<RecyclerItem> getDesc() {
            return items;
        }
    }
}


