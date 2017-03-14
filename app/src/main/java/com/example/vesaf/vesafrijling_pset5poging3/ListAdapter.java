package com.example.vesaf.vesafrijling_pset5poging3;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by vesaf on 3/13/2017.
 */

public class ListAdapter extends ArrayAdapter {
    private ArrayList<List> lists;
    private Context context;
    private ListActivity listActivity; //!?!

    // Constructor
    public ListAdapter(Context context, ArrayList<List> data) {
        super(context,0, data);
        this.lists = data;
        this.listActivity = (ListActivity) context;
        this.context = context;
    }

    // get the view and return it
    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_item, parent, false);
        }

        final List list = lists.get(pos);
        String name = list.getName();

        TextView listTv = (TextView) view.findViewById(R.id.textView);
        listTv.setText(name);

        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                listActivity.deleteList(list.getId());

                return true;
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listActivity.openList(list.getId());
            }
        });

        return view;
    }

    // get the count
    @Override
    public int getCount() {
        return super.getCount();
    }

}
