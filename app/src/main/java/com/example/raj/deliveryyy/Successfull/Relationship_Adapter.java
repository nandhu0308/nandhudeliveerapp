package com.example.raj.deliveryyy.Successfull;

/**
 * Created by Raj on 14-Jun-17.
 */

//public class Relationship_Adapter {
//}

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.raj.deliveryyy.R;

import java.util.ArrayList;


public class Relationship_Adapter extends BaseAdapter {
    Context context;
    ArrayList<Relation_model> data;

    public Relationship_Adapter(Context context, ArrayList<Relation_model> data) {
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
        holder.RelationDescription.setText(data.get(i).getRelationDescription());
        return view;
    }
    public class ViewHolder {
        TextView RelationDescription;

    }
}
