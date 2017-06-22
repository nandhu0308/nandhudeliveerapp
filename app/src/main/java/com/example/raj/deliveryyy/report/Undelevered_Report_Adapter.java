package com.example.raj.deliveryyy.report;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.raj.deliveryyy.R;

import java.util.ArrayList;


public class Undelevered_Report_Adapter extends BaseAdapter {
    Context context;
    ArrayList<Undeliverd_Model> data;

    public Undelevered_Report_Adapter(Context context, ArrayList<Undeliverd_Model> data) {
        this.context = context;
        this.data = data;
    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder holder;
        LayoutInflater inflater;
        if (view == null) {
            holder = new ViewHolder();
            inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.undelevered_item_list, null);
            holder.awbNo = (TextView) view.findViewById(R.id.awb_number);
            holder.statusTime = (TextView) view.findViewById(R.id.statusTime);
            holder.statusDate = (TextView) view.findViewById(R.id.statusDate);
            holder.statusCode = (TextView) view.findViewById(R.id.statusCode);
            holder.liveId = (TextView) view.findViewById(R.id.liveId);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.awbNo.setText(data.get(i).getAwbNo());
        holder.statusTime.setText(data.get(i).getStatusTime());
        holder.statusDate.setText(data.get(i).getStatusDate());
        holder.statusCode.setText(data.get(i).getStatusCode());
        holder.liveId.setText(data.get(i).getLiveId());
        return view;
    }
    public class ViewHolder {
        TextView awbNo,statusTime,statusDate,statusCode,liveId;

    }
}

