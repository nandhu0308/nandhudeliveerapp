package com.example.raj.deliveryyy.report;

/**
 * Created by Raj on 29-Jun-17.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.raj.deliveryyy.R;

import java.util.ArrayList;

/**
 * Created by Raj on 19-Jun-17.
 */

public class Spinner_Report_Adapter_new extends BaseAdapter {
    Context context;
    ArrayList<String> data;

    public Spinner_Report_Adapter_new(Context context, ArrayList<String> data) {
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
            view = inflater.inflate(R.layout.relation_item_dalog, null);
            holder.RelationDescription=(TextView)view.findViewById(R.id.RelationDescription) ;

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.RelationDescription.setText(data.get(i));
//      holder.RelationDescription.setText(data.get(i).get());
        return view;
    }
    public class ViewHolder {
        TextView RelationDescription;

    }
}

