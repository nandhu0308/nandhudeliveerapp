package com.example.raj.deliveryyy.awb;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.raj.deliveryyy.AwbDetails;
import com.example.raj.deliveryyy.R;

import java.util.List;

public class AwbAdapter extends RecyclerView.Adapter<AwbAdapter.ViewHolder> {

    private List<String> items;
    private Context context;

    public AwbAdapter(List<String> item, Context context) {
        this.items = item;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.awblist, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final String awbList = items.get(position);
        holder.awbno.setText(awbList);
        holder.awbno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AwbDetails.class);
                intent.putExtra("awbNumber", awbList);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView awbno;
        public LinearLayout linearLayout;

        public ViewHolder(final View itemView) {
            super(itemView);
            awbno = (TextView) itemView.findViewById(R.id.awbtextview);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayout);
        }

    }


}
