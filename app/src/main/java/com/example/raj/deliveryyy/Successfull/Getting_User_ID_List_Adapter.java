package com.example.raj.deliveryyy.Successfull;

/**
 * Created by Raj on 14-Jun-17.
 */

//public class Getting_User_ID_List_Adapter {
//
//}

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.raj.deliveryyy.R;

import java.util.ArrayList;


public class Getting_User_ID_List_Adapter extends BaseAdapter {
    Context context;
    ArrayList<Getting_User_ID_List_Model> data;

    public Getting_User_ID_List_Adapter(Context context, ArrayList<Getting_User_ID_List_Model> data) {
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
          //  holder.RelationId=(TextView)view.findViewById(R.id.RelationId);
            holder.RelationDescription=(TextView)view.findViewById(R.id.RelationDescription) ;

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.RelationDescription.setText(data.get(i).getIdDescription());
      //  holder.RelationId.setText(data.get(i).getId());
        return view;
    }
    public class ViewHolder {
        TextView RelationDescription;

    }
}
