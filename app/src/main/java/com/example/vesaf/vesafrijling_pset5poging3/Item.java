package com.example.vesaf.vesafrijling_pset5poging3;

import java.io.Serializable;

/**
 * Created by vesaf on 3/14/2017.
 */

public class Item implements Serializable {
    private String name;
    private int id, lId;
    private boolean done;

    public Item(String itemName, int itemId, boolean itemDone, int listId) {
        name = itemName;
        id = itemId;
        done = itemDone;
        lId = listId;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public boolean getDone() {
        return done;
    }

    public int getlId() {
        return lId;
    }

    public void setName(String newName) {
        name = newName;
    }

    public void setId(int newId) {
        id = newId;
    }

    public void setDone(boolean status) {
        done = status;
    }

    public void setlId(int newLID) {
        lId = newLID;
    }
}
