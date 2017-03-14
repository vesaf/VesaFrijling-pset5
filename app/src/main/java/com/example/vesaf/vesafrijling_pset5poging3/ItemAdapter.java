package com.example.vesaf.vesafrijling_pset5poging3;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by vesaf on 3/14/2017.
 */

public class ItemAdapter extends ArrayAdapter {
    private ArrayList<Item> items;
    private Context context;
    private ItemActivity itemActivity; //!?!
    private CheckBox itemCb;

    // Constructor
    public ItemAdapter(Context context, ArrayList<Item> data) {
        super(context,0, data);
        this.items = data;
        this.itemActivity = (ItemActivity) context;
        this.context = context;
    }

    // get the view and return it
    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_item, parent, false);
        }

        final Item item = items.get(pos);
        String name = item.getName();
        Boolean done = item.getDone();

        itemCb = (CheckBox) view.findViewById(R.id.checkBox);
        itemCb.setChecked(done);
        itemCb.setText(name);

        itemCb.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                itemActivity.deleteItem(item.getId());

                return true;
            }
        });

        itemCb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Check", "Pressed");
                itemActivity.itemDone(item.getId());
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
