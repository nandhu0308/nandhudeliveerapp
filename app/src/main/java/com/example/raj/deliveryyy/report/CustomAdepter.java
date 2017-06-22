package com.example.raj.deliveryyy.report;

/**
 * Created by user on 6/8/2017.
 */


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.raj.deliveryyy.R;

import java.util.ArrayList;


public class CustomAdepter extends BaseAdapter {
    Context context;
    ArrayList<Report_Model> data;

    public CustomAdepter(Context context, ArrayList<Report_Model> data) {
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
            view = inflater.inflate(R.layout.item_list, null);
            holder.awbNo = (TextView) view.findViewById(R.id.awb_number);
            holder.deliveryEmpCode = (TextView) view.findViewById(R.id.deliveryEmpCode);

            holder.statusDate = (TextView) view.findViewById(R.id.statusDate);
            holder.revdBy = (TextView) view.findViewById(R.id.revdBy);
            holder.phoneNo = (TextView) view.findViewById(R.id.phoneNo);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.awbNo.setText(data.get(i).getAwbNo());
         holder.deliveryEmpCode.setText(data.get(i).getDeliveryEmpCode());

        holder.statusDate.setText(data.get(i).getStatusDate());
         holder.revdBy.setText(data.get(i).getRevdBy());
        holder.phoneNo.setText(data.get(i).getPhoneNo());


        return view;
    }
    public class ViewHolder {
        TextView awbNo,deliveryEmpCode,statusDate,revdBy,phoneNo;

    }
}
