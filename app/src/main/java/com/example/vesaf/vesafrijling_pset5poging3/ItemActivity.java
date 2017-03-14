package com.example.vesaf.vesafrijling_pset5poging3;

import android.app.*;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class ItemActivity extends AppCompatActivity {

    private EditText nameEt;
    private ItemAdapter adapter;
    private ArrayList<Item> items;
    private int listId;

    SQLiteDatabase TodoDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        nameEt = (EditText) findViewById(R.id.itemEditText);

        listId = getIntent().getIntExtra("listId", -1);

        if (listId == -1) {
            Log.d("last", "-1");
            SharedPreferences prefs = getSharedPreferences("X", MODE_PRIVATE);
            listId = prefs.getInt("last_list", -1);
            Log.d("listId", Integer.toString(listId));
        }

        if (listId < 0) {
            Log.e("ERROR", "Could not retrieve list ID");
        }

        createDatabase();

        Cursor cursor = TodoDB.rawQuery("SELECT * FROM lists WHERE id = " + Integer.toString(listId) + ";", null);
        int nameColumn = cursor.getColumnIndex("name");
        cursor.moveToFirst();
        setTitle(cursor.getString(nameColumn));

        items = getItems();

        setAdapter();
    }


    public void addItemClick(View v) {
        if (!nameEt.getText().toString().equals("")) {
            int id = addItem();
            Item item = new Item(nameEt.getText().toString(), id, false, listId);
            items.add(item);
            nameEt.getText().clear();
            adapter.notifyDataSetChanged();
        }
    }

    public void setAdapter() {
        adapter = new ItemAdapter(this, items);
        ListView listView = (ListView) findViewById(R.id.itemListView);
        listView.setAdapter(adapter);
    }

    public void createDatabase() {

        try {
            TodoDB = this.openOrCreateDatabase("TODO",
                    MODE_PRIVATE, null);
        }
        catch(Exception e) {
            Log.e("ERROR", "Error creating database");
        }
    }

    public int addItem() {
        String name = nameEt.getText().toString();
        String listIdStr = String.valueOf(listId);

        TodoDB.execSQL("INSERT INTO items (name, list) VALUES ('" +
                name + "', " + listIdStr + ");");
        Cursor cursor = TodoDB.rawQuery("SELECT * FROM items WHERE id = (SELECT MAX(id)  FROM items);", null);
        int idColumn = cursor.getColumnIndex("id");
        cursor.moveToFirst();
        int id = cursor.getInt(idColumn);

        return id;
    }

    public ArrayList<Item> getItems() {
        Cursor cursor = TodoDB.rawQuery("SELECT * FROM items WHERE list = " + listId, null);

        int idColumn = cursor.getColumnIndex("id");
        int nameColumn = cursor.getColumnIndex("name");
        int doneColumn = cursor.getColumnIndex("done");

        cursor.moveToFirst();
        ArrayList<Item> items = new ArrayList<>();

        if(cursor != null && (cursor.getCount() > 0)) {

            do{
                int id = Integer.valueOf(cursor.getString(idColumn));
                String name = cursor.getString(nameColumn);
                Boolean done = cursor.getInt(doneColumn) > 0;
                Log.d("Done", done.toString());
                Item item = new Item(name, id, done, listId);

                items.add(item);
                Log.d("Name:", item.getName());
                Log.d("Id:", String.valueOf(item.getId()));
            }while(cursor.moveToNext());
        }
        return items;
    }

    public void deleteItem(int id) {
        String idStr = String.valueOf(id);
        Log.d("Delete:", idStr);
        TodoDB.execSQL("DELETE FROM items WHERE id = " + idStr + ";");

        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getId() == id) {
                items.remove(i);
                break;
            }
        }

        setAdapter();
    }

    public void itemDone(int id) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getId() == id) {
                items.get(i).setDone(!items.get(i).getDone());
                TodoDB.execSQL("UPDATE items SET done = NOT done WHERE id = " + id + ";");
                break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        TodoDB.close();

        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences prefs = getSharedPreferences("X", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("lastActivity", getClass().getName());
        editor.commit();
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(ItemActivity.this, ListActivity.class));

    }
}
