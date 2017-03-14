package com.example.vesaf.vesafrijling_pset5poging3;

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

public class ListActivity extends AppCompatActivity {

    private EditText nameEt;
    private ListAdapter adapter;
    private ArrayList<List> lists;

    SQLiteDatabase TodoDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        nameEt = (EditText) findViewById(R.id.nameEditText);

        createDatabase();

        setTitle("Lists");

        lists = getLists();

        setAdapter();
    }

    public void onClick(View v) {
        if (!nameEt.getText().toString().equals("")) {
            int id = addList();
            List list = new List(nameEt.getText().toString(), id);
            lists.add(list);
            nameEt.getText().clear();
            adapter.notifyDataSetChanged();
        }
    }

    public void setAdapter() {
        adapter = new ListAdapter(this, lists);
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
    }

    public void createDatabase() {

        try {
            TodoDB = this.openOrCreateDatabase("TODO",
                    MODE_PRIVATE, null);
            TodoDB.execSQL("CREATE TABLE IF NOT EXISTS lists " +
                    "(id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR TEXT NOT NULL);");
            TodoDB.execSQL("CREATE TABLE IF NOT EXISTS items " +
                    "(id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR TEXT NOT NULL," +
                    " done BOOLEAN DEFAULT FALSE, list INTEGER);");
        }
        catch(Exception e) {
            Log.e("ERROR", "Error creating database");
        }
    }

    public int addList() {
        String name = nameEt.getText().toString();

        TodoDB.execSQL("INSERT INTO lists (name) VALUES ('" +
            name + "');");
        Cursor cursor = TodoDB.rawQuery("SELECT * FROM lists WHERE id = (SELECT MAX(id)  FROM lists);", null);
        int idColumn = cursor.getColumnIndex("id");
        cursor.moveToFirst();
        int id = cursor.getInt(idColumn);

        return id;
    }

    public ArrayList<List> getLists() {
        Cursor cursor = TodoDB.rawQuery("SELECT * FROM lists", null);

        int idColumn = cursor.getColumnIndex("id");
        int nameColumn = cursor.getColumnIndex("name");

        cursor.moveToFirst();
        ArrayList<List> lists = new ArrayList<>();

        if(cursor != null && (cursor.getCount() > 0)) {

            do{
                int id = Integer.valueOf(cursor.getString(idColumn));
                String name = cursor.getString(nameColumn);
                List list = new List(name, id);

                lists.add(list);
                Log.d("Name:", list.getName());
                Log.d("Id:", String.valueOf(list.getId()));
            }while(cursor.moveToNext());
        }
        return lists;
    }

    public void deleteList(int id) {
        String idStr = String.valueOf(id);
        if (id > 0) {
            Log.d("LARGER", idStr);
        }
        else {
            Log.d("ZERO", idStr);
        }
        Log.d("Delete:", idStr);
        TodoDB.execSQL("DELETE FROM lists WHERE id = " + idStr + ";");
        TodoDB.execSQL("DELETE FROM items WHERE list = " + idStr + ";");

        for (int i = 0; i < lists.size(); i++) {
            if (lists.get(i).getId() == id) {
                lists.remove(i);
                break;
            }
        }

        setAdapter();
    }

    public void openList(int listId) {
        Intent intent = new Intent(this, ItemActivity.class);
        intent.putExtra("listId", listId);
        SharedPreferences prefs = getSharedPreferences("X", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("last_list", listId);
        editor.commit();
        startActivity(intent);
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

}
